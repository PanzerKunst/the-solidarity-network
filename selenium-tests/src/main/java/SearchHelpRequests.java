import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchHelpRequests {

    private static final String hrText = "I can help, send me a message!";

    public static void clickOnFirstSearchResult(WebDriver driver) {
        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".actions a[href='/help']")).isDisplayed();
            }
        });
        driver.findElement(By.cssSelector(".actions a[href='/help']"))
                .click();

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector(".actions a[href='/help-requests']")).isDisplayed();
            }
        });
        driver.findElement(By.cssSelector(".actions a[href='/help-requests']"))
                .click();

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.id("search-button")).isDisplayed();
            }
        });
        driver.findElement(By.id("search-button"))
                .click();

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.cssSelector("tr[data-id='1']")).isDisplayed();
            }
        });
        driver.findElement(By.cssSelector("tr[data-id='1']"))
                .click();

    }
}
