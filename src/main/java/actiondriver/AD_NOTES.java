package actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseClass;

/*
 * ActionDriver is a custom utility class that wraps reusable Selenium actions like 
 * click, enterText, getText, etc., with additional handling like:
 * Explicit waits, Exception logging, Visibility/clickability checks, Custom messages
 * 
 * Centralized code reuse – no repetition across test or page classes
 * Better readability and maintainability
 * Automatic handling of waits and exceptions
 * Isolates Selenium logic from business/test logic
 * Easier debugging with meaningful System.out logs
 * 
 */
public class AD_NOTES {
	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public AD_NOTES(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("WebDriver insatnce is created");
	}

	// wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Element is not clickable: " + e.getMessage());
		}
	}

	// wait for element to be visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible: " + e.getMessage());
		}
	}

	// method to click
	public void click(By by) {
		try {
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			logger.info("CLicked an element");
		} catch (Exception e) {
			//System.out.println("Unable to click: " + e.getMessage());
			logger.error("Unable to click an element");
		}
	}

	// method to enter text into input field
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered Text: " +value);
		} catch (Exception e) {
			logger.error("Unable to enter value in input box: " + e.getMessage());
		}
	}

	// Method to get text from input field
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			logger.error("Unable to get text: " + e.getMessage());
			return "";
		}
	}

	// method to compare text
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				logger.info("Text are matching: " + actualText + " equals " + expectedText);
				return true;
			} else {
				logger.error("Text are not matching: " + actualText + " not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			logger.error("Unable to compare texts: " + e.getMessage());
		}
		return false;
	}

	/*
	 * // method to check if an element is displayed public boolean isDisplayed(By
	 * by) { try { waitForElementToBeVisible(by); boolean isDisplayed =
	 * driver.findElement(by).isDisplayed(); if (isDisplayed) {
	 * System.out.println("Element is visible"); return isDisplayed; } else { return
	 * isDisplayed; } } catch (Exception e) {
	 * System.out.println("Element is not displayed: " + e.getMessage()); return
	 * false; } }
	 */
	
	// method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			logger.error("Element is not displayed: " + e.getMessage());
			return false;
		}
	}

	// scroll to an element
	public void scrollToElement(By by) {
		try {
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", element);
			logger.info("Scrolled to element: " + by.toString());
		} catch (Exception e) {
			logger.error("Unable to scroll to element: " + by.toString() + ". Exception: " + e.getMessage());
		}

	}

	// wait for pageLoad
	public void waitForPageToLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver).executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded successfully.");
		} catch (Exception e) {
			logger.error("Page did not load within " +timeOutInSec + "seconds.Exception: " +e.getMessage());
		}
	}
	
	
}
