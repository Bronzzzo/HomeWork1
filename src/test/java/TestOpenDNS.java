import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;


public class TestOpenDNS {
    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(TestOpenDNS.class);
    // Читаем передаваемый параметр browser (-Dbrowser)
    String env = System.getProperty("browser", "chrome");
    // - Параметр -Doption=Имя_Опции для стратегии загрузки
    String option = System.getProperty("strategy", "normal");

    /*
    Варианты команд для запуска
    mvn clean test -Dbrowser=FiReFoX
    mvn clean test -Dbrowser=chrome
    mvn clean test -Doption='normal'
    mvn clean test -Doption=none
*/

    @BeforeEach
    public void setUp() {
        logger.info("env -> " + env);
        logger.info("strategy -> " + option);
        driver = WebDriverFactory.getDriver(env.toLowerCase(), option.toLowerCase());
        logger.info("Start driver!");

    }

    @Test
    public void openPage() {
        // переход по ссылке
        driver.get("https://www.dns-shop.ru/");



        logger.info("Открыта страница DNS - " + "https://www.dns-shop.ru/");
        //ожидания
        WebElement element = driver.findElement(By.xpath("//a[@class='btn btn-additional']"));
        element.click();
        // Вывод заголовка страницы
        String title = driver.getTitle();
        logger.info("title - " + title.toString());

        // Вывод текущего URL
        String currentUrl = driver.getCurrentUrl();
        logger.info("current URL - " + currentUrl.toString());


        WebElement technique = driver.findElement(By.xpath("//a[@href='/catalog/17a8e9b716404e77/bytovaya-texnika/']"));
        technique.click();

        //список
        List<WebElement> value = driver.findElements(By.xpath("//span[@class='subcategory__title']"));
        for (WebElement e: value) {
            logger.info("value ->" + e.getText());
        }

        // Создание Cookie
        logger.info("Куки, которое добавили мы");
        driver.manage().addCookie(new Cookie("Cookie 1", "This Is Cookie 1"));
        Cookie cookie1  = driver.manage().getCookieNamed("Cookie 1");
        logger.info(String.format("Domain: %s", cookie1.getDomain()));
        logger.info(String.format("Expiry: %s",cookie1.getExpiry()));
        logger.info(String.format("Name: %s",cookie1.getName()));
        logger.info(String.format("Path: %s",cookie1.getPath()));
        logger.info(String.format("Value: %s",cookie1.getValue()));
        logger.info("--------------------------------------");

// Добавляем задержку sleep
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Драйвер остановлен!");
        }
    }
}
