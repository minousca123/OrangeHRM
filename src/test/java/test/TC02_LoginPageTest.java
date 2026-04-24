package test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.HomePage;
import pages.LoginPage;
import utilities.DataProviders;
import utilities.ExtentManager;

//extend BaseClass to access driver
public class TC02_LoginPageTest extends BaseClass{
	
	//create private variables of pages
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	
	
	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username, String password) {
		ExtentManager.logStep("Navigating to login page. enter username and passowrd");
		loginPage.login(username, password);
		ExtentManager.logStep("Verifying admin tab is visible or not");
		Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after successfull login");
		ExtentManager.logStep("Validation successfully");
		homePage.logout();
		ExtentManager.logStep("Logged out successfully");
	}
	
	@Test(dataProvider="inValidLoginData", dataProviderClass = DataProviders.class)
	public void inValidLoginTest(String username, String password) {
		ExtentManager.logStep("Navigating to login page. enter username and passowrd");
		loginPage.login(username, password);
		String expectedErrorMsg = "Invalid credentials1";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMsg), "Test Failed: Invalid Error Message");
		ExtentManager.logStep("Validation successful");
		
	}

	
}
