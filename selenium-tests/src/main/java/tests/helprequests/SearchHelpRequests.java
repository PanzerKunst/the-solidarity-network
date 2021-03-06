package tests.helprequests;

import models.HelpRequest;
import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.TestBase;

public class SearchHelpRequests extends TestBase {
    public static void noResult(WebDriver driver) {
        boolean isAlreadyAtThatPage = driver.findElement(By.tagName("body")).getAttribute("id").equals("search-help-requests");

        if (!isAlreadyAtThatPage) {
            (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.findElement(By.cssSelector(".action > a[href='/help-requests']")).isDisplayed();
                }
            });

            driver.findElement(By.cssSelector(".action > a[href='/help-requests']"))
                    .click();

            if (driver instanceof ChromeDriver) {
                TestBase.sleepBecauseSeleniumSucks();
            }
        }

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("query")).isDisplayed();
            }
        });

        driver.findElement(By.id("query"))
                .clear();

        driver.findElement(By.id("query"))
                .sendKeys("this won't be in any description!!!");

        driver.findElement(By.id("search"))
                .click();

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("search-returned-nothing")).isDisplayed();
            }
        });
    }

    public static void generic(WebDriver driver, HelpRequest helpRequest) {
        driver.findElement(By.id("query"))
                .clear();

        driver.findElement(By.id("query"))
                .sendKeys(helpRequest.getDescription().substring(6, 19));

        driver.findElement(By.id("search"))
                .click();

        TestBase.sleepBecauseSeleniumSucks();

        String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
        }

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem))
                .isDisplayed();
    }

    public static void advancedCity(WebDriver driver, User user) {
        driver.findElement(By.id("query"))
                .clear();

        driver.findElement(By.id("query"))
                .sendKeys("city=" + user.getCity());

        driver.findElement(By.id("search"))
                .click();

        TestBase.sleepBecauseSeleniumSucks();

        String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
        }

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem))
                .isDisplayed();
    }

    public static void advancedTitle(WebDriver driver, HelpRequest helpRequest) {
        driver.findElement(By.id("query"))
                .clear();

        driver.findElement(By.id("query"))
                .sendKeys("title=\"*" + helpRequest.getTitle().substring(1, 3) + "*\"");

        driver.findElement(By.id("search"))
                .click();

        TestBase.sleepBecauseSeleniumSucks();

        String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
        }

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem))
                .isDisplayed();
    }

    public static void advancedDesc(WebDriver driver, HelpRequest helpRequest) {
        driver.findElement(By.id("query"))
                .clear();

        driver.findElement(By.id("query"))
                .sendKeys("desc=\"*" + helpRequest.getDescription().substring(5, 15) + "*\"");

        driver.findElement(By.id("search"))
                .click();

        TestBase.sleepBecauseSeleniumSucks();

        String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
        }

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem))
                .isDisplayed();
    }

    public static void advancedFirstName(WebDriver driver, User user) {
        driver.findElement(By.id("query"))
                .clear();

        driver.findElement(By.id("query"))
                .sendKeys("firstName=\"" + user.getFirstName().substring(0, 2) + "*\"");

        driver.findElement(By.id("search"))
                .click();

        TestBase.sleepBecauseSeleniumSucks();

        String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
        }

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem))
                .isDisplayed();
    }

    public static void advancedUsername(WebDriver driver, User user) {
        driver.findElement(By.id("query"))
                .clear();

        driver.findElement(By.id("query"))
                .sendKeys("username=" + user.getUsername());

        driver.findElement(By.id("search"))
                .click();

        TestBase.sleepBecauseSeleniumSucks();

        String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
        }

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem))
                .isDisplayed();
    }

    public static void emptyQueryAndClickOnFirstResult(WebDriver driver) {
        driver.findElement(By.id("query"))
                .clear();

        driver.findElement(By.id("search"))
                .click();

        TestBase.sleepBecauseSeleniumSucks();

        String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
        }

        TestBase.sleepBecauseSeleniumSucks(2000);

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem))
                .click();
    }
}
