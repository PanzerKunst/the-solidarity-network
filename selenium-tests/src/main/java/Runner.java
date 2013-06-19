import com.opera.core.systems.OperaDriver;
import models.HelpRequest;
import models.Message;
import models.User;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import tests.helprequests.*;
import tests.messages.MsgInbox;
import tests.messages.SentMessages;
import tests.messages.ViewMessage;
import tests.messages.WriteMessage;
import tests.profile.EditProfile;
import tests.profile.MyProfile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Runner {
    public static final String URL_ROOT = "http://192.168.0.4:9000/";
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
        testInFirefox(new Dimension(350, 1024));
        testInChrome(new Dimension(350, 1024));
        testInIE(new Dimension(350, 1024));

        // TODO No test in Opera, because of:
        // java.lang.UnsupportedOperationException: Not supported in OperaDriver yet
    }

    private static void testInFirefox(Dimension resolution) throws IOException {
        File pathToBinary = new File("D:/ProgramFiles/Mozilla Firefox/firefox.exe");
        FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        WebDriver driver = new FirefoxDriver(ffBinary, firefoxProfile);

        runTests(driver, resolution);
    }

    private static void testInChrome(Dimension resolution) throws IOException {
        System.setProperty("webdriver.chrome.driver", "d:/ProgramFiles/Selenium/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        runTests(driver, resolution);
    }

    private static void testInIE(Dimension resolution) throws IOException {
        System.setProperty("webdriver.ie.driver", "d:/ProgramFiles/Selenium/IEDriverServer.exe");
        WebDriver driver = new InternetExplorerDriver();

        runTests(driver, resolution);
    }

    private static void testInOpera() throws IOException {
        DesiredCapabilities capabilities = DesiredCapabilities.opera();
        capabilities.setCapability("opera.binary", "D:/ProgramFiles/Opera/opera.exe");

        WebDriver driver = new OperaDriver(capabilities);

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
                "cbramditgmail.com",
                "blackbird",
                "tigrou",
                "Barcelona",
                "2");

        /**
         * Join
         */
        Join.incorrectEmail(driver, chris);
        chris.setEmail("cbramdit@gmail.com");
        Join.properFormFill(driver, chris);

        /**
         * Login
         */
        Login.incorrectPassword(driver);
        Login.properFormFillUsername(driver, chris);
        Login.logout(driver);
        Login.properFormFillEmail(driver, chris);

        Calendar yesterday = new GregorianCalendar();
        yesterday.add(Calendar.DATE, -1);

        HelpRequest helpRequest = new HelpRequest("HR title",
                "HR desc Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\nUt enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\nExcepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                Runner.yyyymmdd.format(yesterday.getTime()));

        /**
         * SubmitHelpRequest
         */
        SubmitHelpRequest.expiryDateMissing(driver, helpRequest);
        SubmitHelpRequest.expiryDatePast(driver, helpRequest);

        // In 21 days
        yesterday.add(Calendar.DATE, 22);
        helpRequest.setExpiryDate(Runner.yyyymmdd.format(yesterday.getTime()));

        SubmitHelpRequest.expiryDateTooFar(driver, helpRequest);

        // In 7 days
        yesterday.add(Calendar.DATE, -14);
        helpRequest.setExpiryDate(Runner.yyyymmdd.format(yesterday.getTime()));

        SubmitHelpRequest.properFormFill(driver, helpRequest);

        /**
         * DeleteHelpRequest
         */
        DeleteHelpRequest.deleteUnrepliedRequest(driver, chris, helpRequest);

        // We recreate it
        SubmitHelpRequest.properFormFill(driver, helpRequest);

        /**
        * MyHelpRequests
        */
        MyHelpRequests.checkPage(driver, chris);

        /**
         * Logout
         */
        Login.logout(driver);

        User damien = new User("Damien",
                "Bram",
                "jashugan@gmx.de",
                "db",
                "tigrou",
                "Barcelona",
                "2");

        Join.properFormFill(driver, damien);
        Login.properFormFillUsername(driver, damien);

        /**
         * HelpDashboard
         */
        HelpDashboard.helpRequestsNearMe(driver, damien);

        /**
         * SearchHelpRequests
         */
        SearchHelpRequests.noResult(driver);
        SearchHelpRequests.generic(driver, helpRequest);
        SearchHelpRequests.noResult(driver);
        SearchHelpRequests.advancedCity(driver, chris);
        SearchHelpRequests.noResult(driver);
        SearchHelpRequests.advancedTitle(driver, helpRequest);
        SearchHelpRequests.noResult(driver);
        SearchHelpRequests.advancedDesc(driver, helpRequest);
        SearchHelpRequests.noResult(driver);
        SearchHelpRequests.advancedFirstName(driver, chris);
        SearchHelpRequests.noResult(driver);
        SearchHelpRequests.advancedUsername(driver, chris);
        SearchHelpRequests.noResult(driver);
        SearchHelpRequests.emptyQueryAndClickOnFirstResult(driver);

        /**
         * ReplyToHelpRequest
         */
        ReplyToHelpRequest.properFormFill(driver);

        /**
         * HelpRequestsIRepliedTo
         */
        HelpRequestsIRepliedTo.checkPage(driver, damien);

        /**
         * WriteReference
         */
        WriteReference.properFormFill(driver, chris, damien);

        /**
         * Write message from profile page
         */
        Message msg = new Message("Msg title",
                "HR desc Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\nUt enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\nExcepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                damien,
                chris);

        WriteMessage.writeMessageFromProfilePage(driver, msg);
        int unreadMessagesCountForChris = 1;

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
                "panzrkunst@yahoo.fr",
                "db",
                "tigrou",
                "Barcelona",
                "2");
        updatedDamien.setStreetAddress("Heleneborgsgatan 6C");
        updatedDamien.setPostCode("11732");
        updatedDamien.setDescription("Bref, un mec normal");
        updatedDamien.setSubscriptionToNewHelpRequests(User.NEW_HR_SUBSCRIPTION_FREQUENCY_NONE);
        updatedDamien.setSubscribedToNews(false);

        EditProfile.properProfileTabEdit(driver, updatedDamien);
        EditProfile.properAccountTabEdit(driver, updatedDamien);

        updatedDamien.setSubscriptionToNewHelpRequests(User.NEW_HR_SUBSCRIPTION_FREQUENCY_DAILY);
        EditProfile.properAccountTabEdit(driver, updatedDamien);

        /**
         * Messages
         */
        MsgInbox.checkNoMessage(driver);

        WriteMessage.missingRecipient(driver, msg);
        WriteMessage.missingMessage(driver, msg);
        WriteMessage.properFormFill(driver, msg);
        unreadMessagesCountForChris++;

        SentMessages.checkMessage(driver, msg);

        Login.logout(driver);

        Login.properFormFillUsername(driver, chris);

        /**
         * DeleteHelpRequest
         */
        DeleteHelpRequest.tryDeletingRepliedRequest(driver, chris, helpRequest);

        /**
         * More messages tests
         */
        MsgInbox.checkUnreadMessages(driver, unreadMessagesCountForChris);

        SentMessages.checkNoMessage(driver);

        // We need to update the sender of the message otherwise the test below will fail
        msg.setFrom(updatedDamien);
        MsgInbox.checkMessageInList(driver, msg);

        ViewMessage.check(driver, msg);
        unreadMessagesCountForChris--;

        ViewMessage.submitEmptyReply(driver);

        Message reply = new Message("Re: " + msg.getTitle(),
                "Reply text",
                chris,
                damien);

        ViewMessage.writeReply(driver, reply);
        int unreadMessagesCountForDamien = 1;

        MsgInbox.checkMessageInList(driver, msg);   // Just to reload the page, so that unreadMessagesCount is updated
        MsgInbox.checkUnreadMessages(driver, unreadMessagesCountForChris);

        // Logging back to "damien" to check the presence of the message reply
        Login.logout(driver);
        Login.properFormFillUsername(driver, damien);

        MsgInbox.checkUnreadMessages(driver, unreadMessagesCountForDamien);
        MsgInbox.checkMessageInList(driver, reply);

        ViewMessage.checkReply(driver, msg, reply);

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
