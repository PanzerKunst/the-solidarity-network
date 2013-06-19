package tests.helprequests;

import models.HelpRequest;
import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.TestBase;

public class DeleteHelpRequest extends TestBase {
    public static void tryDeletingRepliedRequest(WebDriver driver, final User user, final HelpRequest helpRequest) {
        clickOnMobileMenuLinkIfRequired(driver);

        driver.findElements(By.cssSelector("#header-nav > ul > li")).get(1)
                .findElement(By.tagName("span"))
                .click();

        // Wait for the page to load, and check that the "Inbox" link exists
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("a[href='/help-requests?username=" + user.getUsername() + "']")).isDisplayed();
            }
        });

        driver.findElement(By.cssSelector("a[href='/help-requests?username=" + user.getUsername() + "']"))
                .click();

        sleepBecauseSeleniumSucks();

        String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
        }

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem))
                .click();

        // Wait for the page to load, and check title
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.tagName("h1")).getAttribute("innerHTML").equals(helpRequest.getTitle()) &&
                        d.findElement(By.id("delete")).isDisplayed();
            }
        });

        driver.findElement(By.id("delete"))
                .click();

        // Wait for the page to load, and check popup
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("deletion-impossible-modal")).isDisplayed() &&
                        d.findElement(By.id("close-modal")).isDisplayed();
            }
        });

        driver.findElement(By.id("close-modal"))
                .click();
    }

    public static void deleteUnrepliedRequest(WebDriver driver, final User user, final HelpRequest helpRequest) {
        clickOnMobileMenuLinkIfRequired(driver);

        driver.findElements(By.cssSelector("#header-nav > ul > li")).get(1)
                .findElement(By.tagName("span"))
                .click();

        // Wait for the page to load, and check that the "Inbox" link exists
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("a[href='/help-requests?username=" + user.getUsername() + "']")).isDisplayed();
            }
        });

        driver.findElement(By.cssSelector("a[href='/help-requests?username=" + user.getUsername() + "']"))
                .click();

        sleepBecauseSeleniumSucks();

        String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
        }

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem))
                .click();

        // Wait for the page to load, and check title
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.tagName("h1")).getAttribute("innerHTML").equals(helpRequest.getTitle()) &&
                        d.findElement(By.id("delete")).isDisplayed();
            }
        });

        driver.findElement(By.id("delete"))
                .click();

        // Wait for the page to load, and check popup
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("confirm-delete-modal")).isDisplayed() &&
                        d.findElement(By.id("confirm-delete")).isDisplayed();
            }
        });

        driver.findElement(By.id("confirm-delete"))
                .click();

        // Wait for the page to load, and check that we are redirected to the dashboard
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("help-dashboard")).isDisplayed() &&
                        d.findElement(By.cssSelector(".indication")).getText().equals("Help request deleted");
            }
        });
    }
}
