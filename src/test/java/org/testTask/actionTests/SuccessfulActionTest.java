package org.testTask.actionTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testTask.*;
import org.testng.annotations.Test;

public class SuccessfulActionTest extends BaseApi {

    @Test(description = "Успешный ответ от /doAction после логина")
    @Story("Action")
    @Severity(SeverityLevel.CRITICAL)
    public void successfulAction() {

        String userToken = GenerateToken.generateValidToken();
        ApiClient apiClient = new ApiClient();

        Stubs.create200AuthStub(wireMockServer, userToken);
        apiClient.callEndPoint(userToken, Action.LOGIN);

        Stubs.create200ActionStub(wireMockServer, userToken);
        Response actionResponse = apiClient.callEndPoint(userToken, Action.ACTION);
        Assertions.assertThat(actionResponse.statusCode())
                .isEqualTo(200);
        Assertions.assertThat(actionResponse.jsonPath().getString("result"))
                .isEqualTo("OK");
    }
}
