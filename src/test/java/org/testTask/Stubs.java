package org.testTask;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.qameta.allure.Step;

public class Stubs {

    @Step("Создание заглушки 200 /auth для токена '{token}'")
    public static void create200AuthStub(WireMockServer server, String token) {

        server.stubFor(
                WireMock.post(WireMock.urlEqualTo("/auth"))
                        .withHeader("Content-Type", WireMock.containing("application/x-www-form-urlencoded"))
                        .withHeader("Accept", WireMock.equalTo("application/json"))

                        .withRequestBody(WireMock.containing("token=" + token))

                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"status\":\"SUCCESS\", \"message\":\"Authentication processed successfully\"}")
                        )
        );
    }

    @Step("Создание заглушки 200 /auth для токена '{token}'")
    public static void create401AuthStub(WireMockServer server, String token) {

        server.stubFor(
                WireMock.post(WireMock.urlEqualTo("/auth"))
                        .withHeader("Content-Type", WireMock.containing("application/x-www-form-urlencoded"))
                        .withHeader("Accept", WireMock.equalTo("application/json"))

                        .withRequestBody(WireMock.containing("token=" + token))

                        .willReturn(WireMock.aResponse()
                                .withStatus(401)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"status\":\"Unauthorized\", \"message\":\"Authentication failed\"}")
                        )
        );
    }

    @Step("Создание заглушки 200 /doAction для токена '{token}'")
    public static void create200ActionStub(WireMockServer server, String token) {
        server.stubFor(
                WireMock.post(WireMock.urlEqualTo("/doAction"))
                        .withHeader("Content-Type", WireMock.containing("application/x-www-form-urlencoded"))
                        .withHeader("Accept", WireMock.equalTo("application/json"))

                        .withRequestBody(WireMock.containing("token=" + token))

                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"status\":\"COMPLETED\"}")
                        )
        );
    }

    @Step("Создание заглушки /doAction для токена '{token}'")
    public static void create400ActionStub(WireMockServer server, String token) {
        server.stubFor(
                WireMock.post(WireMock.urlEqualTo("/doAction"))
                        .withHeader("Content-Type", WireMock.containing("application/x-www-form-urlencoded"))
                        .withHeader("Accept", WireMock.equalTo("application/json"))

                        .withRequestBody(WireMock.containing("token=" + token))

                        .willReturn(WireMock.aResponse()
                                .withStatus(400)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"status\":\"Bad Request\"}")
                        )
        );
    }
}
