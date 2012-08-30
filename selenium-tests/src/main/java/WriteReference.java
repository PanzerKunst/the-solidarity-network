import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WriteReference {

    private static final String refText = "Good lad! was fun to help!";

    public static void properFormFill(WebDriver driver, User to, final User from) {
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("write-reference")).isDisplayed();
            }
        });
        driver.findElement(By.id("write-reference"))
                .click();

        driver.findElement(By.cssSelector("#helped > a"))
                .click();

        driver.findElement(By.cssSelector("#grade a"))
                .click();

        driver.findElement(By.id("reference-text"))
                .sendKeys(refText);

        driver.findElement(By.id("post-reference-button"))
                .click();

        // Wait 5s for page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().startsWith(Runner.URL_ROOT + "help-requests/") /* TODO: fails on IE &&
                        !d.findElement(By.id("reference-form")).isDisplayed()*/;
            }
        });

        driver.get(Runner.URL_ROOT + "users/" + to.getUsername());

        // Wait 10s for page to load, then check that the reference is there
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {

                return d.findElement(By.cssSelector("#references p"))
                        .getAttribute("innerHTML")
                        .equals(refText) &&
                        d.findElement(By.cssSelector("a[href='/users/" + from.getUsername() + "']"))
                                .getText()
                                .startsWith(from.getFirstName() + " " + from.getLastName());
            }
        });
    }
}
