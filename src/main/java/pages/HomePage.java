package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import actiondriver.ActionDriver;
import base.BaseClass;

public class HomePage {
	
	private ActionDriver actionDriver;
	
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIDBtn = By.className("oxd-userdropdown-name");
	private By logoutBtn = By.xpath("//a[text()='Logout']");
	private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");
	private By PIMTab = By.xpath("//span[text()='PIM']");
	private By employeeSearch = By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
	private By searchBtn = By.xpath("//button[@type='submit']");
	private By empFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
	private By empLastName = By.xpath("//div[@class='oxd-table-card']/div/div[4]");
	
	
	
	//Initialise the action driver object by passing webdriver insatnce
	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	//Methodto verify if Admin tab is visible
	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTab);
	}
	
	public boolean verifyOrangeHRMLogo() {
		return actionDriver.isDisplayed(orangeHRMLogo);
	}
	
	//method to navigate to PIM tab
	public void clickPIMTab() {
		actionDriver.click(PIMTab);
	}
	
	//employee search
	public void employeeSearch(String value) {
		actionDriver.enterText(employeeSearch, value);
		actionDriver.click(searchBtn);
		actionDriver.scrollToElement(empFirstAndMiddleName);
	}
	
	//verify employee first and middle name
	public boolean verifyEmployeeFirstAndMiddleName(String empFirstAndMiddleNameFromDB) {
		return actionDriver.compareText(empFirstAndMiddleName, empFirstAndMiddleNameFromDB);
		
	}
	
	//verify employee last name
	public boolean verifyEmployeeLastName(String empFLastNameFromDB) {
		return actionDriver.compareText(empLastName, empFLastNameFromDB);
		
	}
	
	//Method to perform logout operation
	public void logout() {
		actionDriver.click(userIDBtn);
		actionDriver.click(logoutBtn);
	}
}
