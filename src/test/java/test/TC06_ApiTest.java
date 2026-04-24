package test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.response.Response;
import utilities.ApiUtility;
import utilities.ExtentManager;
import utilities.RetryAnalyzer;

public class TC06_ApiTest {

	// no need to extend bse class as we donot have to initialise driver
	@Test
	public void verifyGetUserAPI() {

		SoftAssert softAssert = new SoftAssert();

		// Step1: Define API Endpoint
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint: " + endPoint);

		// Step2: Send GET Request
		ExtentManager.logStep("Sending GET Request to the API");
		Response response = ApiUtility.sendGetRequest(endPoint);

		// Step3: validate status code
		ExtentManager.logStep("Validating API Response status code");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);
		softAssert.assertTrue(isStatusCodeValid, "Status code is not as Epxected");
		if (isStatusCodeValid) {
			ExtentManager.logStepValidationAPI("Status Code Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Status Code Validation Failed!");
		}
		// Step4: validate user name
		ExtentManager.logStep("Validating response body for username");
		String userName = ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		softAssert.assertTrue(isUserNameValid, "Username is not valid");
		if (isUserNameValid) {
			ExtentManager.logStepValidationAPI("Username Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Username Validation Failed!");
		}
		// Step4: validate email
		ExtentManager.logStep("Validating response body for email");
		String userEmail = ApiUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
		softAssert.assertTrue(isEmailValid, "Email is not valid");
		if (isEmailValid) {
			ExtentManager.logStepValidationAPI("Email Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Email Validation Failed!");
		}
		softAssert.assertAll();
	}
}
