package org.testTask.endToEndTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testTask.*;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class LoginLogoutTest extends BaseApi {

    @Test(description = "Логин и завершение сессии без других действий юзера: LOGIN -> LOGOUT -> LOGIN -> LOGOUT")
    @Story("User lifecycle")
    @Severity(SeverityLevel.CRITICAL)
    public void userLoginLogout() {

        String userToken = GenerateToken.generateValidToken();
        ApiClient apiClient = new ApiClient();

        Stubs.create200AuthStub(wireMockServer, userToken);

        Response loginResponse1 = apiClient.callEndPoint(userToken, Action.LOGIN);
        loginResponse1.then()
                .log().ifError()
                .statusCode(200)
                .body("result", equalTo("OK"));

        Response logoutResponse1 = apiClient.callEndPoint(userToken, Action.LOGOUT);
        logoutResponse1.then()
                .log().ifError()
                .statusCode(200)
                .body("result", equalTo("OK"));

        Response loginResponse2 = apiClient.callEndPoint(userToken, Action.LOGIN);
        loginResponse2.then()
                .log().ifError()
                .statusCode(200)
                .body("result", equalTo("OK"));

        Response logoutResponse2 = apiClient.callEndPoint(userToken, Action.LOGOUT);
        logoutResponse2.then()
                .log().ifError()
                .statusCode(200)
                .body("result", equalTo("OK"));
    }
}
