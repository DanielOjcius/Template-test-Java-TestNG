package home_page.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import home_page.locators.CustomersLocators;
import template.selenium.BasePage;

public class CustomersPage extends BasePage {

    public CustomersPage(WebDriver driver) {
        super(driver);
    }

    public String verifyCustomerText(String customerText, Integer index) {
        try {
            waitError.until(visibilityOfElementLocated(CustomersLocators.customersList));
            return getElemFromList(CustomersLocators.customersList, index).getText();
        }
        catch (TimeoutException e) {
            return "";
        }
    }
}
