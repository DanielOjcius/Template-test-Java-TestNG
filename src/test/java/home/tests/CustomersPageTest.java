package home_page.tests;

import com.google.common.base.Verify;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import home_page.pages.CustomersPage;
import template.selenium.tests.BaseTest;

public class CustomersPageTest extends BaseTest {

    private CustomersPage customersPage;

    @BeforeClass
    public void initialize() {
        customersPage = new CustomersPage(driver);
    }


    @Test
    public void testVerifyCustomersPage() {
        Assert.assertEquals(customersPage.getPageTitle(),"Customers | Home Page");
    }

    @Test
    public void testVerifyCustomerText() {
        Assert.assertEquals(customersPage.getCustomerText("Deluxe",2));
    }
}
