package tests.helprequests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.TestBase;

public class ReplyToHelpRequest extends TestBase {

    private static final String hrText = "I can help, send me a message!";

    public static void properFormFill(WebDriver driver) {
        TestBase.sleepBecauseSeleniumSucks(2000);

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("reply")).isDisplayed();
            }
        });
        driver.findElement(By.id("reply"))
                .click();

        driver.findElement(By.id("reply-text"))
                .sendKeys(hrText);

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("post-reply")).isDisplayed();
            }
        });

        driver.findElement(By.id("post-reply"))
                .click();

        TestBase.sleepBecauseSeleniumSucks();

        // Wait for page to load, then check if response is in the body
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("#replies > article > p")).getText().equals(hrText);
            }
        });
    }
}
