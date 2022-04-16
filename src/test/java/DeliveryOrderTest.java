import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryOrderTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    private Faker faker;
    @BeforeEach
    void setUp() {

        faker = new Faker (new Locale("ru"));
    }

    @Test
    void shouldSendDeliveryOrderData() {
        String address = faker.address().city();
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().phoneNumber();
        String date = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String date2 = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        System.out.println(address);
        System.out.println(date);
        System.out.println(name);
        System.out.println(phone);

        open ("http://localhost:9999");
        $("[Data-test-id=city] input").setValue(address);
        $("[Data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[Data-test-id=date] input").setValue(date);
        $("[Data-test-id=name] input").setValue(name);
        $("[Data-test-id=phone] input").setValue(phone);
        $(By.cssSelector("[class='checkbox__box']")).click();
        $$("button").find(exactText("Запланировать")).click();
        $("[Data-test-id=date] input").setValue(date2);
        $$("button").find(exactText("Запланировать")).click();
        $(withText("Перепланировать")).shouldBe(visible, Duration.ofSeconds(15));
        $(withText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));

    }

}
