import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Join extends TestBase {
    public static void properFormFill(WebDriver driver, final User user) {
        driver.findElement(By.cssSelector(".action a[href='/join']"))
                .click();

        // Wait 5s for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("first-name")).isDisplayed();
            }
        });

        driver.findElement(By.id("first-name"))
                .sendKeys(user.getFirstName());

        driver.findElement(By.id("last-name"))
                .sendKeys(user.getLastName());

        driver.findElement(By.id("email"))
                .sendKeys(user.getEmail());

        driver.findElement(By.id("email-confirmation"))
                .sendKeys(user.getEmail());

        driver.findElement(By.id("username"))
                .sendKeys(user.getUsername());

        driver.findElement(By.id("password"))
                .sendKeys(user.getPassword());

        driver.findElement(By.id("city"))
                .sendKeys(user.getCity());

        Select droplist = new Select(driver.findElement(By.id("country")));
        droplist.selectByValue(user.getCountryId());

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait 5s for the page to load, and check that we are redirected to post-join login
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().equals(Runner.URL_ROOT + "login?from=join&username=" + user.getUsername()) &&
                        d.findElement(By.id("username-or-email"))
                                .getAttribute("value")
                                .equals(user.getUsername());
            }
        });
    }
}
