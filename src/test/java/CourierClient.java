import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient extends ScooterRestClient {
    private static final String COURIER_PATH = "api/v1/courier/";

    @Step("Авторизация курьера {credentials}")
    public ValidatableResponse login(CourierCredentials credentials) {
            return given()
                    .spec(getBaseSpec())
                    .body(credentials)
                    .when()
                    .post(COURIER_PATH + "login")
                    .then();
    }

    @Step("Создание курьера {courier}")
    public ValidatableResponse create(Courier courier){
            return given()
                    .spec(getBaseSpec())
                    .body(courier)
                    .when()
                    .post(COURIER_PATH)
                    .then();

    }

    @Step("Удаление курьера с id: {courierId}")
    public ValidatableResponse delete(int courierId) {
            return given()
                    .spec(getBaseSpec())
                    .when()
                    .delete(COURIER_PATH + ((courierId < 0)? "":courierId))
                    .then();
    }

}
