import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EditProfile extends TestBase {

    public static void checkProfileTab(WebDriver driver, final User user) {
        driver.findElement(By.cssSelector("#content a[href='/my-profile/edit']"))
                .click();

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return checkProfileValuesForUser(d, user);
            }
        });
    }

    public static void checkAccountTab(WebDriver driver, final User user) {
        driver.findElement(By.id("show-account-info"))
                .click();

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return checkAccountValuesForUser(d, user);
            }
        });
    }

    public static void properFormFill(WebDriver driver, final User user) {
        /**
         * Profile tab
         */
        driver.findElement(By.id("show-profile-info"))
                .click();

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("first-name")).isDisplayed();
            }
        });


        driver.findElement(By.id("first-name"))
                .clear();

        driver.findElement(By.id("last-name"))
                .clear();

        driver.findElement(By.id("city"))
                .clear();


        driver.findElement(By.id("first-name"))
                .sendKeys(user.getFirstName());

        driver.findElement(By.id("last-name"))
                .sendKeys(user.getLastName());

        driver.findElement(By.id("street-address"))
                .sendKeys(user.getStreetAddress());

        driver.findElement(By.id("post-code"))
                .sendKeys(user.getPostCode());

        driver.findElement(By.id("city"))
                .sendKeys(user.getCity());

        Select droplist = new Select(driver.findElement(By.id("country")));
        droplist.selectByValue(user.getCountryId());

        driver.findElement(By.id("description"))
                .sendKeys(user.getDescription());

        /**
         * Account tab
         */
        driver.findElement(By.id("show-account-info"))
                .click();

        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("email")).isDisplayed();
            }
        });

        driver.findElement(By.id("email"))
                .clear();

        driver.findElement(By.id("email"))
                .sendKeys(user.getEmail());

        driver.findElement(By.id("email-confirmation"))
                .sendKeys(user.getEmail());

        driver.findElement(By.tagName("form"))
                .submit();

        /**
         * Checking result
         */

        // Wait for the page to load, check account tab
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".indication"))
                        .getAttribute("innerHTML")
                        .equals("Saved!") &&
                        checkAccountValuesForUser(d, user);
            }
        });

        driver.findElement(By.id("show-profile-info"))
                .click();

        // Wait for the page to load, check profile tab
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return checkProfileValuesForUser(d, user);
            }
        });

    }

    private static boolean checkProfileValuesForUser(WebDriver driver, User user) {
        return driver.findElement(By.id("first-name")).getAttribute("value").equals(user.getFirstName()) &&
                driver.findElement(By.id("last-name")).getAttribute("value").equals(user.getLastName()) &&
                driver.findElement(By.id("city")).getAttribute("value").equals(user.getCity()) &&
                driver.findElement(By.id("country")).getAttribute("value").equals(user.getCountryId()) &&
                (driver.findElement(By.id("description")).getAttribute("value").equals(user.getDescription()) ||
                        driver.findElement(By.id("description")).getAttribute("value").equals("") && user.getDescription() == null);
    }

    private static boolean checkAccountValuesForUser(WebDriver driver, User user) {
        return driver.findElement(By.id("email")).getAttribute("value").equals(user.getEmail());
    }
}
