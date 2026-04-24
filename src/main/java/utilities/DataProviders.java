package utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
	private static final String File_PATH = System.getProperty("user.dir") + "/src/test/resources/testdata/LoginTestData.xlsx";
	@DataProvider(name="validLoginData")
	public static Object[][] validLoginData(){
		return getSheetData("validLoginData");
	}
	
	@DataProvider(name="inValidLoginData")
	public static Object[][] inValidLoginData(){
		return getSheetData("inValidLoginData");
	}
	
	@DataProvider(name="empVerification")
	public static Object[][] empVerification(){
		return getSheetData("empVerification");
	}
	
	private static Object[][] getSheetData(String sheetName) {
		List<String[]> sheetData = ExcelReaderUtility.getSheetData(File_PATH, sheetName);
		
		Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];
		for(int i=0; i<sheetData.size(); i++) {
			data[i] = sheetData.get(i);
		}
		return data;
	}
	
}
