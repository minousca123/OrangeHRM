package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import base.BaseClass;

public class DBConnection {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/orangehrm";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "";
	public static final Logger logger = BaseClass.logger;
	
	public static Connection getDBConnection() {
		try {
			logger.info("Starting DB Connection");
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			logger.info("DB COnnection Successful");
			return conn;
		} catch (SQLException e) {
			logger.error("Error while establishing DB Connection");
			e.printStackTrace();
			return null;
		}
	}

	// get employee details from db and store in map
	// return as Map so we can store data as key value pair
	public static Map<String, String> getEmployeeDetails(String employee_id) {
		String query = "Select emp_firstname, emp_middle_name, emp_lastname from hs_hr_employee where employee_id="
				+ employee_id;

		Map<String, String> employeeDetails = new HashMap<>();

		try (Connection conn = getDBConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			logger.info("Executing query: " + query);
			if (rs.next()) {
				String firstname = rs.getString("emp_firstname");
				String middlename = rs.getString("emp_middle_name");
				String lastname = rs.getString("emp_lastname");

				// store in a map
				employeeDetails.put("firstname", firstname);
				employeeDetails.put("middlename", middlename!=null? middlename: "");
				employeeDetails.put("lastname", lastname);

				logger.info("Query Executed Successfully");
				logger.info("Employee Data Fetched" +employeeDetails);
			} else {
				logger.error("Employee not found");
			}
		} catch (Exception e) {
			logger.error("Error while executing query");
			e.printStackTrace();
		}
		return employeeDetails;

	}

}
