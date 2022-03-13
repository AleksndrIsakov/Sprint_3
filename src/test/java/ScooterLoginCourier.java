// Вспомогательный класс для получения идентификатора пользователя
public class ScooterLoginCourier {

        public static int getId(String login, String password) throws NullPointerException {
            String jsonBody = "{\"login\":\"" + login + "\","
                    + "\"password\":\"" + password + "\"}";

            return RequestsTemplates.postRequest("/api/v1/courier/login", jsonBody).getBody().jsonPath().get("id");
        }
}
