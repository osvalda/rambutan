package com.github.osvalda.rambutan.uitest.framework;

import com.google.common.collect.ImmutableMap;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

@Slf4j
@Listeners(TestLogListener.class)
public abstract class BaseTest implements IHookable {

    private static String USER_ROLE = "user";
    protected final String loginUrl =  "login";

    private static final String EMAIL_KEY = "user.email";
    private static final String PASSWORD_KEY = "user.password";
    private static final String TIMEOUT_KEY = "webdriver.timeout";
    private static final String SITE_URL_KEY = "site.url";

    @Getter
    private static String email;

    @Getter
    private static String password;

    @Getter
    private static long timeout;

    @Getter
    private static String siteUrl;

    private static Properties envProperties;

    private WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void environmentSetup(ITestContext ctx) {
        String browser = System.getenv("BROWSER");
        if (browser == null){
            browser = "default";
        }
        setupWebDriver(browser);

        String environment = getEnvironmentName();

        allureEnvironmentWriter(ImmutableMap.<String, String>builder()
                        .put("Environment Name", environment)
                        .put("URL", getSiteUrl())
                        .put("Browser Type", browser)
                        .put("Suite", ctx.getCurrentXmlTest().getSuite().getName())
                        .build(), System.getProperty("user.dir")
                        + "/build/allure-results/");
    }

    static {
        Properties userProperties = getPropertiesFile(USER_ROLE);
        email = userProperties.getProperty(EMAIL_KEY);
        password = userProperties.getProperty(PASSWORD_KEY);

        envProperties = getPropertiesFile(getEnvironmentName());
        timeout = Long.parseLong(envProperties.getProperty(TIMEOUT_KEY));
        siteUrl = envProperties.getProperty(SITE_URL_KEY);
    }

    private static String getEnvironmentName() {
        String environment = System.getenv("ENV");
        log.info("The {} environment is chosen.",environment );
        if (environment == null) {
            throw new VerifyError("The ENV environment variable must contain the ".concat(
                    "name of the pod where the tests are going to run!"));
        }
        return environment;
    }

    private static Properties getPropertiesFile(String fileName) {
        String filePath = "environments/" + fileName + ".properties";
        log.info("Open properties file: {}", filePath);
        try {
            InputStream propertiesStream = BaseTest.class.getClassLoader().getResourceAsStream(filePath);
            Properties prop = new Properties();
            prop.load(propertiesStream);
            return prop;
        } catch (IOException | NullPointerException e) {
            log.error(e.getLocalizedMessage());
            throw new VerifyError("The ".concat(filePath).concat(" file is corrupted or missing!"));
        }
    }

    private void setupWebDriver(String browserType) {
        switch (browserType) {
            case "safari":
                WebDriverManager.chromedriver().setup();
                driver = new SafariDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "firefox-headless":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(true);
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "chrome-headless":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("no-sandbox");
                chromeOptions.addArguments("headless");
                chromeOptions.addArguments("disable-dev-shm-usage");
                chromeOptions.addArguments("window-size=1800,950");
                chromeOptions.addArguments("disable-extensions");
                driver = new ChromeDriver(chromeOptions);
        }
        driver.manage().timeouts().pageLoadTimeout(getTimeout(), TimeUnit.MILLISECONDS);
        driver.manage().window().maximize();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver = null;
            log.info("Driver is quited.");
        }
    }

    protected WebDriver getDriver() {
        if (driver != null)
            return driver;

        String browser = System.getenv("BROWSER");
        if (browser == null){
            browser = "default";
        }

        setupWebDriver(browser);
        return getDriver();
    }

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        callBack.runTestMethod(testResult);
        if (testResult.getThrowable() != null) {
            attachScreenShot();
            attachTitle();
            attachSiteUrl();
        }
    }

    @Attachment(value = "ScreenShot of failed page", type = "image/png")
    private byte[] attachScreenShot() {
        if (getDriver() != null) {
            TakesScreenshot scrShot = ((TakesScreenshot) getDriver());
            return scrShot.getScreenshotAs(OutputType.BYTES);
        }
        return null;
    }

    @Attachment(value = "Title of failed page")
    private String attachTitle() {
        if (getDriver() != null) {
            return getDriver().getTitle();
        }
        return null;
    }

    @Attachment(value = "URL of failed page", type = "text/uri-list")
    private String attachSiteUrl() {
        if (getDriver() != null) {
            return getDriver().getCurrentUrl();
        }
        return null;
    }

    protected void attachScreenShot(String message) {
        if (getDriver() != null) {
            TakesScreenshot scrShot = ((TakesScreenshot) getDriver());
            Allure.addByteAttachmentAsync(message, "image/png",
                    () -> scrShot.getScreenshotAs(OutputType.BYTES));
        }
    }

}
