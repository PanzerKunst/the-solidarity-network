import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Join {
    public static void properFormFill(WebDriver driver) {
        driver.get("http://localhost:9000/join");

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

        // Wait for the page to load, timeout after 5 seconds
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().startsWith("http://localhost:9000/login?from=join&username=");
            }
        });

        //Close the browser
        driver.quit();
    }
}
