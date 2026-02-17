package org.testTask;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiClient {

    @Step("Вызов /endpoint с токеном '{token}' и действием '{action}'")
    public Response callEndPoint(String token, Action action) {
        String requestBody = "token=" + token + "&action=" + action;

        String apiKey = "qazWSXedc";

        return given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .header("X-Api-Key", apiKey)
                .body(requestBody)
                .when()
                .post("/endpoint");
    }
}
