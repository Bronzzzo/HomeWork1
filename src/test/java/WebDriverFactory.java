import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;

public class WebDriverFactory {
    private static Logger logger = LogManager.getLogger(WebDriverFactory.class);

    public static WebDriver getDriver(String browserName, String strategyName) {
        switch (browserName) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-fullscreen");
                options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
                options.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
                options.addArguments("--incognito");
                switch (strategyName) {
                    case "normal":
                        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                        break;
                    case "eager":
                        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
                        break;
                    case "none":
                        options.setPageLoadStrategy(PageLoadStrategy.NONE);

                        return new ChromeDriver(options);
                }
                logger.info("Драйвер для браузера Google Chrome");
                return new ChromeDriver(options);

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--kiosk");
                firefoxOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
                firefoxOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
                firefoxOptions.addArguments("--private");
                switch (strategyName) {
                    case "normal":
                        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                        break;
                    case "eager":
                        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                        break;
                    case "none":
                        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                        return new FirefoxDriver(firefoxOptions);
                }
                logger.info("Драйвер для браузера Mozilla Firefox");
                return new FirefoxDriver(firefoxOptions);


            default:
                throw new RuntimeException("Incorrect browser name");
        }
    }
}
