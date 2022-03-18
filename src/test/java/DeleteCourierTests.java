import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

@Epic("Дополнительное задание")
@Story("1. Удалить курьера")
public class DeleteCourierTests {

    private int courierId;

    @Before
    // Подготовим данные для тестов
    public void setUp() {
        baseURI = "http://qa-scooter.praktikum-services.ru";
        ArrayList<String> courier = ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        try {
            courierId = ScooterLoginCourier.getId(courier.get(0), courier.get(1));
        } catch (NullPointerException e) {
            courierId = 0;
        }
    }

    @After
    // Удалим созданные данные
    public void tearDown(){
        if (courierId != 0)
            RequestsTemplates.deleteRequest("/api/v1/courier/" + courierId, "{}");
    }

    @Test
    @DisplayName("Успешное удаление курьера")
    public void deleteCourier() {
        Response response = RequestsTemplates.deleteRequest("/api/v1/courier/" + courierId, "{}");
        response.then().statusCode(200)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Запрос без id возвращает ошибку")
    public void deleteCourierWithoutId() {
        Response response = RequestsTemplates.deleteRequest("/api/v1/courier", "{}");
        response.then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Запрос с несуществующим id возвращает ошибку")
    public void deleteCourierWithNotExistId() {
        Response response = RequestsTemplates.deleteRequest("/api/v1/courier/-1", "{}");
        response.then().statusCode(404)
                .and()
                .body("message", equalTo("Курьера с таким id нет"));
    }
}
