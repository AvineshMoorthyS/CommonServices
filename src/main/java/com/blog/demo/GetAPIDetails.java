package com.blog.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

public class GetAPIDetails {

	private static String JDBC_URL = "jdbc:mysql://localhost:3306/ownerdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    
    public static void getAPIDetails(CustomHashMap apiDetailMap) {
    	
		String methodName = "getAPIDetails";
		System.out.println("Inside getData method");
		try {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
		
		if (connection != null) {
            System.out.println("Connected to the database!");

            Statement statement = connection.createStatement();

            String sqlQuery = "SELECT API_NAME AS 'ApiName', CLASS_NAME AS 'ClassName', PACKAGE_NAME AS 'PackageName', METHOD_NAME AS 'MethodName' FROM API_DETAIL WHERE API_NAME = \""+apiDetailMap.getString("ApiName")+"\"";
            System.out.println(sqlQuery);
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                	String columnName = metaData.getColumnLabel(i);
                	String data = resultSet.getString(i);
                	apiDetailMap.put(columnName, data);
                }
               }

            resultSet.close();
            statement.close();

            connection.close();
        	}
		}catch(Exception e) {
//			headerMap.put("MethodName", methodName);
//			headerMap.put("ErrorMessage", e.getMessage());
		}
    }
	
}
