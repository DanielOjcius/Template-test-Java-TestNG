package home_page.pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import home_page.locators.HomeLocators;
import template.selenium.BasePage;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver){
        super(driver);
        waitVisibilityOf(HomeLocators.navigationBar);
    }

    public boolean isCustomerLinkDisplayed(String navLinkText) {

        try {
            waitError.until(visibilityOfElementLocated(HomeLocators.customersPageLink));
            return getElemText(HomeLocators.customersPageLink)== navLinkText;
        }
        catch (TimeoutException e) {
            return false;
        }
    }

    public void clickOnCustomerLink() {
        try {
            waitError.until(visibilityOfElementLocated(HomeLocators.customersPageLink));
            clickOnElem(HomeLocators.customersPageLink);
        }
        catch (TimeoutException e) {
        }
    }
}
