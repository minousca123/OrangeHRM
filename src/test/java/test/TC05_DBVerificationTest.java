package test;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseClass;
import pages.HomePage;
import pages.LoginPage;
import utilities.DBConnection;
import utilities.DataProviders;
import utilities.ExtentManager;

public class TC05_DBVerificationTest extends BaseClass{
	
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	

	@Test(dataProvider="empVerification",  dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameFromDB(String empID, String empName) {
		
		SoftAssert softAssert = getSoftAssert();
		
		ExtentManager.logStep("Logging with credentials");
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));

		ExtentManager.logStep("Click on PIM tab");
		homePage.clickPIMTab();
		
		ExtentManager.logStep(" Search for employee");
		homePage.employeeSearch(empName);
		
		ExtentManager.logStep("Get the Employeename from DB");
		String employee_id=empID;
		
		//fetch the data to Map
		Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);
		String emplFirstName = employeeDetails.get("firstname");
		String emplMiddleName = employeeDetails.get("middlename");
		String emplLastName = employeeDetails.get("lastname");
		
		//use trim if middle nam eis blank
		String empFirstAndMiddleName = (emplFirstName +" " + emplMiddleName).trim();
		
		
		//validation for first and middle name
		ExtentManager.logStep("Verify employee first and middle name");
		softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(empFirstAndMiddleName),"First and Middle name are not matching");
		
		//validation for last name
		ExtentManager.logStep("Verify employee last name");
		softAssert.assertTrue(homePage.verifyEmployeeLastName(emplLastName),"Last name is not matching");
		
		ExtentManager.logStep("DB validation completed");
		
		softAssert.assertAll();
	
	}
	
}
