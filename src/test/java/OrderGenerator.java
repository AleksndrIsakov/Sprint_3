import com.github.javafaker.Faker;

import java.util.List;
import java.util.Locale;

public class OrderGenerator {

    public static String changeLength(String sentence, int length) {
        if (sentence.length() <= length) return  sentence;
        return sentence.substring(1,length);
    }

    public static Order customOrder(List<String> colors) {
        Faker faker = new Faker(Locale.forLanguageTag("ru"));
        Order order = Order.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .address(faker.address().fullAddress())
                .metroStation(4)
                .phone(faker.phoneNumber().cellPhone())
                .rentTime(5)
                .deliveryDate("2020-06-06")
                .comment(changeLength(faker.hitchhikersGuideToTheGalaxy().marvinQuote(), 255))
                .colors(colors)
                .build();

        return order;
    }

}
