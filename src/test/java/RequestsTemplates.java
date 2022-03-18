import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RequestsTemplates {

    @Step("Выполнили GET запрос: {apiUrl}")
    public static Response getRequest(String apiUrl) {
        return given()
                .header("Content-type", "application/json")
                .get(apiUrl);
    }

    @Step("Выполнили POST запрос: {apiUrl}\n")
    public static Response postRequest(String apiUrl, String jsonBody) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonBody)
                .when()
                .post(apiUrl);
    }

    @Step("Выполнили PUT запрос: {apiUrl}")
    public static Response putRequest(String apiUrl, String jsonBody) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonBody)
                .when()
                .put(apiUrl);
    }

    @Step("Выполнили DELETE запрос: {apiUrl}")
    public static Response deleteRequest(String apiUrl) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(apiUrl);
    }


}
