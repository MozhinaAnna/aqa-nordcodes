package org.testTask.loginTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testTask.*;
import org.testng.annotations.Test;

public class RepeatTokenLoginTest extends BaseApi {

    @Test(description = "2 логина подряд с одним и тем же токеном")
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    public void repeatToken() {

        String userToken = GenerateToken.generateValidToken();
        ApiClient apiClient = new ApiClient();

        Stubs.create200AuthStub(wireMockServer, userToken);

        Response loginResponse1 = apiClient.callEndPoint(userToken, Action.LOGIN);

        Assertions.assertThat(loginResponse1
                        .statusCode()).isEqualTo(200);
        Assertions.assertThat(loginResponse1.jsonPath().getString("result"))
                .isEqualTo("OK");

        Response loginResponse2 = apiClient.callEndPoint(userToken, Action.LOGIN);

        Assertions.assertThat(loginResponse2.statusCode())
                .isEqualTo(409);
        Assertions.assertThat(loginResponse2.jsonPath().getString("result"))
                .isEqualTo("ERROR");

        apiClient.callEndPoint(userToken, Action.LOGOUT);
    }
}
