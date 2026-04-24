package test;

import org.testng.annotations.Test;

import base.BaseClass;

public class TC04_DummyTest extends BaseClass{
	
	@Test
	public void dummyTest() {
		String title = getDriver().getTitle();
		assert title.contentEquals("OrangeHRM"):"Test Failed - Title not matching";
		
		System.out.println("Test Passed - Title matching");
		
		
	}

}
