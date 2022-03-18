import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScooterOrder {

    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String color;

    // Конструктор по умолчанию заполняет все поля
    public ScooterOrder() {
        firstName = "Ivan";
        lastName = "Ivnov";
        address = "Saint-Petersburg";
        metroStation = 4;
        phone = "+7 800 355 35 35";
        rentTime = 5;
        deliveryDate = "2020-06-06";
        comment = "Please call me";
        color = "\"BLACK\"";
    }

    public String getJsonOrderBody() {
        String jsonBody = "{\n" +
                "    \"firstName\": \"" + firstName + "\",\n" +
                "    \"lastName\": \"" + lastName + "\",\n" +
                "    \"address\": \"" + address + "\",\n" +
                "    \"metroStation\": " + metroStation + ",\n" +
                "    \"phone\": \"" + phone + "\",\n" +
                "    \"rentTime\": " + rentTime + ",\n" +
                "    \"deliveryDate\": \"" + deliveryDate + "\",\n" +
                "    \"comment\": \"" + comment + "\",\n" +
                "    \"color\": [\n" +
                "        " + color + "\n" +
                "    ]\n" +
                "}";
        return jsonBody;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMetroStation(int metroStation) {
        this.metroStation = metroStation;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRentTime(int rentTime) {
        this.rentTime = rentTime;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
