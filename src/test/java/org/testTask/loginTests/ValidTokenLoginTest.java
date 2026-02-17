package org.testTask.loginTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testTask.*;
import org.testng.annotations.Test;

public class ValidTokenLoginTest extends BaseApi {

    @Test(description = "Логин с валидным токеном")
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    public void loginValidToken() {

        String userToken = GenerateToken.generateValidToken();
        ApiClient apiClient = new ApiClient();

        Stubs.create200AuthStub(wireMockServer, userToken);

        Response loginResponse = apiClient.callEndPoint(userToken, Action.LOGIN);

        Assertions.assertThat(loginResponse.statusCode())
                .isEqualTo(200);
        Assertions.assertThat(loginResponse.jsonPath().getString("result"))
                .isEqualTo("OK");

        apiClient.callEndPoint(userToken, Action.LOGOUT);
    }
}
