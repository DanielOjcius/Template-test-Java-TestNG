/**
 *  This package contains the common behavior of the page
 */
package template.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;


public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait waitError;

    protected final String BASE_URL = "https://www.blazemeter.com/";

    public BasePage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 5);
        this.waitError = new WebDriverWait(driver,2);
    }


    protected WebElement waitVisibilityOf(By locator) {
        return wait.until(visibilityOfElementLocated(locator));
    }

    protected boolean waitInvisibilityOf(By locator) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            return true;
        }
        catch(TimeoutException e) {
            return false;
        }
    }

    protected boolean isElementVisible(By locator) {
        try {
            waitVisibilityOf(locator);
            return true;
        }
        catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean isElementDisplayed(By locator) {
        return driver.findElement(locator).isDisplayed();
    }

    protected boolean isTextInElementLocated (By elementLoc, String text){
        try {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(elementLoc, text));
            return true;
        }
        catch (TimeoutException e){
            return false;
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getElemText(By elem) {
        return driver.findElement(elem).getText();
    }

    public WebElement getElemFromList(By elemList, Integer index) {
        return driver.findElements(elemList).get(index);
    }

    public void clickOnElem(By elem){
        try {
            wait.until(ExpectedConditions.elementToBeClickable (elem));
            driver.findElement(elem).click();
        }
        catch (TimeoutException e){

        }
    }

}
