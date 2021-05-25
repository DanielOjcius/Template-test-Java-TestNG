# Explain the Template use:

- Set different resources on Endpoint Interface 

- Set url of the site to use (in BasePage). 
BaseTest, class, setUP webdriver on @BeforeClass, and have configuration for different Drivers (chrome, firefox, ie).
The new test have to extends BaseTest class.

- Create different Pages Objects, with specific responsibilities. Build methods to navigate on web

- If you need to send different data to some test, implement a data provider. 
For example, at the Home Page you can click the different links, and verify texts, give different results in each test.
It is a good option for not implement many tests with the same functionality

- Create new tests classes, if you have different functional conditions. Is good practice to make different test pages.
For example, one for each Navigation Bar link, others to validate Case Studies, etc.

- Implement test suites for including test classes 
