package org.testTask.loginTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testTask.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class InvalidTokenLoginTest extends BaseApi {

    @DataProvider(name = "provideTokens")
    public Object[][] provide() {
        return new Object[][]{
                {GenerateToken.generateInvalidToken()},
                {GenerateToken.generateLongToken()},
                {GenerateToken.generateShortToken()},
                {""},
                {" "},
                {null}
        };
    }


    @Test(dataProvider = "provideTokens", description = "Логин невалидным токеном")
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    public void loginInvalidToken(String userToken) {

        ApiClient apiClient = new ApiClient();
        Stubs.create200AuthStub(wireMockServer, userToken);

        Response loginResponse = apiClient.callEndPoint(userToken, Action.LOGIN);

        Assertions.assertThat(loginResponse.statusCode())
                .isEqualTo(400);
        Assertions.assertThat(loginResponse.jsonPath().getString("result"))
                .isEqualTo("ERROR");

        apiClient.callEndPoint(userToken, Action.LOGOUT);
    }
}
