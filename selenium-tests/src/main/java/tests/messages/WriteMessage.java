package tests.messages;

import models.Message;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.TestBase;

public class WriteMessage extends TestBase {
    public static void missingRecipient(WebDriver driver, Message msg) {
        clickOnMobileMenuLinkIfRequired(driver);

        driver.findElements(By.cssSelector("#header-nav > ul > li")).get(3)
                .findElement(By.tagName("span"))
                .click();

        // Wait for the page to load, and check that the "Inbox" link exists
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("a[href='/messages/new']")).isDisplayed();
            }
        });

        driver.findElement(By.cssSelector("a[href='/messages/new']"))
                .click();

        // Wait for the page to load, and check that the "title" field exists
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("title")).isDisplayed();
            }
        });

        driver.findElement(By.id("title"))
                .sendKeys(msg.getTitle());

        driver.findElement(By.id("text"))
                .sendKeys(msg.getText());

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait for the page to load, and check for the error message
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("error-msg-empty-recipient")).isDisplayed();
            }
        });
    }

    public static void missingMessage(WebDriver driver, final Message msg) {
        driver.findElement(By.cssSelector(".select2-choice")).click();

        // Wait for the Select2 search field to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".select2-input")).isDisplayed();
            }
        });

        driver.findElement(By.cssSelector(".select2-input"))
                .sendKeys("bla");

        // Wait for the Select2 search results to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".select2-result-label")).isDisplayed();
            }
        });

        driver.findElement(By.cssSelector(".select2-result-label"))
                .click();

        // Wait for the Select2 element to be updated with the selection
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".select2-choice > span")).getAttribute("innerHTML")
                        .equals(msg.getTo().getFirstName() + " " + msg.getTo().getLastName() + " &lt;" + msg.getTo().getUsername() + "&gt;") &&
                        !d.findElement(By.id("error-msg-empty-recipient")).isDisplayed();
            }
        });

        driver.findElement(By.id("text"))
                .clear();

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait for the page to load, and check for the error message
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("error-msg-empty-text")).isDisplayed();
            }
        });
    }

    public static void properFormFill(WebDriver driver, Message msg) {
        driver.findElement(By.id("text"))
                .sendKeys(msg.getText());

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait for the page to load, and check for the confirmation message
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.tagName("body")).getAttribute("id").equals("msg-inbox") &&
                        d.findElement(By.cssSelector(".indication")).getText().contains("Message sent");
            }
        });
    }

    public static void writeMessageFromProfilePage(WebDriver driver, final Message msg) {
        // Wait for the page to load, and check that the "Write a message" button exists
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("a[href='/messages/new?recipient=" + msg.getTo().getUsername() + "']")).isDisplayed();
            }
        });

        driver.findElement(By.cssSelector("a[href='/messages/new?recipient=" + msg.getTo().getUsername() + "']"))
                .click();

        // Wait for the page to load, and check that the "title" field exists
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("title")).isDisplayed();
            }
        });

        driver.findElement(By.id("title"))
                .sendKeys(msg.getTitle());

        driver.findElement(By.id("text"))
                .sendKeys(msg.getText());

        driver.findElement(By.tagName("form"))
                .submit();

        sleepBecauseSeleniumSucks();

        // Wait for the page to load, and check for the confirmation message
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.tagName("body")).getAttribute("id").equals("msg-inbox") &&
                        d.findElement(By.cssSelector(".indication")).getText().contains("Message sent");
            }
        });
    }
}
