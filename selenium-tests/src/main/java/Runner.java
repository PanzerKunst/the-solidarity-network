import com.opera.core.systems.OperaDriver;
import models.User;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Runner {
    public static final String URL_ROOT = "http://localhost:9000/";
    public static final DateFormat yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
    private static final DefaultHttpClient httpClient = new DefaultHttpClient();

    public static void main(String[] args) throws IOException {
        testOnDesktop();
        testOnMobile();
    }

    private static void testOnDesktop() throws IOException {
        testInFirefox(new Dimension(820, 1024));
        testInChrome(new Dimension(820, 1024));
        testInIE(new Dimension(820, 1024));
        testInOpera();
    }

    private static void testOnMobile() throws IOException {
        testInFirefox(new Dimension(320, 1024));
        testInChrome(new Dimension(320, 1024));
        testInIE(new Dimension(320, 1024));

        // TODO No test in Opera, because of:
        // java.lang.UnsupportedOperationException: Not supported in OperaDriver yet
    }

    private static void testInFirefox(Dimension resolution) throws IOException {
        WebDriver driver = new FirefoxDriver();

        runTests(driver, resolution);
    }

    private static void testInChrome(Dimension resolution) throws IOException {
        System.setProperty("webdriver.chrome.driver", "e:/servers/selenium-drivers/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        runTests(driver, resolution);
    }

    private static void testInIE(Dimension resolution) throws IOException {
        System.setProperty("webdriver.ie.driver", "e:/servers/selenium-drivers/IEDriverServer.exe");
        WebDriver driver = new InternetExplorerDriver();

        runTests(driver, resolution);
    }

    private static void testInOpera() throws IOException {
        WebDriver driver = new OperaDriver();

        runTests(driver, null);
    }

    private static void runTests(WebDriver driver, Dimension resolution) throws IOException {
        dropTables();
        createTables();

        if (resolution != null) {
            driver.manage().window().setPosition(new Point(0, 0));
            driver.manage().window().setSize(resolution);
        }

        driver.get(Runner.URL_ROOT);

        User chris = new User("Christophe",
                "Bram",
                "cbramdit@gmail.com",
                "blackbird",
                "tigrou",
                "Stockholm",
                "1");

        /**
         * Join
         */
        Join.properFormFill(driver, chris);

        /**
         * Login
         */
        Login.incorrectPassword(driver);
        Login.properFormFillUsername(driver, chris);
        Login.logout(driver);
        Login.properFormFillEmail(driver, chris);

        SubmitHelpRequest.properFormFill(driver);

        Login.logout(driver);

        User damien = new User("Damien",
                "Bram",
                "panzrkunst@yahoo.fr",
                "db",
                "tigrou",
                "Lille",
                "2");

        Join.properFormFill(driver, damien);
        Login.properFormFillUsername(driver, damien);

        /**
         * SearchHelpRequests
         */
        SearchHelpRequests.clickOnFirstSearchResult(driver);

        /**
         * RespondToHelpRequest
         */
        RespondToHelpRequest.properFormFill(driver);

        /**
         * WriteReference
         */
        WriteReference.properFormFill(driver, chris, damien);

        /**
         * MyProfile
         */
        MyProfile.checkPage(driver, damien);

        /**
         * EditProfile
         */
        EditProfile.checkProfileTab(driver, damien);
        EditProfile.checkAccountTab(driver, damien);

        User updatedDamien = new User("Dämish",
                "Bram dit Saint-Amand",
                "jahugan@gmx.de",
                "damish",
                "tigrou",
                "Strasbourg",
                "1");
        updatedDamien.setStreetAddress("Heleneborgsgatan 6C");
        updatedDamien.setPostCode("11732");
        updatedDamien.setDescription("Bref, un mec normal");

        EditProfile.properFormFill(driver, updatedDamien);

        /**
         * Logout
         */
        Login.logout(driver);

        // Close the browser
        driver.quit();
    }

    private static void createTables() throws IOException {
        HttpPost httpPost = new HttpPost(Runner.URL_ROOT + "api/db/admin?key=OZs:]T@R]u9I4nyqbvNyAMe4hPZaoFsNhiQvGjCh@GErJ7/0wqaVdj8To8MpmE1O");

        try {
            httpClient.execute(httpPost);
        } finally {
            httpPost.releaseConnection();
        }
    }

    private static void dropTables() throws IOException {
        HttpDelete httpDelete = new HttpDelete(Runner.URL_ROOT + "api/db/admin?key=OZs:]T@R]u9I4nyqbvNyAMe4hPZaoFsNhiQvGjCh@GErJ7/0wqaVdj8To8MpmE1O");

        try {
            httpClient.execute(httpDelete);
        } finally {
            httpDelete.releaseConnection();
        }
    }
}
