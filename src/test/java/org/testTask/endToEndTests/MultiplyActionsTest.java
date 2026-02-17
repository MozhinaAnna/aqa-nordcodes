package org.testTask.endToEndTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testTask.*;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class MultiplyActionsTest extends BaseApi {

    @Test(description = "Генерация нескольких действий подряд: LOGIN -> ACTION -> ACTION -> ACTION -> LOGOUT")
    @Story("User lifecycle")
    @Severity(SeverityLevel.CRITICAL)
    public void userMultiplyActions() {

        String userToken = GenerateToken.generateValidToken();
        ApiClient apiClient = new ApiClient();

        Stubs.create200AuthStub(wireMockServer, userToken);

        Response loginResponse = apiClient.callEndPoint(userToken, Action.LOGIN);
        loginResponse.then()
                .log().ifError()
                .statusCode(200)
                .body("result", equalTo("OK"));

        Stubs.create200ActionStub(wireMockServer, userToken);

        Response actionResponse1 = apiClient.callEndPoint(userToken, Action.ACTION);
        actionResponse1.then()
                .log().ifError()
                .statusCode(200)
                .body("result", equalTo("OK"));

        Response actionResponse2 = apiClient.callEndPoint(userToken, Action.ACTION);
        actionResponse2.then()
                .log().ifError()
                .statusCode(200)
                .body("result", equalTo("OK"));

        Response actionResponse3 = apiClient.callEndPoint(userToken, Action.ACTION);
        actionResponse3.then()
                .log().ifError()
                .statusCode(200)
                .body("result", equalTo("OK"));

        Response logoutResponse = apiClient.callEndPoint(userToken, Action.LOGOUT);
        logoutResponse.then()
                .log().ifError()
                .statusCode(200)
                .body("result", equalTo("OK"));
    }
}
