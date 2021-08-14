import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class TestOpenDNS {
    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(TestOpenDNS.class);
    // Читаем передаваемый параметр browser (-Dbrowser)

    String env = System.getProperty("browser", "chrome");
    // - Параметр -Doption=Имя_Опции для стратегии загрузки
    String str = System.getProperty("option", "normal");

    /*
    По умолчанию: browser - chrome, Имя_Опции - normal
    Варианты команд для запуска
    mvn clean test -Dbrowser=FiReFoX
    mvn clean test -Dbrowser=chrome
    mvn clean test -Doption='normal'
    mvn clean test -Doption=none
    mvn clean test -Dbrowser=firefox -Doption=normal
    mvn clean test -Dbrowser=chrome -Doption=none
*/

    @BeforeEach
    public void setUp() {
        logger.info("env -> " + env);
        logger.info("option -> " + str);
        driver = WebDriverFactory.getDriver(env.toLowerCase(), str.toLowerCase());
        logger.info("Start driver!");

    }

    @Test
    public void openPage() {
        // 1. Установка таймаута загрузки страницы = 20 секунд
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

        // 2. Открытие страницы DNS
        driver.get("https://www.dns-shop.ru/");
        logger.info("Открыта страница DNS - " + "https://www.dns-shop.ru/");
        //    Вывод заголовка страницы
        String title = driver.getTitle();
        logger.info("title - " + title.toString());
        //    Проскроллить вниз на 1000px
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script1 = "window.scrollBy(0,1000);";
        //    Вывод текущего URL
        String currentUrl = driver.getCurrentUrl();
        logger.info("current URL - " + currentUrl.toString());
        //    Закрытие всплывашки
        String btnYesXpath = "//a[contains(text(), \"Да\")]";
        WebElement btnYes = driver.findElement(By.xpath(btnYesXpath));
        btnYes.click();
        logger.info("Закрытие всплывашки");
        // 3. Активируем вкладку -> "Смартфоны и гаджеты"
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        String btnBytTechXpath = "//a[contains(text(), \"Смартфоны и гаджеты\")]";
        Actions action = new Actions(driver);
        WebElement gadgets = driver.findElement(By.xpath(btnBytTechXpath));
        action.moveToElement(gadgets).build().perform();
        logger.info("Активируем вкладку смарфоны и гаджеты");
        // 4. Переход к категории -> "Смартфоны"
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//a[contains(text(), 'Смартфоны')])[3]"))).click();
        logger.info("Выбор категории смартфоны");
        // 5. Сделать скриншот видимой области веб страницы
        try {
            File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage linkYesImage = ImageIO.read(file);
            ImageIO.write(linkYesImage, "png", new File("temp\\FirstPage.png"));
            logger.info("Скриншот сохранен в файле [temp\\FirstPage.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 6. Выбрать значение в фильтре Производитель Samsung -> //span[contains(text(), 'Samsung ')]
        js.executeScript(script1);
        WebElement samsung = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='samsung']/..")));
        samsung.click();
        logger.info("Самсунг выбран");
        // 7. Активируем объем оперативной памяти
        js.executeScript(script1);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), 'Объем оперативной памяти')]"))).click();
        logger.info("Активируем плашку 'Объем оперативной памяти'");
        // 8. Выбираем ОП = 8Гб.
        ((JavascriptExecutor) driver).executeScript("scroll(0,1500)");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value=\"i2ft\"]/.."))).click();
        logger.info("Выбор объема памяти");
        // 9. Применяем фильтр
        js.executeScript(script1);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), \"Применить\")]"))).click();
        logger.info("Применяем фильтры");
        // 10. Активируем сортировку по цене
        ((JavascriptExecutor) driver).executeScript("scroll(0,40)");

        try{
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), \"Сначала недорогие\")]/.."))).click();

        }catch(NoSuchElementException e ){
            e.printStackTrace();
        }
        logger.info("Активируем сортировку");

        // 11. Выбираем Сначала дорогие
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), \"Сначала дорогие\")]"))).click();
        logger.info("Выбираем дорогие");
        // 12. Сделать скриншот всей веб страницы
        try {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(100))
                    .takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\FullPage.png"));
            logger.info("Скриншот сохранен в файле [temp\\FullPage.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Дескриптор 1 окна
        String oldWindow = driver.getWindowHandle();
        logger.info("Старое окно:\n" + oldWindow.toString());
        // 13. Перейти по 1 ссылке в новое окно.
        String selectFirst = Keys.chord(Keys.SHIFT, Keys.RETURN);
        driver.findElement(By.xpath("//a[@class='catalog-product__name ui-link ui-link_black']")).sendKeys(selectFirst);
        //получаем набор дескрипторов текущих открытых окон
        List<String> browserTabs = new ArrayList(driver.getWindowHandles());
        logger.info("Список окон:\n" + browserTabs.toString());
        // 14. Переключиться на новое окно
        driver.switchTo().window(browserTabs.get(1)).manage().window().maximize();
        // Дескриптор нового окна
        String newWindow = driver.getWindowHandle();
        logger.info("Новое окно:\n" + newWindow.toString());
        // 15. Сделать скриншот видимой области веб страницы
        try {
            File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage linkYesImage = ImageIO.read(file);
            ImageIO.write(linkYesImage, "png", new File("temp\\NewPage.png"));
            logger.info("Скриншот сохранен в файле [temp\\NewPage.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  16. Вывод заголовка страницы
        String actualText = driver.findElement(By.xpath("//h1")).getText();
        String expectedText = "6.7\" Смартфон Samsung Galaxy Z Flip3 256 ГБ бежевый";
        logger.info("Ожидаемый заголовок ->" + expectedText.toString());
        logger.info("Актуальный заголовок ->" + actualText.toString());
        //  17. Проверка заголовков страниц
        Assert.assertEquals("Заголовки не совпадают -> ", expectedText, actualText);
        //  18. Переходим в характеристики
        ((JavascriptExecutor) driver).executeScript("scroll(0,500)");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), \"Характеристики\")]"))).click();
        ((JavascriptExecutor) driver).executeScript("scroll(0,2000)");
        //  19. Получаем значение ОП
        String memoryTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@class='table-params table-no-bordered']/tbody/tr[39]/td[2]"))).getText();
        String expectedMemoryTable = "8 Гб";
        //  17. Проверка значений ОП
        Assert.assertEquals("Указанные параметры не совпадают ->", expectedMemoryTable, memoryTable);
        logger.info("Ожидаемый объем ОП ->" + expectedMemoryTable);
        logger.info("Актуальный объем ОП ->" + memoryTable);

}

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Драйвер остановлен!");
        }
    }
}
