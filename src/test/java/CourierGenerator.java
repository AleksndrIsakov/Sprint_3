import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {

    public static Courier getRandom() {
        return Courier.builder()
                .login(RandomStringUtils.randomAlphabetic(10))
                .password(RandomStringUtils.randomAlphabetic(10))
                .firstName(RandomStringUtils.randomAlphabetic(10))
                .build();
    }
}
