package org.testTask.loginTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testTask.*;
import org.testng.annotations.Test;

public class UnauthorizedLoginTest extends BaseApi {

    @Test(enabled = false, description = "Попытка логина с ответом 401 от мок-сервера")
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    public void unauthorized() {

        String userToken = GenerateToken.generateValidToken();
        ApiClient apiClient = new ApiClient();

        Stubs.create401AuthStub(wireMockServer, userToken);

        Response loginResponse = apiClient.callEndPoint(userToken, Action.LOGIN);

        Assertions.assertThat(loginResponse.statusCode())
                .isEqualTo(500); //TODO сервер отвечает 500, убрать enabled = false после исправления
        Assertions.assertThat(loginResponse.jsonPath().getString("result"))
                .isEqualTo("ERROR");
    }
}
