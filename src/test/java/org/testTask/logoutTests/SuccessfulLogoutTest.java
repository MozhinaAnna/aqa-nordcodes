package org.testTask.logoutTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testTask.*;
import org.testng.annotations.Test;

public class SuccessfulLogoutTest extends BaseApi {

    @Test(description = "Успешное завершение сессии")
    @Story("Logout")
    @Severity(SeverityLevel.CRITICAL)
    public void successfulLogout() {

        String userToken = GenerateToken.generateValidToken();
        ApiClient apiClient = new ApiClient();

        Stubs.create200AuthStub(wireMockServer, userToken);
        apiClient.callEndPoint(userToken, Action.LOGIN);

        Response logoutResponse = apiClient.callEndPoint(userToken, Action.LOGOUT);
        Assertions.assertThat(logoutResponse.statusCode())
                .isEqualTo(200);
        Assertions.assertThat(logoutResponse.jsonPath().getString("result"))
                .isEqualTo("OK");
    }
}
