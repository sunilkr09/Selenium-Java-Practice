package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import reportinglistener.CustomWebDriverListener;

import java.time.Duration;

public class Browser {
    public WebDriver openBrowser(String browserName) {
        browserName = browserName.toLowerCase();

        WebDriver driver;
        switch (browserName) {
            case DesktopBrowserType.CHROME:
                driver = openChromeBrowser();
                break;
            case DesktopBrowserType.FIREFOX:
                driver = openFirefoxBrowser();
                break;
            case DesktopBrowserType.CHROME_HEADLESS:
                driver = openHeadlessChrome();
                break;
            case DesktopBrowserType.EDGE_HEADLESS:
                driver = openHeadlessEdge();
                break;
            case DesktopBrowserType.FIREFOX_HEADLESS:
                driver = openHeadlessFirefox();
                break;
            case DesktopBrowserType.EDGE:
            default:
                driver = openEdgeBrowser();
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));
        driver.manage().window().maximize();

        CustomWebDriverListener listener = new CustomWebDriverListener();
        return new EventFiringDecorator<>(listener).decorate(driver);
    }

    private WebDriver openChromeBrowser() {
        return new ChromeDriver();
    }

    private WebDriver openFirefoxBrowser() {
        return new FirefoxDriver();
    }

    private WebDriver openHeadlessChrome() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        return new ChromeDriver(options);
    }

    private WebDriver openHeadlessEdge() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        return new EdgeDriver(options);
    }

    private WebDriver openHeadlessFirefox() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-headless");
        return new FirefoxDriver(options);
    }

    private WebDriver openEdgeBrowser() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        return new EdgeDriver(options);
    }
}
