# Template : UI Selenium Test

This is a Guide to install and use the UI Selenium Test template. This template provide a basic structure to test an UI. 
Using Java, Maven, Selenium, TestNG and Allure.
Also, has a GitLab Pipeline configured and a [guide for do it](docs/HowToUsePipeline.md).

Requirements (tools needed)
---
 - Java JDK (recommended version 12.0.1). [How to install Java JDK](docs/HowToInstallJavaJDK.md)
 - Maven (recommended version 3.6.1). [How to install Maven](docs/maveninstall.md)
 - GIT (recommended version 2.21.0). [How to install GIT](https://git-scm.com/downloads)
 - Allure (recommended version 2.12.1). [How to install Allure](docs/StepsToUseAllure.md)
 - WebDriver (at least one). [Chrome Web Driver Site](http://chromedriver.chromium.org/), [Firefox Web Driver Site](https://github.com/mozilla/geckodriver/releases), [Edge Web Driver Site](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/)

Packages
---

 - **java.template.selenium:** This package contains BasePage class. Is where all api page's goes
 - **java.example:** An example to use the template (pages, endpoint, etc)
 - **test.java.template.selenium.data:** This package contains all data provider
 - **test.java.template.selenium.suites:** This package contains all xml files, that run the tests 
 - **test.java.template.selenium.tests:** This package contains all test classes
 - **test.java.example:** An example to use the template (tests)


Classes
---

**BasePage:** This class contains the common behavior of the page

**HomePage** the Home Page 

**Menu Item Page** a Menu Item Page

***Locators:** These classes contains the selenium page locators

**BaseTest:** This class contains the common behavior of the tests


XML Files
---
**allTestsSuite:** This file contains all xml files, that run the tests 

**aSuite:** This file contains a example of suite test


Guide links
---
 - [Explaining the example](docs/ExplainingTheExample.md)
 - [Steps to use the template](docs/StepsToUseTemplate.md)
 - [Steps to use Allure](docs/StepsToUseAllure.md)


References
---
 - [Selenium](https://www.seleniumhq.org/docs/)
 - [Testng](https://testng.org/doc/documentation-main.html)
 - [Allure](https://docs.qameta.io/allure/)
 - [Page Object Model](https://medium.com/tech-tajawal/page-object-model-pom-design-pattern-f9588630800b)
