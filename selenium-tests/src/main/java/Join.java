import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Join {
    public static void properFormFill(WebDriver driver) {
        driver.findElement(By.cssSelector("a[href='/join']"))
                .click();

        // Wait 5s for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("first-name")).isDisplayed();
            }
        });

        driver.findElement(By.id("first-name"))
                .sendKeys("Christophe");

        driver.findElement(By.id("last-name"))
                .sendKeys("Bram");

        driver.findElement(By.id("email"))
                .sendKeys("cbramdit@gmail.com");

        driver.findElement(By.id("email-confirmation"))
                .sendKeys("cbramdit@gmail.com");

        driver.findElement(By.id("username"))
                .sendKeys("blackbird");

        driver.findElement(By.id("password"))
                .sendKeys("tigrou");

        driver.findElement(By.id("city"))
                .sendKeys("Stockholm");

        Select droplist = new Select(driver.findElement(By.id("country")));
        droplist.selectByIndex(1);

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait 5s for the page to load, and check that we are redirected to post-join login
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().startsWith(Runner.URL_ROOT + "login?from=join&username=") &&
                        d.findElement(By.id("username-or-email"))
                                .getAttribute("value")
                                .equals("blackbird");
            }
        });
    }
}
