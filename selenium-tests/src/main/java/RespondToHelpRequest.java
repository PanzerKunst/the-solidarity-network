import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RespondToHelpRequest extends TestBase {

    private static final String hrText = "I can help, send me a message!";

    public static void properFormFill(WebDriver driver) {
        sleepBecauseSeleniumSucks(2000);

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("respond")).isDisplayed();
            }
        });
        driver.findElement(By.id("respond"))
                .click();

        driver.findElement(By.id("response-text"))
                .sendKeys(hrText);

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("post-response-button")).isDisplayed();
            }
        });

        driver.findElement(By.id("post-response-button"))
                .click();

        sleepBecauseSeleniumSucks();

        // Wait for page to load, then check if response is in the body
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("#responses > article > p")).getAttribute("innerHTML").equals(hrText);
            }
        });
    }
}
