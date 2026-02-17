package org.testTask.endToEndTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testTask.*;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class FullLifecycleTest extends BaseApi {

    @Test(description = "Полный жизненный цикл пользователя: LOGIN -> ACTION -> LOGOUT -> ACTION")
    @Story("User lifecycle")
    @Severity(SeverityLevel.CRITICAL)
    public void userLifecycle() {

        String userToken = GenerateToken.generateValidToken();
        ApiClient apiClient = new ApiClient();

        Stubs.create200AuthStub(wireMockServer, userToken);
        Response loginResponse = apiClient.callEndPoint(userToken, Action.LOGIN);

        loginResponse.then()
                .log().ifError()
                .statusCode(200)
                .body("result", equalTo("OK"));

        Stubs.create200ActionStub(wireMockServer, userToken);

        Response actionResponse = apiClient.callEndPoint(userToken, Action.ACTION);
        actionResponse.then()
                .log().ifError()
                .statusCode(200)
                .body("result", equalTo("OK"));

        Response logoutResponse = apiClient.callEndPoint(userToken, Action.LOGOUT);
        logoutResponse.then()
                .log().ifError()
                .statusCode(200)
                .body("result", equalTo("OK"));

        Response postLogoutActionResponse = apiClient.callEndPoint(userToken, Action.ACTION);
        postLogoutActionResponse.then()
                .log().all()
                .statusCode(403)
                .body("result", equalTo("ERROR"))
                .body("message", equalTo("Token " + "'" + userToken + "'" + " not found"));
    }
}


