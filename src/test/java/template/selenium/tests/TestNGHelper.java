package template.selenium.tests;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.codehaus.groovy.runtime.wrappers.BooleanWrapper;
import org.testng.TestNG;
import java.lang.reflect.Method;

import org.testng.annotations.ITestAnnotation;
import org.testng.internal.ClassHelper;
import org.testng.internal.annotations.AnnotationHelper;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.annotations.JDK15AnnotationFinder;
import org.testng.xml.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class TestNGHelper{

    public static void main(String[] args) throws Exception{

        String browserArguments = System.getProperty("browser");
        boolean nonInteractive = Boolean.parseBoolean(System.getProperty("non-interactive"));
        String xmlFile = System.getProperty("xmlFile");
        List<String> browserList = new ArrayList<>();

        if(browserArguments != null) //If no browser name is specified, it defaults to Google Chrome
            browserList = Arrays.asList(browserArguments.split("\\s*,\\s*"));
        else {
            browserList.add("chrome");
        }

        XmlSuite suite = new XmlSuite();

        if(xmlFile != null){
            createTestsFromXML(xmlFile, browserList, suite);
            suite.setName("XML Test");

        }
        else {
            if (nonInteractive == false) {
                List<String> xmlFiles = checkExistenceOfXMLFiles();
                Scanner sc = new Scanner(System.in);
                if (xmlFiles != null) {
                    System.out.println("No XML File was specified as argument.");
                    System.out.println("The following files were found: ");
                    for (int i = 0; i < xmlFiles.size(); i++) {
                        System.out.println(i + 1 + ") Filename: " + xmlFiles.get(i));
                    }
                    System.out.println("0) Run all tests");
                    int option = sc.nextInt();
                    if (option != 0) {
                        createTestsFromXML(xmlFiles.get(option - 1), browserList, suite);
                        suite.setName(xmlFiles.get(option));
                    } else {
                        createTestsProgrammatically(browserList, suite);
                        suite.setName("Programmatic tests");
                    }
                } else {
                    System.out.println("No XML files found. Do you wish to run all tests? Y/N");
                    String yes = sc.nextLine();
                    if (yes.equalsIgnoreCase("y")) {
                        createTestsProgrammatically(browserList, suite);
                        suite.setName("Programmatic tests");
                    } else
                        return;
                }
            }
            else {
                createTestsProgrammatically(browserList, suite);
                suite.setName("Programmatic tests");
            }
        }

        List<XmlSuite> suites = new ArrayList<>();
        suites.add(suite);

        TestNG tng = new TestNG();
        tng.setXmlSuites(suites);
        tng.run();
    }

    private static List<String> checkExistenceOfXMLFiles() {
        try{
            Stream<Path> walk = Files.walk(Paths.get("src/test"));
            return walk.map(Path::toString).filter(f -> f.endsWith(".xml")).collect(toList());
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private static void createTestsFromXML(String xmlFilePath, List<String> browserList, XmlSuite suite) {
        try {
            File inputFile = new File(xmlFilePath);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            doc.getDocumentElement().normalize();

            NodeList testNodes = doc.getElementsByTagName("test");

            List<SuiteTest> testList = new ArrayList<>();
            if (testNodes.getLength() != 0) {

                Element parameter = (Element) doc.getElementsByTagName("parameter").item(0);

                for (int i = 0; i < testNodes.getLength(); i++) {
                    testList.add(getTestsFromXML(testNodes.item(i)));
                    testList.get(i).setParameter(parameter.getAttribute("name"));
                }
                setupTests(browserList, testList, suite);
            }

            else {
                NodeList suiteNodes = doc.getElementsByTagName("suite-file");
                for(int i = 0; i < suiteNodes.getLength(); i++){
                    Element suiteName = (Element) doc.getElementsByTagName("suite-file").item(i);
                    String path = "src/test/suites/"+suiteName.getAttribute("path");
                    createTestsFromXML(path, browserList, suite);
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SuiteTest getTestsFromXML(Node node) {

        SuiteTest test = new SuiteTest();

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            test.setTestName(element.getAttribute("name"));
            List<String> testClasses = new ArrayList<>();
            List<String> testIncludedGroups = new ArrayList<>();
            List<String> testExcludedGroups = new ArrayList<>();


            Element elemClasses = (Element) element.getElementsByTagName("classes").item(0);
            for(int i=0; i<element.getElementsByTagName("class").getLength(); i++){
                Element elemClass = (Element) elemClasses.getElementsByTagName("class").item(i);
                testClasses.add(elemClass.getAttribute("name"));
            }

            if (element.getElementsByTagName("groups").getLength() != 0){
                Element elemGroups = (Element) element.getElementsByTagName("groups").item(0);
                for (int i=0; i<elemGroups.getElementsByTagName("include").getLength(); i++){
                    Element elemInclude = (Element) elemGroups.getElementsByTagName("include").item(i);
                    testIncludedGroups.add(elemInclude.getAttribute("name"));
                }
                for (int i=0; i<elemGroups.getElementsByTagName("exclude").getLength(); i++){
                    Element elemExclude = (Element) elemGroups.getElementsByTagName("exclude").item(i);
                    testExcludedGroups.add(elemExclude.getAttribute("name"));
                }
            }

            test.setClasses(testClasses);
            test.setIncludedGroups(testIncludedGroups);
            test.setExcludedGroups(testExcludedGroups);
        }
        return test;
    }

    private static void setupTests(List<String> browserList, List<SuiteTest> testFromXMLList, XmlSuite suite) throws Exception {
        for (SuiteTest suiteTest: testFromXMLList) {
            for (String browser: browserList) {
                if(browser.equalsIgnoreCase("chrome") || browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("edge")) {
                    List<XmlClass> classes = new ArrayList<>();

                    XmlTest test = new XmlTest(suite);
                    List<String> testClasses = suiteTest.getClasses();
                    List<String> testIncludedGroups = suiteTest.getIncludedGroups();
                    List<String> testExcludedGroups = suiteTest.getExcludedGroups();

                    for (String testClass : testClasses) {
                        classes.add(new XmlClass(testClass));
                    }

                    for (String testIncludedGroup : testIncludedGroups) {
                        test.addIncludedGroup(testIncludedGroup);
                    }

                    for (String testExcludedGroup : testExcludedGroups) {
                        test.addExcludedGroup(testExcludedGroup);
                    }

                    test.setName(suiteTest.getTestName() + " using " + browser + " browser");
                    test.addParameter("browser", browser);
                    test.setXmlClasses(classes);
                    if (!browser.equalsIgnoreCase("edge")){
                        suite.setParallel(XmlSuite.ParallelMode.TESTS);
                    }
                }


                else throw new Exception("Incorrect browser: " + browser);
            }
        }
    }

    private static void createTestsProgrammatically(List<String> browserList, XmlSuite suite) throws Exception{

        File dir = new File("src");

        List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        List<XmlClass> classes = new ArrayList<>();

        for (File f : files) {
            String path = f.getPath().replaceAll("[\\\\|/]", ".");
            if(path.startsWith("src.test.java") && path.endsWith(".java")){
                path = path.replace("src.test.java.", "");
                path = path.replace(".java", "");
                XmlClass xmlClass = new XmlClass(path);
                classes.add(xmlClass);
            }
        }

        IAnnotationFinder finder = new JDK15AnnotationFinder(new AnnotationTransformer());

        for (String browser: browserList) {
            if(browser.equalsIgnoreCase("chrome") || browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("edge")) {

                for (XmlClass currentClass: classes) {
                    Set<Method> classMethods = ClassHelper.getAvailableMethods(currentClass.getSupportClass()); //Obtains all methods of the current class
                    Set<String> testGroups = new HashSet<>();
                    List<ITestAnnotation> testMethods = new ArrayList<>();

                    for (Method method : classMethods) {  //This loop selects all methods that have the '@Test' annotation and loads them in a list
                        ITestAnnotation testAnnotation = AnnotationHelper.findTest(finder, method);

                        if (testAnnotation != null) {
                            testMethods.add(testAnnotation);
                        }
                    }

                    for (ITestAnnotation annotation: testMethods){
                        if (annotation.getGroups().length > 0){
                            testGroups.add(annotation.getGroups()[0]);
                        }
                    }

                    for(String group: testGroups){ //This for loop creates a test for each group of a class
                        List<XmlClass> currentTestClass = new ArrayList<>();
                        XmlTest test = new XmlTest(suite);
                        test.addIncludedGroup(group);
                        test.addParameter("browser", browser);
                        currentTestClass.add(currentClass);
                        test.setXmlClasses(currentTestClass);
                        test.setName(currentClass.getName() + " - Group: " + group + " - " + browser);

                        Iterator<ITestAnnotation> iterator = testMethods.iterator();

                        while (iterator.hasNext()){
                            ITestAnnotation auxAnnotation = iterator.next(); //An auxiliar variable is needed due to the use of iterator.next()
                            if (auxAnnotation.getGroups().length > 0){
                                if (auxAnnotation.getGroups()[0].equals(group)) {
                                    iterator.remove();
                                }
                            }
                        }
                    }

                    if(testMethods.size()>0){ //If there are tests without a defined group, another test is created to group them
                        List<XmlClass> currentTestClass = new ArrayList<>();
                        XmlTest test = new XmlTest(suite);
                        test.addParameter("browser", browser);
                        currentTestClass.add(currentClass);
                        test.setXmlClasses(currentTestClass);
                        test.setName(currentClass.getName() +" - No groups test" + " - " + browser);
                    }

                }
                if (!browser.equalsIgnoreCase("edge")){
                    suite.setParallel(XmlSuite.ParallelMode.TESTS);
                }
            }

            else throw new Exception("Incorrect browser: " + browser);
        }
    }
}