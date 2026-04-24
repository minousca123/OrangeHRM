package test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.HomePage;
import pages.LoginPage;
import utilities.DataProviders;
import utilities.ExtentManager;

public class TC03_HomePageTest extends BaseClass{
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	
	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verifyOrgangeHRMLogo(String username, String password) {
		ExtentManager.logStep("Navigating to login page. enter username and passowrd");
		loginPage.login(username, password);

		ExtentManager.logStep("Verifying logo is visible or not");
		Assert.assertTrue(homePage.verifyOrangeHRMLogo(),"Logo is not visible");
		ExtentManager.logStep(" Validation successful");
	
	}
	

}
