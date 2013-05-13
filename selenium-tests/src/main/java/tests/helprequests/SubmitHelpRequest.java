package tests.helprequests;

import models.HelpRequest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.TestBase;

public class SubmitHelpRequest extends TestBase {
    public static void expiryDateMissing(WebDriver driver, HelpRequest helpRequest) {
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".action a[href='/help']")).isDisplayed();
            }
        });

        if (driver instanceof ChromeDriver) {
            TestBase.sleepBecauseSeleniumSucks();
        }

        driver.findElement(By.cssSelector(".action a[href='/help']"))
                .click();

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".action a[href='/help-requests/new']")).isDisplayed();
            }
        });
        driver.findElement(By.cssSelector(".action a[href='/help-requests/new']"))
                .click();

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("title")).isDisplayed();
            }
        });

        driver.findElement(By.id("title"))
                .sendKeys(helpRequest.getTitle());

        driver.findElement(By.id("description"))
                .sendKeys(helpRequest.getDescription());

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait for the page to load, and check that the error message is displayed
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("error-msg-expiry-date-empty"))
                        .isDisplayed();
            }
        });
    }

    public static void expiryDatePast(WebDriver driver, HelpRequest helpRequest) {
        driver.findElement(By.id("expiry-date"))
                .sendKeys(helpRequest.getExpiryDate());

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait for the page to load, and check that the error message is displayed
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("error-msg-expiry-date-in-future"))
                        .isDisplayed();
            }
        });
    }

    public static void expiryDateTooFar(WebDriver driver, HelpRequest helpRequest) {
        driver.findElement(By.id("expiry-date"))
                .clear();

        driver.findElement(By.id("expiry-date"))
                .sendKeys(helpRequest.getExpiryDate());

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait for the page to load, and check that the error message is displayed
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("error-msg-expiry-date-in-max-2-weeks"))
                        .isDisplayed();
            }
        });
    }

    public static void properFormFill(WebDriver driver, final HelpRequest helpRequest) {
        driver.findElement(By.id("expiry-date"))
                .clear();

        driver.findElement(By.id("expiry-date"))
                .sendKeys(helpRequest.getExpiryDate());

        driver.findElement(By.tagName("form"))
                .submit();

        sleepBecauseSeleniumSucks();

        // Wait for the page to load, and check that we are redirected to post-join login
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.tagName("body")).getAttribute("id").equals("view-help-request") &&
                        d.findElement(By.tagName("h1")).getAttribute("innerHTML").equals(helpRequest.getTitle());
            }
        });
    }
}
