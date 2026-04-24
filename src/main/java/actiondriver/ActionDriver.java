package actiondriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseClass;
import utilities.ExtentManager;

public class ActionDriver {
	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
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
		String elementDesc = getElementDescription(by);
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("CLicked element --> " + elementDesc);
			logger.info("CLicked element --> " + elementDesc);

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to click an element");

		}
	}

	// method to enter text into input field
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered Text on " + getElementDescription(by) + " --> " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to enter value in input box: " + e.getMessage());
		}
	}

	// Method to get text from input field
	public String getText(By by) {
		try {
			applyBorder(by, "green");
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			logger.error("Unable to get text: " + e.getMessage());
			applyBorder(by, "red");
			return "";
		}
	}

	// method to compare text
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				logger.info("Text are matching: " + actualText + " equals " + expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text",
						"Text Verified successfully! " + actualText + " equals " + expectedText);
				return true;
			} else {
				applyBorder(by, "red");
				logger.error("Text are not matching: " + actualText + " not equals " + expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparison Failed!",
						"Text compariosn failed! " + actualText + " not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to compare texts: " + e.getMessage());
		}
		return false;
	}

	// method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Element is displayed" + getElementDescription(by));
			ExtentManager.logStep("Element is displayed" + getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed",
					"Element is displayed: " + getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Element is not displayed: " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed",
					"Element is not displayed" + getElementDescription(by));
			return false;
		}
	}

	// scroll to an element
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", element);
			logger.info("Scrolled to element: " + by.toString());
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to scroll to element: " + by.toString() + ". Exception: " + e.getMessage());
		}

	}

	// wait for pageLoad
	public void waitForPageToLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded successfully.");
		} catch (Exception e) {
			logger.error("Page did not load within " + timeOutInSec + "seconds.Exception: " + e.getMessage());
		}
	}

	// method to get the description of an element using by locator
	public String getElementDescription(By locator) {

		// check for null driver or locator to avoid nullPointer Exception
		if (driver == null)
			return "driver is null";
		if (locator == null)
			return "Locator is null";

		// find element usinf locator
		WebElement element = driver.findElement(locator);

		// Get Element Attribute
		String name = element.getDomAttribute("name");
		String id = element.getDomAttribute("id");
		String text = element.getText();
		String className = element.getDomAttribute("class");
		String placeholder = element.getDomAttribute("placeholder");

		try {
			// Return description based on element attributes
			if (isNotEmpty(name)) {
				return "Element with name: " + name;
			} else if (isNotEmpty(id)) {
				return "Element with id: " + id;
			} else if (isNotEmpty(text)) {
				return "Element with text: " + truncate(text, 50);
			} else if (isNotEmpty(placeholder)) {
				return "Element with placeholder: " + placeholder;
			} else if (isNotEmpty(className)) {
				return "Element with class: " + className;
			}
		} catch (Exception e) {
			logger.error("Unable to describe the element: " + e.getMessage());
		}
		return "Unable to describe element";
	}

	// Utitlity method to check a string is not null or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utitlity method to truncate long string
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}

	// utility to border an element
	public void applyBorder(By by, String color) {
		try {
			// locate the element
			WebElement element = driver.findElement(by);

			// apply border
			String script = "arguments[0].style.border='3px solid " + color + " ' ";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("applied border color with " + color + " to element " + getElementDescription(by));
		} catch (Exception e) {
			logger.warn("failed to apply border to an element: " + getElementDescription(by), e.getMessage());
			;
		}

	}

	// ===========SELECT METHODS===========

	// method to select dropdown by visible text
	public void selectByVisibleText(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			Select select = new Select(element);
			select.selectByVisibleText(value);
			applyBorder(by, "green");
			logger.info("Selected dropdown value: " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to select dropdown value: " + value, e);

		}
	}

	// method to select dropdown by value
	public void selectByValue(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			Select select = new Select(element);
			select.selectByValue(value);
			applyBorder(by, "green");
			logger.info("Selected dropdown value: " + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to select dropdown value: " + value, e);
		}
	}

	// method to select dropdown by index
	public void selectByIndex(By by, int index) {
		try {
			WebElement element = driver.findElement(by);
			Select select = new Select(element);
			select.selectByIndex(index);
			applyBorder(by, "green");
			logger.info("Selected dropdown value by index: " + index);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to select dropdown value by index: " + index, e);
		}
	}

	// method to get all options from the dropdown
	public List<String> getDropdownOptions(By by) {
		List<String> optionsList = new ArrayList<>();
		try {
			WebElement dropdownElement = driver.findElement(by);
			Select select = new Select(dropdownElement);
			for (WebElement option : select.getOptions()) {
				optionsList.add(option.getText());
			}
			applyBorder(by, "green");
			logger.info("Retrieved dropdown options for: " + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get dropdown options: " + e.getMessage());
		}
		return optionsList;
	}

	// ============================ Javascript Utility Methods========

	// method to click using JS
	public void clickUsingJS(By by) {
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			applyBorder(by, "green");

			logger.info("CLicked element usinf JavaScript --> " + getElementDescription(by));

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to click using JavaScript", e);
		}
	}

	// method to scroll to bottom pf page
	public void scrollToBottom() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");

	}

	// method to highlight an element using jav script
	public void highlightElementJS(By by) {
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid green'", element);
			logger.info("Highlighted element using JavaScript: " + getElementDescription(by));
		} catch (Exception e) {
			logger.error("Unable to highlight elemnet using JavaScript", e);
		}
	}

	// =====window and frame handling =====

	// switch to window
	public void switchToWindow(String windowTitle) {
		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().equals(windowTitle)) {
					logger.info("Switched to window: " + windowTitle);
					return;
				}
			}
			logger.warn("Window with title " + windowTitle + "not found ");
		} catch (Exception e) {
			logger.error("Unable to switch to window", e);
		}
	}

	// switch to iframe
	public void switchToFrame(By by) {
		try {
			driver.switchTo().frame(driver.findElement(by));
			logger.info("Switched to iframe: " + getElementDescription(by));
		} catch (Exception e) {
			logger.error("Unable to switch to iframe", e);
		}
	}

	// method to switch back to default content
	public void switchToDefaultContent(By by) {
		driver.switchTo().defaultContent();
		logger.info("Switched back to default content");
	}

	// ====================Alert Handling==============================

	// method to accept alert popup
	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
			logger.info("Alert Accepted");
		} catch (Exception e) {
			logger.info("No alert found to accept", e);
		}
	}

	// method to dismiss alert popup
	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
			logger.info("Alert dismissed");
		} catch (Exception e) {
			logger.info("No alert found to dismiss", e);
		}
	}

	// method to get alert text
	public String getALertText() {
		try {
			return driver.switchTo().alert().getText();
		} catch (Exception e) {
			logger.info("No alert text found", e);
			return "";
		}
	}

	// ========================Browser actions=======================
	public void refreshPage() {
		try {
			driver.navigate().refresh();
			ExtentManager.logStep("Page refreshed scuccessfully");
			logger.info("Page refreshed scuccessfully");
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to refresh page", "refresh uncessfully");
			logger.error("Unable to refresh page: " + e.getMessage());
		}
	}

	public String getCurrentURL() {
		try {
			String url = driver.getCurrentUrl();
			ExtentManager.logStep("Current url fetched: " + url);
			logger.info("Current url fetched: " + url);
			return url;
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to fetch current URL",
					"Unable to fetch current URL");
			logger.error("Unable to fetch current URL: " + e.getMessage());
			return null;
		}
	}

	public void maximiseWindow() {
		try {
			driver.manage().window().maximize();
			ExtentManager.logStep("Browser window maximised");
			logger.info("Browser window maximised");
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to maximise window", "Unable to maximise window");
			logger.error("Unable to maximise window: " + e.getMessage());
		}
	}

	// =============Advanced WebElement Action==============
	public void moveToElelemnt(By by) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(driver.findElement(by)).perform();
			ExtentManager.logStep("Moved to element: " + elementDescription);
			logger.info("Moved to element --> " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to move to element",
					"Unable to move to element" + elementDescription);
			logger.error("Unable to move to element: " + e.getMessage());
		}
	}

	public void dragAndDrop(By source, By target) {
		String sourceDesc = getElementDescription(source);
		String tragetDesc = getElementDescription(target);
		try {
			Actions actions = new Actions(driver);
			actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).perform();
			ExtentManager.logStep("Dragged element: " + sourceDesc + "and dropped on " + tragetDesc);
			logger.info("Dragged element: " + sourceDesc + "and dropped on " + tragetDesc);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to drag and drop",
					"Unable to drag from " + sourceDesc + "and drop on " + tragetDesc);
			logger.error("Unable to drag and drop: " + e.getMessage());
		}
	}

	public void doubleClick(By by) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.doubleClick(driver.findElement(by)).perform();
			ExtentManager.logStep("Double clicked on element: " + elementDescription);
			logger.info("Double clicked on element --> " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to move to element",
					"Unable to doubleclick on element" + elementDescription);
			logger.error("Unable to doubleclick on element: " + e.getMessage());
		}
	}

	public void rightClick(By by) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.contextClick(driver.findElement(by)).perform();
			ExtentManager.logStep("Right clicked on element: " + elementDescription);
			logger.info("Right clicked on element --> " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to move to element",
					"Unable to rightclick on element" + elementDescription);
			logger.error("Unable to rightclick on element: " + e.getMessage());
		}
	}

	public void sendKeysWithAction(By by, String value) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.sendKeys(driver.findElement(by), value).perform();
			ExtentManager.logStep("Sendkeys to element: " + elementDescription + "| Value: " + value);
			logger.info("Sendkeys to element --> " + elementDescription + "| Value: " + value);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to sendkeys to element", elementDescription);
			logger.error("Unable to sendkeys to element: " + e.getMessage());
		}
	}

	public void clearText(By by) {
		String elementDescription = getElementDescription(by);
		try {
			driver.findElement(by).clear();
			ExtentManager.logStep("Cleared text in element: " + elementDescription);
			logger.info("Cleared text in element --> " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to clear text in element ", elementDescription);
			logger.error("Unable to clear text in element: " + e.getMessage());
		}
	}

	// method to upload a file
	public void uploadFile(By by, String filePath) {
		try {
			driver.findElement(by).sendKeys(filePath);
			applyBorder(by, "green");
			logger.info("Uploaded file: " + filePath);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.info("Unable to upload file: " + e.getMessage());
		}
	}

}
