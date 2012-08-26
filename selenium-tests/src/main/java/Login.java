import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login {
    public static void incorrectPassword(WebDriver driver) {
        driver.findElement(By.id("password"))
                .sendKeys("tigru");

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait 5s for the page to load, and check we have been redirected to "home"
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("auth-failed"))
                        .isDisplayed();
            }
        });
    }

    public static void properFormFillUsername(WebDriver driver) {
        driver.findElement(By.id("username-or-email"))
                .clear();

        driver.findElement(By.id("password"))
                .clear();

        driver.findElement(By.id("username-or-email"))
                .sendKeys("blackbird");

        driver.findElement(By.id("password"))
                .sendKeys("tigrou");

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait 5s for the page to load, and check we have been redirected to "home"
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().equals(Runner.URL_ROOT + "home") &&
                        d.findElement(By.cssSelector("a[href='/logout']")).isDisplayed();
            }
        });
    }

    public static void properFormFillEmail(WebDriver driver) {
        driver.findElement(By.cssSelector("a[href='/login']"))
                .click();

        // Wait 5s for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("username-or-email")).isDisplayed();
            }
        });

        driver.findElement(By.id("username-or-email"))
                .sendKeys("cbramdit@gmail.com");

        driver.findElement(By.id("password"))
                .sendKeys("tigrou");

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait 5s for the page to load, and check we have been redirected to "home"
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().equals(Runner.URL_ROOT + "home") &&
                        d.findElement(By.cssSelector("a[href='/logout']")).isDisplayed();
            }
        });
    }

    public static void logout(WebDriver driver) {
        driver.findElement(By.cssSelector("a[href='/logout']"))
                .click();

        // Wait 5s for the page to load, and check that menu is logged-out
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().equals(Runner.URL_ROOT) &&
                        d.findElement(By.id("header-nav"))
                                .getText().indexOf("Login") > 0;
            }
        });
    }
}
