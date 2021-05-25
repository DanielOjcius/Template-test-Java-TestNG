****Previous steps:****

Install the following maven plugins:
 
- "exec-maven-plugin" (https://www.mojohaus.org/exec-maven-plugin/usage.html).
- "Apache Commons IO" (https://mvnrepository.com/artifact/commons-io/commons-io/2.6)


Open pom.xml, and add the following fields inside the plugin `configuration` tags:



`<mainClass>javahipster.TestNGHelper</mainClass>`

`<classpathScope>test</classpathScope>`

![](imagesDocs/exec-maven-plugin-configuration.png)

And inside the surefire plugin configuration, add the following tag:

`<skipTests>true</skipTests>`

![](imagesDocs/surefire-configuration.png)

###### Adding security certificate: 

To avoid security issues when using the XML parser, a certificate must be added locally to the JVM TrustStore. To do so:

Open a terminal and run the following command `openssl s_client -showcerts -connect <REMOTE_URL:PORT>`
 
_In this case: openssl s_client -showcerts -connect testng.org:443_

The following will be displayed on the console:

![](imagesDocs/certificateConsole.png)

Copy the lines starting from ---BEGIN CERTIFICATE--- and ---END CERTIFICATE--- and save them as a .crt file (for example, root.crt)

Navigate to the $JAVA_HOME/lib/security from the terminal and then run sudo keytool -importcert -keystore cacerts -storepass changeit -file path-to-the-root.crt-file -alias "rhel-root"

###### Execution

Before actually running, the project needs to be compiled. To do so, open the maven terminal from IntelliJ:

![](imagesDocs/intellij-terminal.png)

And execute the following commands in order:
1. mvn clean
2. mvn compile
3. mvn install 

Notes: 

It is not necessary to execute mvn clean every time; however, it is recommended to do so, to clean any leftover files automatically generated
by previous runs.

All three commands can be executed at the same time on the command line, like `mvn clean compile install`.

After the project is compiled, execute the following command on the terminal: `mvn exec:java -D"browser"="chrome, firefox" -D"xmlFile"="path-to-xmlFile" -D"non-interactive"="boolean-value"`.

- Browser parameter accepts Chrome, Firefox or Edge as values. 
- xmlFile requires the full path to a valid XML file. For example, "/src/test/suites/countrySuite.xml"
  - If the XML file is a suite that contains another suites, the program will execute each one of them. 
- non-interactive accepts true or false as values. If it is true, the interaction with the user is avoided. 

Any of the arguments may not be used. If that's the case:
- The program will set Google Chrome as the default browser.
- The program will look up XML files inside /src/test/suites; if any files are found, its names will be displayed on screen
and the user may decide which one he wants to run. If there aren't any files (or the user so desires), 
all tests classes will be run.
- If the non-interactive argument's value is not specified, it defaults to false.
