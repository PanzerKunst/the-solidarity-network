package tests.messages;

import models.Message;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.TestBase;

public class SentMessages extends TestBase {
    public static void checkNoMessage(WebDriver driver) {
        clickOnMobileMenuLinkIfRequired(driver);

        driver.findElements(By.cssSelector("#header-nav > ul > li")).get(3)
                .findElement(By.tagName("span"))
                .click();

        // Wait for the page to load, and check that the "Inbox" link exists
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("a[href='/messages/sent']")).isDisplayed();
            }
        });

        driver.findElement(By.cssSelector("a[href='/messages/sent']"))
                .click();

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement noMessageParagraph = d.findElement(By.cssSelector("#content > p"));

                return noMessageParagraph.isDisplayed() &&
                        noMessageParagraph.getText().equals("Nothing here yet.");
            }
        });
    }

    public static void checkMessage(WebDriver driver, final Message msg) {
        clickOnMobileMenuLinkIfRequired(driver);

        driver.findElements(By.cssSelector("#header-nav > ul > li")).get(3)
                .findElement(By.tagName("span"))
                .click();

        // Wait for the page to load, and check that the "Inbox" link exists
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("a[href='/messages/sent']")).isDisplayed();
            }
        });

        driver.findElement(By.cssSelector("a[href='/messages/sent']"))
                .click();

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

                if (d.findElement(By.id("desktop-list")).isDisplayed()) {
                    cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
                }

                return d.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem)).isDisplayed() &&
                        d.findElement(By.cssSelector("#list-container .username")).getAttribute("innerHTML").equals(msg.getTo().getUsername()) &&
                        d.findElement(By.cssSelector("#list-container .first-name")).getAttribute("innerHTML").equals(msg.getTo().getFirstName()) &&
                        d.findElement(By.cssSelector("#list-container .last-name")).getAttribute("innerHTML").equals(msg.getTo().getLastName()) &&
                        d.findElement(By.cssSelector("#list-container .user-city")).getAttribute("innerHTML").equals(msg.getTo().getCity()) &&
                        isDisplayedTitleCorrect(d.findElement(By.cssSelector(".selenium-property-displayed-title")), msg);

            }
        });
    }
}
