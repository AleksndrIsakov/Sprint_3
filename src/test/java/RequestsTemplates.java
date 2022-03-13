import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RequestsTemplates {

    public Response getRequest(String apiUrl, String jsonBody) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonBody)
                .when()
                .get(apiUrl);
    }

    public static Response postRequest(String apiUrl, String jsonBody) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonBody)
                .when()
                .post(apiUrl);
    }

    public Response putRequest(String apiUrl, String jsonBody) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonBody)
                .when()
                .put(apiUrl);
    }

    public static Response deleteRequest(String apiUrl, String jsonBody) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonBody)
                .when()
                .delete(apiUrl);
    }



}
