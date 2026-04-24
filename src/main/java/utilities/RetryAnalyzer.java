package utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{

	private int retryCount = 0; //number of retries
	public static final int maxRetryCount = 0; //max no. of retries
	
	@Override
	public boolean retry(ITestResult result) {
		if(retryCount<maxRetryCount) {
			retryCount++;
			return true; //retry the test
		}
		return false;		
	}
}
