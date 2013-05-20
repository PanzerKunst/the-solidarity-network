package tests.messages;

import models.Message;
import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.TestBase;

public class ViewMessage extends TestBase {
    public static void check(WebDriver driver, final Message msg) {
        String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
        }

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem))
                .click();

        // Wait for the page to load, and check title
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                User author = msg.getFrom();
                WebElement authorLink = d.findElement(By.cssSelector("a[href='/users/" + author.getUsername() + "']"));

                return d.findElement(By.tagName("h1")).getAttribute("innerHTML").equals(msg.getTitle()) &&
                        authorLink.getText().equals(author.getFirstName() + " " + author.getLastName() + " <" + author.getUsername() + ">") &&
                        d.findElement(By.cssSelector(".msg-text")).getText().equals(msg.getText());
            }
        });
    }

    public static void submitEmptyReply(WebDriver driver) {
        driver.findElement(By.tagName("form"))
                .submit();

        // Wait for the page to load, and check error
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("error-msg-empty-text")).isDisplayed();
            }
        });
    }

    public static void writeReply(WebDriver driver, Message msg) {
        driver.findElement(By.id("text"))
                .sendKeys(msg.getText());

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait for the page to load, and check for the confirmation message
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.tagName("body")).getAttribute("id").equals("msg-inbox") &&
                        d.findElement(By.cssSelector(".indication")).getText().contains("Reply sent");
            }
        });
    }

    public static void checkReply(WebDriver driver, final Message initialMsg, final Message reply) {
        String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
        }

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem))
                .click();

        // Wait for the page to load, and check initial message and reply
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                // Initial message
                User initialAuthor = initialMsg.getFrom();
                WebElement initialAuthorLink = d.findElement(By.cssSelector("a[href='/users/" + initialAuthor.getUsername() + "']"));

                boolean isInitialMsgDataCorrect = d.findElement(By.tagName("h1")).getAttribute("innerHTML").equals(initialMsg.getTitle()) &&
                        initialAuthorLink.getText().equals(initialAuthor.getFirstName() + " " + initialAuthor.getLastName() + " <" + initialAuthor.getUsername() + ">") &&
                        d.findElement(By.cssSelector(".msg-text")).getText().equals(initialMsg.getText());

                // Reply
                User replyAuthor = reply.getFrom();
                WebElement replyAuthorLink = d.findElement(By.cssSelector("article[data-reply-id] a[href='/users/" + replyAuthor.getUsername() + "']"));

                boolean isReplyDataCorrect = replyAuthorLink.getText().equals(replyAuthor.getFirstName() + " " + replyAuthor.getLastName() + " <" + replyAuthor.getUsername() + ">") &&
                        d.findElement(By.cssSelector("article[data-reply-id] .msg-text")).getText().equals(reply.getText());

                return isInitialMsgDataCorrect && isReplyDataCorrect;
            }
        });

    }
}
