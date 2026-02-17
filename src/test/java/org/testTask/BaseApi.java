package org.testTask;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.awaitility.Awaitility;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.time.Duration;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;

public class BaseApi {
    protected static WireMockServer wireMockServer;
    private static Process appProcess;

    private static final int APP_PORT = 8080;
    private static final int MOCK_PORT = 8888;

    @BeforeSuite
    public static void setupEnvironment() throws IOException, InterruptedException {

        wireMockServer = new WireMockServer(options().port(MOCK_PORT));
        wireMockServer.start();
        System.out.println("WireMock started on: " + wireMockServer.baseUrl());

        String mockAuthUrl = wireMockServer.baseUrl();

        ProcessBuilder processBuilder = new ProcessBuilder(
                "java",
                "-Dsecret=qazWSXedc",
                "-Dmock=" + mockAuthUrl,
                "-jar",
                "c:/Juicy/JAVA/testTask/aqa/internal-0.0.1-SNAPSHOT.jar",
                "--server.port=" + APP_PORT
        );

        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(new File("target/app.log"));
        appProcess = processBuilder.start();

        RequestSpecification healthCheckSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(APP_PORT)
                .build();

        Awaitility.await()
                .atMost(Duration.ofSeconds(60))
                .pollInterval(Duration.ofSeconds(2))
                .ignoreException(ConnectException.class)
                .until(() -> given(healthCheckSpec).get("/").statusCode() > 0);

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = APP_PORT;
        System.out.println("RestAssured is configured for http://localhost:" + APP_PORT);
    }

    @AfterSuite
    public static void teardownEnvironment() {

        if (appProcess != null) {
            appProcess.destroyForcibly();
            System.out.println("Application process stopped.");
        }
        if (wireMockServer != null) {
            wireMockServer.stop();
            System.out.println("WireMock server stopped.");
        }
    }

    @BeforeMethod
    public void resetState() {
        wireMockServer.resetAll();
    }
}