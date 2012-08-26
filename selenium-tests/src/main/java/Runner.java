import com.opera.core.systems.OperaDriver;
import models.User;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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
        testInFirefox();
        testInChrome();
        testInIE();
        testInOpera();
    }

    private static void testInFirefox() throws IOException {
        WebDriver driver = new FirefoxDriver();

        runTests(driver);
    }

    private static void testInChrome() throws IOException {
        System.setProperty("webdriver.chrome.driver", "e:/servers/selenium-drivers/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        runTests(driver);
    }

    private static void testInIE() throws IOException {
        System.setProperty("webdriver.ie.driver", "e:/servers/selenium-drivers/IEDriverServer.exe");
        WebDriver driver = new InternetExplorerDriver();

        runTests(driver);
    }

    private static void testInOpera() throws IOException {
        WebDriver driver = new OperaDriver();

        runTests(driver);
    }

    private static void runTests(WebDriver driver) throws IOException {
        dropTables();
        createTables();

        driver.get(Runner.URL_ROOT);

        User christophe = new User("Christophe",
                "Bram",
                "cbramdit@gmail.com",
                "blackbird",
                "tigrou",
                "Stockholm");

        Join.properFormFill(driver, christophe);

        Login.incorrectPassword(driver);
        Login.properFormFillUsername(driver, christophe);
        Login.logout(driver);
        Login.properFormFillEmail(driver, christophe);

        SubmitHelpRequest.properFormFill(driver);

        Login.logout(driver);

        User damien = new User("Damien",
                "Bram",
                "panzrkunst@yahoo.fr",
                "db",
                "tigrou",
                "Lille");

        Join.properFormFill(driver, damien);
        Login.properFormFillUsername(driver, damien);

        SearchHelpRequests.clickOnFirstSearchResult(driver);

        RespondToHelpRequest.properFormFill(driver);

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
