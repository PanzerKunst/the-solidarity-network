package tests.helprequests;

import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.TestBase;

public class HelpDashboard extends TestBase {
    public static void helpRequestsNearMe(WebDriver driver, final User user) {
        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".action > a[href='/help']")).isDisplayed();
            }
        });

        driver.findElement(By.cssSelector(".action > a[href='/help']"))
                .click();

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement searchQueryField = d.findElement(By.id("query"));
                return searchQueryField.isDisplayed() &&
                        searchQueryField.getAttribute("value").endsWith(" city=\"" + user.getCity() + "\"");
            }
        });

        TestBase.sleep();

        String cssSelectorOfFirstDisplayedSearchResult = "li[data-id='1']";

        if (driver.findElement(By.id("desktop-list")).isDisplayed()) {
            cssSelectorOfFirstDisplayedSearchResult = "tr[data-id='1']";
        }

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedSearchResult))
                .isDisplayed();
    }
}
