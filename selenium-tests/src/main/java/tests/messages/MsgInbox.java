package tests.messages;

import models.Message;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.TestBase;

public class MsgInbox extends TestBase {
    public static void checkNoMessage(WebDriver driver) {
        clickOnMobileMenuLinkIfRequired(driver);

        driver.findElements(By.cssSelector("#header-nav > ul > li")).get(3)
                .findElement(By.tagName("span"))
                .click();

        // Wait for the page to load, and check that the "Inbox" link exists
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("a[href='/messages']")).isDisplayed();
            }
        });

        driver.findElement(By.cssSelector("a[href='/messages']"))
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
        boolean isAlreadyAtThatPage = driver.findElement(By.tagName("body")).getAttribute("id").equals("msg-inbox");

        if (!isAlreadyAtThatPage) {
            clickOnMobileMenuLinkIfRequired(driver);

            driver.findElements(By.cssSelector("#header-nav > ul > li")).get(3)
                    .findElement(By.tagName("span"))
                    .click();

            // Wait for the page to load, and check that the "Inbox" link exists
            (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.findElement(By.cssSelector("a[href='/messages']")).isDisplayed();
                }
            });

            driver.findElement(By.cssSelector("a[href='/messages']"))
                    .click();
        }

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                String cssSelectorOfFirstDisplayedSearchResult = "li[data-id='1']";

                if (d.findElement(By.id("desktop-list")).isDisplayed()) {
                    cssSelectorOfFirstDisplayedSearchResult = "tr[data-id='1']";
                }

                return d.findElement(By.cssSelector(cssSelectorOfFirstDisplayedSearchResult)).isDisplayed() &&
                        d.findElement(By.cssSelector("#list-container .username")).getAttribute("innerHTML").equals(msg.getFrom().getUsername()) &&
                        d.findElement(By.cssSelector("#list-container .first-name")).getAttribute("innerHTML").equals(msg.getFrom().getFirstName()) &&
                        d.findElement(By.cssSelector("#list-container .last-name")).getAttribute("innerHTML").equals(msg.getFrom().getLastName()) &&
                        d.findElement(By.cssSelector("#list-container .user-city")).getAttribute("innerHTML").equals(msg.getFrom().getCity()) &&
                        isDisplayedTitleCorrect(d.findElement(By.cssSelector(".selenium-property-displayed-title")), msg);

            }
        });
    }
}
