import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.TestBase;

public class Login extends TestBase {
    public static void incorrectPassword(WebDriver driver) {
        driver.findElement(By.id("password"))
                .sendKeys("tigru");

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait for the page to load, and check we have been redirected to "home"
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("auth-failed"))
                        .isDisplayed();
            }
        });
    }

    public static void properFormFillUsername(WebDriver driver, User user) {
        boolean isAlreadyAtThatPage = driver.findElement(By.tagName("body")).getAttribute("id").equals("login");

        if (!isAlreadyAtThatPage) {
            TestBase.clickOnMobileMenuLinkIfRequired(driver);

            if (driver instanceof ChromeDriver) {
                TestBase.sleepBecauseSeleniumSucks();
            }

            // Wait for the page to load
            (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.findElement(By.cssSelector("a[href='/login']")).isDisplayed();
                }
            });

            driver.findElement(By.cssSelector("a[href='/login']"))
                    .click();
        }

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("username-or-email")).isDisplayed();
            }
        });

        driver.findElement(By.id("username-or-email"))
                .clear();

        driver.findElement(By.id("password"))
                .clear();

        driver.findElement(By.id("username-or-email"))
                .sendKeys(user.getUsername());

        driver.findElement(By.id("password"))
                .sendKeys(user.getPassword());

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait for the page to load, and check we have been redirected to "home"
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().equals(Runner.URL_ROOT + "home");
            }
        });

        clickOnMobileMenuLinkIfRequired(driver);

        // Wait for the page to load, and check that the "logout" link exists
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("a[href='/logout']")).isDisplayed();
            }
        });
    }

    public static void properFormFillEmail(WebDriver driver, User user) {
        driver.findElement(By.cssSelector("a[href='/login']"))
                .click();

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("username-or-email")).isDisplayed();
            }
        });

        driver.findElement(By.id("username-or-email"))
                .sendKeys(user.getEmail());

        driver.findElement(By.id("password"))
                .sendKeys(user.getPassword());

        driver.findElement(By.tagName("form"))
                .submit();

        // Wait for the page to load, and check we have been redirected to "home"
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().equals(Runner.URL_ROOT + "home");
            }
        });

        clickOnMobileMenuLinkIfRequired(driver);

        // Wait for the page to load, and check that the "logout" link exists
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("a[href='/logout']")).isDisplayed();
            }
        });
    }

    public static void logout(WebDriver driver) {
        clickOnMobileMenuLinkIfRequired(driver);

        if (driver instanceof ChromeDriver) {
            sleepBecauseSeleniumSucks();
        }

        driver.findElement(By.cssSelector("a[href='/logout']"))
                .click();

        sleepBecauseSeleniumSucks();

        // Wait for the page to load, and check that menu is logged-out
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().equals(Runner.URL_ROOT);
            }
        });

        clickOnMobileMenuLinkIfRequired(driver);

        // Wait for the page to load, and check that menu is logged-out
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("header-nav"))
                        .getText().indexOf("Login") > -1;
            }
        });
    }
}
