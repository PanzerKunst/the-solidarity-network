import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchHelpRequests extends TestBase {

    private static final String hrText = "I can help, send me a message!";

    public static void clickOnFirstSearchResult(WebDriver driver) {
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".action a[href='/help']")).isDisplayed();
            }
        });

        if (driver instanceof ChromeDriver) {
            sleepBecauseSeleniumSucks();
        }

        driver.findElement(By.cssSelector(".action a[href='/help']"))
                .click();

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".action a[href='/help-requests']")).isDisplayed();
            }
        });
        driver.findElement(By.cssSelector(".action a[href='/help-requests']"))
                .click();

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("search-button")).isDisplayed();
            }
        });
        driver.findElement(By.id("search-button"))
                .click();

        String cssSelectorOfFirstDisplayedSearchResult = "li[data-id='1']";

        sleepBecauseSeleniumSucks();

        if (driver.findElement(By.id("desktop-results")).isDisplayed()) {
            cssSelectorOfFirstDisplayedSearchResult = "tr[data-id='1']";
        }

        sleepBecauseSeleniumSucks(2000);

        driver.findElement(By.cssSelector(cssSelectorOfFirstDisplayedSearchResult))
                .click();

    }
}
