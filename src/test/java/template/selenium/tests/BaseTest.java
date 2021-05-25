/**
 * this class contains the common behavior of the test
 */
package template.selenium.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;


public abstract class BaseTest {

    protected static WebDriver driver;

    // this method set the web driver
    @BeforeClass
    @Parameters("browser")
    public static WebDriver setUp(String browser) throws Exception {

        if(browser.equalsIgnoreCase("firefox")){
            System.setProperty("webdriver.gecko.driver", "geckodriver");
            FirefoxOptions options = new FirefoxOptions();
            options.setHeadless(true);
            driver = new FirefoxDriver(options);
        }
        //Check if parameter passed as 'chrome'
        else if(browser.equalsIgnoreCase("chrome")){
            System.setProperty("webdriver.chrome.driver", "chromedriver");
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);
            driver = new ChromeDriver(options);
        }
        //Check if parameter passed as 'Edge'
        else if(browser.equalsIgnoreCase("Edge")){
            driver = new EdgeDriver();
        }
        else {
            //If no browser received throw exception
            throw new Exception("Browser is not correct");
        }
        return driver;
    }

    //This method close the browser after tests
    @AfterClass
    public void tearDown(){
        driver.quit();
    }

    //This method delete all cookies
    @AfterMethod
    public void cleanCookies() {
        driver.manage().deleteAllCookies();
    }
}
