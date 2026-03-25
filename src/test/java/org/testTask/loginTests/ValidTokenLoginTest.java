package org.testTask.loginTests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testTask.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ValidTokenLoginTest extends BaseApi {

    @Test(description = "Логин с валидным токеном")
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    public void loginValidToken() {

        String userToken = GenerateToken.generateValidToken();
        ApiClient apiClient = new ApiClient();

        Stubs.create200AuthStub(wireMockServer, userToken);

        Response loginResponse = apiClient.callEndPoint(userToken, Action.LOGIN);

        //этот вариант проверок не дойдет до второй проверки, если упала первая
        Assertions.assertThat(loginResponse.statusCode())
                .isEqualTo(200);
        Assertions.assertThat(loginResponse.jsonPath().getString("result"))
                .isEqualTo("OK");

        //вариант проверок с помощью TetsNG
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(loginResponse.statusCode(), 200,
                "Проверка: Статус-код должен быть 200 OK");

        softAssert.assertEquals(loginResponse.jsonPath().
                getString("result"), "OK",
                "Проверка поля 'result' в теле ответа");

        // если хотя бы одно из требований не выполнилось, выбросит одно большое исключение со всеми ошибками
        softAssert.assertAll();


        //вариант проверок с помощью AssertJ
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(loginResponse.statusCode())
                .as("Проверка статус-кода") // .as() добавляет кастомное сообщение к ошибке
                .isEqualTo(200);

        softly.assertThat(loginResponse.jsonPath().getString("result"))
                .as("Проверка поля 'result' в теле ответа")
                .isEqualTo("OK");

        // если хотя бы одно из требований не выполнилось, выбросит одно большое исключение со всеми ошибками
        softly.assertAll();

        apiClient.callEndPoint(userToken, Action.LOGOUT);
    }
}
