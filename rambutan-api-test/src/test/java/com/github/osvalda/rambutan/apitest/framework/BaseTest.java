package com.github.osvalda.rambutan.apitest.framework;

import com.github.osvalda.rambutan.apitest.framework.supplementary.TestLogListener;
import com.google.common.collect.ImmutableMap;


import io.github.osvalda.pitaya.PitayaCoverageReporter;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

@Slf4j
@Listeners({PitayaCoverageReporter.class, TestLogListener.class})
public abstract class BaseTest {

    protected static final String ACCESS_TOKEN = "accessToken";

    private static final String USER_ROLE = "user";
    private static final String EMAIL_KEY = "user.email";
    private static final String PASSWORD_KEY = "user.password";
    private static final String SITE_URL_KEY = "site.url";
    private static final String API_URL_KEY = "api.url";

    public static String loginUrl;

    @Getter
    protected static String email;

    @Getter
    protected static String password;

    @Getter
    private static String accessToken;

    private static String environment;

    protected static final String GET = "GET ";
    protected static final String PUT = "PUT ";
    protected static final String POST = "POST ";
    protected static final String DELETE = "DELETE ";
    protected static final String PATCH = "PATCH ";

    @BeforeSuite(alwaysRun = true)
    @Step("Login as user and save accessToken")
    public static void login() {
        log.info("Login with user {}", email);

        log.info("Retrieved access token: {}", accessToken);
    }

    static {
        environment = getEnvironmentName();
        Properties environmentProperties = getPropertiesFile(environment);

        RestAssured.baseURI = environmentProperties.getProperty(API_URL_KEY);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        Properties userProperties = getPropertiesFile(USER_ROLE);
        email = userProperties.getProperty(EMAIL_KEY);
        password = userProperties.getProperty(PASSWORD_KEY);

        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Environment Name", environment)
                        .put("API URL", RestAssured.baseURI)
                        .put("Site URL", environmentProperties.getProperty(SITE_URL_KEY))
                        .build(), System.getProperty("user.dir")
                        + "/build/allure-results/");
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

    private static String getEnvironmentName() {
        String environment = System.getenv("ENV");
        log.info("The {} environment is chosen.",environment );
        if (environment == null) {
            throw new VerifyError("The ENV environment variable must contain the ".concat(
                    "name of the pod where the tests are going to run!"));
        }
        return environment;
    }

}
