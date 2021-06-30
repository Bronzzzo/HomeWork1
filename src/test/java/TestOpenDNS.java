import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;


public class TestOpenDNS {
    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(TestOpenDNS.class);
    // Читаем передаваемый параметр browser (-Dbrowser)
    String env = System.getProperty("browser", "chrome");


    @BeforeEach
    public void setUp() {
        logger.info("env = " + env);
        driver = WebDriverFactory.getDriver(env.toLowerCase());
        logger.info("Start driver!");
    }

    @Test
    public void openPage() {
        driver.get("https://www.dns-shop.ru/");
        logger.info("Открыта страница DNS - " + "https://www.dns-shop.ru/");

        // Вывод заголовка страницы
        String title = driver.getTitle();
        logger.info("title - " + title.toString());

        // Вывод текущего URL
        String currentUrl = driver.getCurrentUrl();
        logger.info("current URL - " + currentUrl.toString());

    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Драйвер остановлен!");
        }
    }
}
