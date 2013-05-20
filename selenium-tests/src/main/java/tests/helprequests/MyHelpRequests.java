package tests.helprequests;

import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.TestBase;

public class MyHelpRequests extends TestBase {
    public static void checkPage(WebDriver driver, final User user) {

        TestBase.clickOnMobileMenuLinkIfRequired(driver);

        if (driver instanceof ChromeDriver) {
            TestBase.sleepBecauseSeleniumSucks();
        }

        driver.findElements(By.cssSelector("#header-nav > ul > li")).get(1)
                .findElement(By.tagName("span"))
                .click();

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("a[href='/help-requests?username=" + user.getUsername() + "']")).isDisplayed();
            }
        });

        driver.findElement(By.cssSelector("a[href='/help-requests?username=" + user.getUsername() + "']"))
                .click();

        TestBase.sleep();

        String cssSelectorOfFirstDisplayedListItem = "li[data-id]";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedListItem = "tr[data-id]";
        }

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedListItem))
                .isDisplayed();
    }
}
