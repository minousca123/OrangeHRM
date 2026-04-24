package listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import base.BaseClass;
import utilities.ExtentManager;
import utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer {

	/*
	 * TestNG provides several listener interfaces. Some common ones are:
	 * ITestListener: Handles events for individual test methods (start, success,
	 * fail, skip). ISuiteListener: Handles events for the entiresuite (start,
	 * finish). IInvokedMethodListener: Handles events before/after each method is
	 * invoked. IAnnotationTransformer: Modifies annotations at runtime (e.g., to
	 * add retry logic). IReporter Generates custom reports after execution.
	 */

	// triggered when test starts
	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		// start logging in extent report
		ExtentManager.startTest(testName);
		ExtentManager.logStep("TestStarted" + testName);
	}

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

	// Triggered when test succeeds
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();

		if (result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logStepValidationAPI("Test End: " + testName + " - ✔ Test Passed");
		} else {
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "TestPassed Successfully!",
					"Test End: " + testName + " - ✔ Test Passed");
		}
	}

	// triggered when test fails
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);

		if (result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logFailureAPI("Test End: " + testName + " - ❌ Test Failed");
		} else {
			ExtentManager.logFailure(BaseClass.getDriver(), "TestPassed Failed!",
					"Test End: " + testName + " - ❌ Test Failed");
		}
	}

	// Triggered when a test skips
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test Skipped" + testName);
	}

	// triggered when suite starts
	@Override
	public void onStart(ITestContext context) {
		// initialize extent reports
		ExtentManager.getReporter();
	}

	// Triggered when suite ends
	@Override
	public void onFinish(ITestContext context) {
		ExtentManager.flushReport();
	}

}
