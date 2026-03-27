package org.testTask;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testTask.testTask.TestConfig;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;

public class BaseApi {
    protected static WireMockServer wireMockServer;
    private static Process appProcess;
    private static final Logger log = LoggerFactory.getLogger(BaseApi.class);
    private static final int SHUTDOWN_WAIT_SECONDS = 10;

    @BeforeSuite
    public static void setupEnvironment() throws IOException, InterruptedException {

        wireMockServer = new WireMockServer(options().dynamicPort()); // динамическое выделение портов
        wireMockServer.start();
        // библиотека логирования SLF4J с реализацией Logback
        log.info("WireMock started on: {}", wireMockServer.baseUrl());

        String mockAuthUrl = wireMockServer.baseUrl();

        int appPort; //выделение динамических портов делает возможным параллельный запуск нескольких сьютов
        try (ServerSocket socket = new ServerSocket(0)) {
            appPort = socket.getLocalPort();
        }
        log.info("Found free port for app: " + appPort);

        //вынести все конфигурационные параметры в отдельный файл config.properties
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java",
                "-Dsecret=qazWSXedc",
                "-Dmock=" + mockAuthUrl,
                "-jar",
                "c:/Juicy/JAVA/testTask/aqa/internal-0.0.1-SNAPSHOT.jar",
                "--server.port=" + appPort
        );

        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(new File("target/app.log"));
        appProcess = processBuilder.start();

        RequestSpecification healthCheckSpec = new RequestSpecBuilder()
                .setBaseUri(TestConfig.APP_BASE_URL)
                .setPort(appPort)
                .build();

        Awaitility.await()
                .atMost(Duration.ofSeconds(60))
                .pollInterval(Duration.ofSeconds(2))
                .ignoreException(ConnectException.class)
//если у приложения есть специальный health-check эндпоинт (например, /actuator/health в Spring Boot),
//обращаться к нему и проверять не только статус, но и тело ответа
                .until(() -> given(healthCheckSpec).get("/").statusCode() > 0);

        RestAssured.baseURI = TestConfig.APP_BASE_URL;
//        RestAssured.baseURI = "http://localhost";

        RestAssured.port = appPort;
        log.info("RestAssured is configured for: " + TestConfig.APP_BASE_URL + ":" + appPort);
    }

    @AfterSuite
    public static void teardownEnvironment() {

        if (appProcess != null && appProcess.isAlive()) {

            appProcess.destroy(); // стандартный сигнал на завершение (SIGTERM)
            try {
                boolean exited = appProcess.waitFor(SHUTDOWN_WAIT_SECONDS, TimeUnit.SECONDS);

                if (!exited) {
                    // если процесс не завершился за отведенное время, убиваем его принудительно
                    log.warn("Application did not shut down within {} seconds. Forcing termination.", SHUTDOWN_WAIT_SECONDS);
                    appProcess.destroyForcibly(); //  эквивалент kill -9 в Linux, немедленно убивает процесс
                } else {
                    log.info("Application process stopped.");
                }
            } catch (InterruptedException e) {
                // Это исключение может быть брошено, если поток, выполняющий teardown,
                // будет прерван извне. В этом случае мы тоже принудительно убиваем процесс.
                log.error("Teardown thread was interrupted. Forcing application shutdown.", e);
                appProcess.destroyForcibly();
                // восстанавливаем флаг прерывания потока
                Thread.currentThread().interrupt();
            }

            if (wireMockServer != null) {
                wireMockServer.stop();
                log.info("WireMock server stopped.");
            }
        }
    }


    @BeforeMethod
    public void resetWireMock() {
        if (wireMockServer != null) {
            wireMockServer.resetAll(); // сбрасывает все моки, счетчики запросов и т.д.
            //Если не сбрасывать, то это может привести к неожиданным и трудноотлаживаемым падениям тестов.
            //Тесты перестают быть независимыми
            log.info("WireMock stubs reset.");
        }
    }
}