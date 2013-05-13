package tests.profile;

import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.TestBase;

public class MyProfile extends TestBase {

    public static void checkPage(WebDriver driver, final User user) {

        TestBase.clickOnMobileMenuLinkIfRequired(driver);

        if (driver instanceof ChromeDriver) {
            TestBase.sleepBecauseSeleniumSucks();
        }

        driver.findElement(By.cssSelector("a[href='/home']"))
                .click();

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".action a[href='/my-profile']")).isDisplayed();
            }
        });

        driver.findElement(By.cssSelector(".action a[href='/my-profile']"))
                .click();

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement h1 = d.findElement(By.tagName("h1"));
                WebElement textualInfo = d.findElement(By.cssSelector("#textual-info"));

                return h1.getAttribute("innerHTML").contains(user.getFirstName()) &&
                        h1.getAttribute("innerHTML").contains(user.getLastName()) &&
                        h1.getAttribute("innerHTML").contains(user.getUsername()) &&
                        textualInfo.getAttribute("innerHTML").contains(user.getCity());
            }
        });
    }
}
