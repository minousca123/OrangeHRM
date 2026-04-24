package test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import base.BaseClass;
import utilities.ExtentManager;

public class TC01_DummyTest extends BaseClass{
	
	@Test
	public void dummyTest() {
		String title = getDriver().getTitle();
		ExtentManager.logStep(" Veryfying title");
		assert title.contentEquals("OrangeHRM"):"Test Failed - Title not matching";
		
		System.out.println("Test Passed - Title matching");
		ExtentManager.logSkip("This case is skipped");
		//throw new SkipException("Skipping the test as part of testing");
		
		
	}

}
