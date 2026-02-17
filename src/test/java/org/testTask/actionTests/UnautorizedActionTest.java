package org.testTask.actionTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testTask.*;
import org.testng.annotations.Test;

public class UnautorizedActionTest extends BaseApi {

    @Test(description = "Попытка отправки запроса /doAction после неуспешного логина (401 от мок-сервера)")
    @Story("Action")
    @Severity(SeverityLevel.CRITICAL)
    public void login401Action() {

        String userToken = GenerateToken.generateValidToken();
        ApiClient apiClient = new ApiClient();

        Stubs.create401AuthStub(wireMockServer, userToken);
        apiClient.callEndPoint(userToken, Action.LOGIN);

        Stubs.create200ActionStub(wireMockServer, userToken);
        Response actionResponse = apiClient.callEndPoint(userToken, Action.ACTION);
        Assertions.assertThat(actionResponse.statusCode())
                .isEqualTo(403);
        Assertions.assertThat(actionResponse.jsonPath().getString("result"))
                .isEqualTo("ERROR");
    }

    @Test(description = "Попытка отправки запроса /doAction после неуспешного логина (невалидный токен)")
    @Story("Action")
    @Severity(SeverityLevel.CRITICAL)
    public void invalidTokenAction() {

        String userToken = GenerateToken.generateInvalidToken();
        ApiClient apiClient = new ApiClient();

        Stubs.create200AuthStub(wireMockServer, userToken);
        apiClient.callEndPoint(userToken, Action.LOGIN);

        Stubs.create200ActionStub(wireMockServer, userToken);
        Response actionResponse = apiClient.callEndPoint(userToken, Action.ACTION);
        Assertions.assertThat(actionResponse.statusCode())
                .isEqualTo(400);
        Assertions.assertThat(actionResponse.jsonPath().getString("result"))
                .isEqualTo("ERROR");
    }
}
