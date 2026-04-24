package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import actiondriver.ActionDriver;
import utilities.ExtentManager;
import utilities.LoggerManager;

/* 
 * backbone of framework
 * contains common setup and teardown logic shared across all test cases
 * common functionalities in BaseClass:
	• Initializing WebDriver and Initial browser launch
	• Reading properties from config.properties
	• Setting implicit wait (managing wait time)
	• maximize window
	• Closing browser after test
	
 * Code reusability: Code Reusability: Avoid duplicate setup/teardown code in each test class.
 * Scalability: Makes the framework easier to maintain and scale.
 * Centralized Control: Any changes in test setup (e.g., browser settings) can be applied globally from one class.
 * Foundation for Test Execution: Acts as a base for all test classes.
 */

public class BaseClass {
	/*
	 * protected because they can be accessed within the same class/ same package/
	 * subclasses in same package through inheritance can be accessed by subclass in
	 * different package through inheritance via its own instance or instance of
	 * it's own subclass, not through instance of parent class
	 */

	protected static Properties prop;
	// protected static WebDriver driver;
	// private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
	
	//getter method for assert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}
	
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	
	@BeforeSuite
	/*
	 * since befouresuite will only run once, driver initialisation happens only for
	 * the first test class in testng.xml and the other classes will fail hence
	 * initialise the variable as protected static Properies prop; static will carry
	 * the same value like the instance will be same for other classes as well
	 */
	public void loadConfig() throws IOException {
		// load properties/config file
		prop = new Properties(); // create an empty object of properties file
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties"); // to read the file, create
																							// object of FileInputStream
		prop.load(fis); // load the file
		logger.info("Config.properties file loaded");		
	}

	// initialise the webdriver based on browser defined in config file
	private void launchBrowser() {
		String browser = prop.getProperty("browser");
		switch (browser.toLowerCase()) {
		case "chrome":
			
			//create ChromeOptions
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless"); //run chrome in headless mode
			options.addArguments("--disable-gpu");// disable gpu for headless mode
			//options.addArguments("--window-size=1920,1080");//set window size
			options.addArguments("--disable-notifications");//disable browser notification
			options.addArguments("--no-sandbox");//required for some CI environmnets like
			options.addArguments("--disable-dev-shn-usage");//resolve issues in resources
			
			// driver = new ChromeDriver();
			driver.set(new ChromeDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver instance is created");
			break;
		case "edge":
			
			//create EdgeOptions
			EdgeOptions options1 = new EdgeOptions();
			options1.addArguments("--headless"); 
			options1.addArguments("--disable-gpu");
			//options1.addArguments("--window-size=1920,1080");
			options1.addArguments("--disable-notifications");
			options1.addArguments("--no-sandbox");
			options1.addArguments("--disable-dev-shn-usage");
			
			// driver = new EdgeDriver();
			driver.set(new EdgeDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver instance is created");
			break;
		case "firefox":
			//create Firefox Options
			FirefoxOptions options2 = new FirefoxOptions();
			options2.addArguments("--headless"); 
			options2.addArguments("--disable-gpu");
			//options2.addArguments("--window-size=1920,1080");
			options2.addArguments("--disable-notifications");
			options2.addArguments("--no-sandbox");
			options2.addArguments("--disable-dev-shn-usage");
			
			
			// driver = new FirefoxDriver();
			driver.set(new FirefoxDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver instance is created");
			break;
		default:
			throw new IllegalArgumentException("Browser not supported: " + browser);
		}
	}

	// configure browser setting: implicit wait, maximise browser, open url
	private void configBrowser() {
		// Implicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// Maximize browser
		getDriver().manage().window().maximize();

		// Navigate to url
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to navigate to the url: " + e.getMessage());
		}
	}

	/*
	 * @BeforeMethod instead of @BeforClass ensures that each test method starts
	 * with a fresh browser instance, which avoids test interdependencies and
	 * improves reliability. It's ideal for UI tests where isolation is important.
	 */

	@BeforeMethod
	public synchronized void setup() {
		System.out.println("Setting of webdriver for: " + this.getClass().getSimpleName());
		launchBrowser();
		configBrowser();
		logger.info("WebDriver Initialised and Browser maximised");
		logger.trace("This is a Trace Message");
		logger.error("This is a Error Message");
		logger.debug("This is a debug Message");
		logger.fatal("This is a fatal Message");
		logger.warn("This is a warning Message");

		/*
		 * //initialise the action driver oly once if(actionDriver == null) {
		 * actionDriver = new ActionDriver(driver);
		 * logger.info("ActionDriver instance is created" +
		 * Thread.currentThread().getId()); }
		 */

		// initialise actionDriver for the current thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialsied for thread: " + Thread.currentThread().getId());
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (driver != null) {
			try {
				getDriver().quit(); // will close all pages and webdriver session
			} catch (Exception e) {
				logger.error("Unable to quite driver: " + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed");
		driver.remove();
		actionDriver.remove();
		/*
		 * driver = null; actionDriver = null;
		 */
	}

	// static wait to pause - uses nano instead of seconds
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	// since variables are declared as protected, how to access them ouside the
	// package
	// create getter and setter methods to use the variable outside package

	/*
	 * //driver getter method public WebDriver getDriver() { return driver; }
	 * 
	 * //driver setter method public void setDriver(WebDriver driver) {
	 * this.driver=driver; }
	 */

	// getter method for prop
	public static Properties getProp() {
		return prop;
	}
	
	//Driver setter ethod
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	// Getter Method for driver
	public static WebDriver getDriver() {

		if (driver.get() == null) {
			System.out.println("Webdriver is not initialsied");
			throw new IllegalStateException("Webdriver is not initialsied");
		}
		return driver.get();
	}
	
	

	// Getter Method for action Driver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialsied");
			throw new IllegalStateException("ActionDriver is not initialsied");
		}
		return actionDriver.get();
	}
}
