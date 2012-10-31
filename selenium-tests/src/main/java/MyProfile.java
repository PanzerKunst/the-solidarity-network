import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyProfile extends TestBase {

    public static void checkPage(WebDriver driver, final User user) {

        clickOnMobileMenuLinkIfRequired(driver);

        if (driver instanceof ChromeDriver) {
            sleepBecauseSeleniumSucks();
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
                WebElement textualInfo = d.findElement(By.cssSelector("#textual-info"));

                return textualInfo.getAttribute("innerHTML").indexOf(user.getFirstName()) > 0 &&
                        textualInfo.getAttribute("innerHTML").indexOf(user.getLastName()) > 0 &&
                        textualInfo.getAttribute("innerHTML").indexOf(user.getUsername()) > 0 &&
                        textualInfo.getAttribute("innerHTML").indexOf(user.getCity()) > 0;
            }
        });
    }
}
