package home_page.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;
import template.selenium.tests.BaseTest;

import home_page.pages.HomePage;

public class HomePageTest extends BaseTest {

    private HomePage homePage;

    @BeforeClass
    public void initialize() {
        homePage = new HomePage(driver);
    }

    @Test
    public void testGetHomePage() {
        Assert.assertEquals(homePage.getPageTitle(),"Home Page");
        Assert.assertTrue(homePage.isCustomerLinkDisplayed("Customers"));
    }

    @Test
    public void testGoToCustomersPage() {
        homePage.clickOnCustomerLink();
        Assert.assertEquals(homePage.getPageTitle(),"Customers | Home Page");
    }
}
