package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import actiondriver.ActionDriver;
import base.BaseClass;

/*
 * separate test logic from UI structure
 */

public class LoginPage {

	private ActionDriver actionDriver;
	
	//Define locators using By class
	private By userNameField = By.name("username");
	private By passwordField = By.cssSelector("input[type='password']");
	private By loginBtn = By.xpath("//button[text()=' Login ']");
	private By errorMessage = By.xpath("//p[text() = 'Invalid credentials']");
	
	//create constructor. Initialize the action driver object by passing webdriver instance
	public LoginPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	//Method to login
	public void login(String userName, String password) {
		actionDriver.enterText(userNameField, userName);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginBtn);
		System.out.println("clicked");
	}
	
	//method to check if error message is displayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
		
	}
	
	//method to get the text from error message
	public String getErrorMessageText() {
		return actionDriver.getText(errorMessage);
	}
	
	//verify if error is correct or not
	public boolean verifyErrorMessage(String expectedError) {
		return actionDriver.compareText(errorMessage, expectedError);
	}
	
}
