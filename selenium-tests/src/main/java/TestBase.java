import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBase {
    public static void clickOnMobileMenuLinkIfRequired(WebDriver driver) {
        // Wait for the page to load
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("#header-nav")).isDisplayed();
            }
        });

        WebElement menuAnchor = driver.findElement(By.cssSelector("#header-nav > span > button"));
        if (menuAnchor.getAttribute("innerHTML").indexOf("Menu") > -1 &&  // Mobile browser
                !driver.findElement(By.cssSelector("#header-nav > ul")).isDisplayed()) {

            if (driver instanceof InternetExplorerDriver) {
                sleepBecauseSeleniumSucks(2000);
            }

            menuAnchor.click();
        }

        // Wait for the content to be displayed
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("a[href='/about']")).isDisplayed();
            }
        });
    }

    public static void sleepBecauseSeleniumSucks() {
        sleepBecauseSeleniumSucks(500);
    }

    public static void sleepBecauseSeleniumSucks(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            // Nothing
        }
    }
}
