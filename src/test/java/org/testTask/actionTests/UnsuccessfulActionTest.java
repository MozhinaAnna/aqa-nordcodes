package org.testTask.actionTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testTask.*;
import org.testng.annotations.Test;

public class UnsuccessfulActionTest extends BaseApi {

    @Test(enabled = false, description = "Неуспешный ответ от /doAction после успешного логина")
    @Story("Action")
    @Severity(SeverityLevel.CRITICAL)
    public void unsuccessfulAction() {

        String userToken = GenerateToken.generateValidToken();
        ApiClient apiClient = new ApiClient();

        Stubs.create200AuthStub(wireMockServer, userToken);
        apiClient.callEndPoint(userToken, Action.LOGIN);

        Stubs.create400ActionStub(wireMockServer, userToken);
        Response actionResponse = apiClient.callEndPoint(userToken, Action.ACTION);
        Assertions.assertThat(actionResponse.statusCode())
                .isEqualTo(400); //TODO сервер отвечает 500, убрать enabled = false после исправления
        Assertions.assertThat(actionResponse.jsonPath().getString("result"))
                .isEqualTo("ERROR");
    }
}
