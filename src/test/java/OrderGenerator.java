import java.util.List;

public class OrderGenerator {

    public static Order customOrder(List<String> colors) {
        Order order = Order.builder()
                .firstName("Имя заказчика")
                .lastName("Фамилия заказчика")
                .address("Адрес заказчика")
                .metroStation(4)
                .phone("+7 800 355 35 35")
                .rentTime(5)
                .deliveryDate("2020-06-06")
                .comment("Комментарий от заказчика")
                .colors(colors)
                .build();

        return order;
    }

}
