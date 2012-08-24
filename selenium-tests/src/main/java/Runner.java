import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Runner {
    private static DefaultHttpClient httpClient = new DefaultHttpClient();

    public static void main(String[] args) throws IOException {
        testInFirefox();
        testInChrome();
    }

    private static void testInFirefox() throws IOException {
        dropTables();
        createTables();

        WebDriver firefoxDriver = new FirefoxDriver();
        Join.properFormFill(firefoxDriver);
    }

    private static void testInChrome() throws IOException {
        dropTables();
        createTables();

        System.setProperty("webdriver.chrome.driver", "e:/servers/chromedriver.exe");
        WebDriver chromeDriver = new ChromeDriver();

        Join.properFormFill(chromeDriver);
    }

    private static void createTables() throws IOException {
        HttpPost httpPost = new HttpPost("http://localhost:9000/api/db/admin?key=OZs:]T@R]u9I4nyqbvNyAMe4hPZaoFsNhiQvGjCh@GErJ7/0wqaVdj8To8MpmE1O");

        try {
            httpClient.execute(httpPost);
        } finally {
            httpPost.releaseConnection();
        }
    }

    private static void dropTables() throws IOException {
        HttpDelete httpDelete = new HttpDelete("http://localhost:9000/api/db/admin?key=OZs:]T@R]u9I4nyqbvNyAMe4hPZaoFsNhiQvGjCh@GErJ7/0wqaVdj8To8MpmE1O");

        try {
            httpClient.execute(httpDelete);
        } finally {
            httpDelete.releaseConnection();
        }
    }
}
