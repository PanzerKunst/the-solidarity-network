import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SubmitHelpRequest {

    private static final String hrTitle = "HR title";
    private static final String hrDesc = "HR desc Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    public static void properFormFill(WebDriver driver) {
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".actions a[href='/help']")).isDisplayed();
            }
        });
        driver.findElement(By.cssSelector(".actions a[href='/help']"))
                .click();

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".actions a[href='/help-requests/new']")).isDisplayed();
            }
        });
        driver.findElement(By.cssSelector(".actions a[href='/help-requests/new']"))
                .click();

        // Wait 5s for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("title")).isDisplayed();
            }
        });

        driver.findElement(By.id("title"))
                .sendKeys(hrTitle);

        driver.findElement(By.id("description"))
                .sendKeys(hrDesc);

        Calendar inAWeek = new GregorianCalendar();
        inAWeek.add(Calendar.DATE, 7);

        driver.findElement(By.id("expiry-date"))
                .sendKeys(Runner.yyyymmdd.format(inAWeek.getTime()));

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait 10s for the page to load, and check that we are redirected to post-join login
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.tagName("body")).getAttribute("id").equals("view-help-request") &&
                        d.findElement(By.tagName("h1")).getAttribute("innerHTML").equals(hrTitle);
            }
        });
    }
}
