import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isA;
import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isA;


// Тесты на логин курьера
@Epic("Основное задание")
@Story("2. Логин курьера")
public class LoginCourierTests {

    private ArrayList<String> courier;
    private final String apiUrl = "/api/v1/courier/login";

    @Before
    // Подготовим данные для тестов
    public void setUp() {
        baseURI = "http://qa-scooter.praktikum-services.ru";
        courier = ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
    }

    @After
    // Удалим созданные данные
    public void tearDown(){
        try {
            int courierId = ScooterLoginCourier.getId(courier.get(0), courier.get(1));
            RequestsTemplates.deleteRequest("/api/v1/courier/" + courierId, "{}");
        } catch (NullPointerException e) {
            System.out.println("Пользователь не найден - удалять нечего");
        }
    }

    @Test
    @DisplayName("Проверка авторизации курьера в системе")
    public void checkCourierCanLogin(){
        String jsonBody = "{\"login\":\"" + courier.get(0) + "\","
                        + "\"password\":\"" + courier.get(1) + "\"}";

        RequestsTemplates.postRequest(apiUrl,jsonBody)
                .then().statusCode(200)
                .and()
                .body("id", isA(int.class));
    }

    @Test
    // Данный тест проверяет так же условие "если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;"
    @DisplayName("Проверка авторизации курьера в системе c некорректным логином")
    public void checkCourierCantLoginWithIncorrectLogin(){
        String jsonBody = "{\"login\":\"" + courier.get(1) + "\","
                + "\"password\":\"" + courier.get(1) + "\"}";

        RequestsTemplates.postRequest(apiUrl,jsonBody)
                .then().statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверка авторизации курьера в системе c некорректным паролем")
    public void checkCourierCantLoginWithIncorrectPassword(){
        String jsonBody = "{\"login\":\"" + courier.get(0) + "\","
                + "\"password\":\"" + courier.get(0) + "\"}";

        RequestsTemplates.postRequest(apiUrl,jsonBody)
                .then().statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверка авторизации курьера с пустым логином")
    public void checkCourierCantLoginWithEmptyLogin(){
        String jsonBody = "{\"login\":\"" + "" + "\","
                + "\"password\":\"" + courier.get(1) + "\"}";

        RequestsTemplates.postRequest(apiUrl,jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка авторизации курьера с пустым паролем")
    public void checkCourierCantLoginWithEmptyPassword(){
        String jsonBody = "{\"login\":\"" + courier.get(0) + "\","
                + "\"password\":\"" + "" + "\"}";

        RequestsTemplates.postRequest(apiUrl,jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка авторизации курьера без логина")
    public void checkCourierCantLoginWithoutLoginField(){
        String jsonBody = "{\"password\":\"" + courier.get(1) + "\"}";

        RequestsTemplates.postRequest(apiUrl,jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка авторизации курьера без пароля")
    public void checkCourierCantLoginWithoutPasswordField(){
        String jsonBody = "{\"login\":\"" + courier.get(0) + "\"}";

        RequestsTemplates.postRequest(apiUrl,jsonBody)
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
