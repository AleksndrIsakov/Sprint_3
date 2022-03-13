import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isA;


public class LoginCourierTests {

    @Before
    public void setUp() {
        baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    private Response createRequest(String jsonBody) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonBody)
                .when()
                .post("/api/v1/courier/login");
    }

    @Test
    @DisplayName("Проверка авторизации курьера в системе")
    public void checkCourierCanLogin(){
        ArrayList<String> courier = ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        String jsonBody = "{\"login\":\"" + courier.get(0) + "\","
                        + "\"password\":\"" + courier.get(1) + "\"}";

        createRequest(jsonBody)
                .then().statusCode(200)
                .and()
                .body("id", isA(int.class));

    }

    @Test
    // Данный тест проверяет так же условие "если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;"
    @DisplayName("Проверка авторизации курьера в системе c некорректным логином")
    public void checkCourierCantLoginWithIncorrectLogin(){
        ArrayList<String> courier = ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        String jsonBody = "{\"login\":\"" + courier.get(1) + "\","
                + "\"password\":\"" + courier.get(1) + "\"}";

        createRequest(jsonBody)
                .then().statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Проверка авторизации курьера в системе c некорректным паролем")
    public void checkCourierCantLoginWithIncorrectPassword(){
        ArrayList<String> courier = ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        String jsonBody = "{\"login\":\"" + courier.get(0) + "\","
                + "\"password\":\"" + courier.get(0) + "\"}";

        createRequest(jsonBody)
                .then().statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Проверка авторизации курьера с пустым логином")
    public void checkCourierCantLoginWithEmptyLogin(){
        ArrayList<String> courier = ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        String jsonBody = "{\"login\":\"" + "" + "\","
                + "\"password\":\"" + courier.get(1) + "\"}";

        createRequest(jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Проверка авторизации курьера с пустым паролем")
    public void checkCourierCantLoginWithEmptyPassword(){
        ArrayList<String> courier = ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        String jsonBody = "{\"login\":\"" + courier.get(0) + "\","
                + "\"password\":\"" + "" + "\"}";

        createRequest(jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Проверка авторизации курьера без логина")
    public void checkCourierCantLoginWithoutLoginField(){
        ArrayList<String> courier = ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        String jsonBody = "{\"password\":\"" + courier.get(1) + "\"}";

        createRequest(jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Проверка авторизации курьера без пароля")
    public void checkCourierCantLoginWithoutPasswordField(){
        ArrayList<String> courier = ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        String jsonBody = "{\"login\":\"" + courier.get(0) + "\"}";

        createRequest(jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));

    }
}
