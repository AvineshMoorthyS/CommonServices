package com.blog.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import com.mysql.cj.jdbc.Driver;
import com.mysql.cj.jdbc.result.ResultSetImpl;

public class DBManagement {

	private static String JDBC_URL = "jdbc:mysql://localhost:3306/${connectionName}";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private static final String BOLD = "\u001B[1m";
    private static final String RESET_BOLD = "\u001B[0m";
    	
	public void getData(String dataGetterName, String dataGetterID, CustomHashMap jsonData, String connectionName ) {
		String methodName = "getData";

		try {
		if(connectionName.equals("OwnerConnection")) {
			JDBC_URL = JDBC_URL.replace("${connectionName}", "ownerdb");
		}
		
	    LogManagement logManagement = new LogManagement();
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		CustomHashMap resultMap = new CustomHashMap();
		resultMap.put("DataGetterName", dataGetterName);
		resultMap.put("DataGetterId", dataGetterID);
		resultMap.put("ConnectionName", connectionName);
		Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
		if (connection != null) {
            Statement statement = connection.createStatement();

            String sqlQuery = "SELECT SQL_QUERY AS 'Query', ISMULTIPLEROWSNEEDED AS 'MultiRowNeeded', COLLECTION_NAME AS 'CollectionName' FROM QUERY_DETAIL WHERE QUERY_ID = "+dataGetterID+" AND QUERY_NAME = \""+dataGetterName+"\" AND ACTIVE = 'Y'";
            logManagement.updateLog("Query"+sqlQuery);
            
            jsonData.put("SQL::Query", sqlQuery);
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            logManagement.updateLog("ColumnCount"+columnCount);
            
            while (resultSet.next()) {
            	System.out.println("Inside GetData ResultSet");
                for (int i = 1; i <= columnCount; i++) {
                	System.out.println("Inside for loop");
                	String columnName = metaData.getColumnLabel(i);
                	String data = resultSet.getString(i);
                	resultMap.put(columnName, data);
                }
            }
            
            if(jsonData.getString("LogDBInformation").equals("Y")) {
            	resultMap.put("LogDBInformation", "Y");
            	if(!jsonData.getString("QueryName").equals("") && !jsonData.getString("QueryId").equals("")) {  
            		resultMap.put("QueryName", jsonData.getString("QueryName"));
            		resultMap.put("QueryId", jsonData.getString("QueryId"));
            	}else {
            		throw new Exception("Provide DataGetterName in Key -> \"QueryName\" and Provide DataGetterId in Key -> \"QueryId\"");
            	}
            }           
            
            System.out.println("ResultMap1"+resultMap);
            
            dataFetcher(resultMap, jsonData);
            CustomHashMap resultFromFetcher = new CustomHashMap();
            System.out.println("FetcherResult"+resultMap.get("GetDataOutput"));
            if(!resultMap.get("GetDataOutput").equals("") && resultMap.get("GetDataOutput")!=null) {
            	resultFromFetcher = (CustomHashMap)resultMap.get("GetDataOutput");
            }
            else {
            	throw new Exception("Retriver result is empty");
            }
            Iterator<CustomHashMap.Entry<String, Integer>> iterator = resultFromFetcher.entrySet().iterator();
            
            while(iterator.hasNext()) {
                CustomHashMap.Entry<String, Integer> entry = iterator.next();
                System.out.println(BOLD+"KEY"+RESET_BOLD+entry.getKey());
                jsonData.put(entry.getKey(), entry.getValue());
            }
            
            System.out.println(BOLD+"FinalResultSet::"+RESET_BOLD+resultMap);
            logManagement.updateLog("ResultMap"+resultMap);
            jsonData.put("GetDataResult", resultMap);
            System.out.println(jsonData.getString("LogDBInformation"));
            
             
            
            resultSet.close();
            statement.close();
            connection.close();
        }
		}catch(Exception e) {
//			jsonData.clear();
			jsonData.put("MethodName", methodName);
            jsonData.put("Error",  e.getMessage());
            e.printStackTrace();
		}
	}
	
	private static void dataFetcher(CustomHashMap retrieverMap, CustomHashMap inputMap) {
		
		String methodName = "dataFetcher";
		try {
		if(retrieverMap.getString("ConnectionName").equals("OwnerConnection")) {
			JDBC_URL = JDBC_URL.replace("${connectionName}", "ownerdb");
		}
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		CustomHashMap resultMap = new CustomHashMap();
		String finalQuery = "";
		finalQuery = retrieverMap.getString("Query");
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(finalQuery);
        System.out.println("FinalQuery1"+finalQuery);
        while (matcher.find()) {
            String enclosedSubstring = matcher.group(1);
            System.out.println("Enclosed substring: " + enclosedSubstring);
            finalQuery = finalQuery.replace("${"+enclosedSubstring+"}", inputMap.getString(enclosedSubstring));
            
        }
		Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
		if (connection != null) {
			System.out.println("Inside Connection");
            Statement statement = connection.createStatement();   
            ResultSet resultSet = statement.executeQuery(finalQuery);
            System.out.println("FinalQuery"+finalQuery);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if(!retrieverMap.getString("MultiRowNeeded").equals("Y")) {
            	while (resultSet.next()) {
            		System.out.println("TestLine148");
                	for (int i = 1; i <= columnCount; i++) {
                		String columnName = metaData.getColumnLabel(i);
                		String data = resultSet.getString(i);
                		resultMap.put(columnName, data);
                	}
                }
            }
            else {
            	ArrayList apiDetail = new ArrayList();
            	System.out.println("TestLine158");
            	while (resultSet.next()) {
            		CustomHashMap resultSetMap = new CustomHashMap();
                	for (int i = 1; i <= columnCount; i++) {
                		String columnName = metaData.getColumnLabel(i);
                		String data = resultSet.getString(i);
                		resultSetMap.put(columnName, data);
                	}
                	apiDetail.add(resultSetMap);
                }
            	resultMap.put(retrieverMap.getString("CollectionName"), apiDetail);
            }
            
            if(resultMap.getString("LogDBInformation").equals("Y")) {
            	resultMap.put("LogDBInformation", "Y");
            	
            	if(resultMap.getString("QueryName").equals(resultMap.getString("DataGetterName")) && resultMap.getString("QueryId").equals(resultMap.getString("DataGetterId"))) {  
            		resultMap.put("QueryName", resultMap.getString("QueryName"));
            		resultMap.put("QueryId", resultMap.getString("QueryId"));
            		resultMap.put("ResultMap","InputData");
            	}else {
            		throw new Exception("Provide DataGetterName in Key -> \"QueryName\" and Provide DataGetterId in Key -> \"QueryId\"");
            	}
            } 
            
            System.out.println("ResultMap"+resultMap);
            
            retrieverMap.put("GetDataOutput", resultMap);
            resultSet.close();
            statement.close();
            connection.close();
        }
		}catch(Exception e) {
			inputMap.clear();
			inputMap.put("MethodName", methodName);
			inputMap.put("Error",  e.getMessage());
			inputMap.put("ErrorMessage",  e.getLocalizedMessage());
			inputMap.put("ErrorReason",  e.getCause());
            e.printStackTrace();
		}
	}
	
	public static void saveData(String dataSaverName, String dataSaverID, CustomHashMap jsonData, String connectionName) {
		
		String methodName = "saveData";
		try {
		if(connectionName.equals("OwnerConnection")) {
			JDBC_URL = JDBC_URL.replace("${connectionName}", "ownerdb");
		}
		Class.forName("com.mysql.cj.jdbc.Driver");
		CustomHashMap queryDetailMap = new CustomHashMap();
		queryDetailMap.put("ConnectionName", connectionName);
		Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
		if (connection != null) {
            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT SQL_QUERY AS 'Query', MULTIPLE_VALUES AS 'MultipleValues', COLLECTION_NAME AS 'CollectionName' FROM SAVER_QUERY_DETAIL WHERE QUERY_ID = "+dataSaverID+" AND QUERY_NAME = \""+dataSaverName+"\" AND ACTIVE = 'Y'";
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();            
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                	String columnName = metaData.getColumnLabel(i);
                	String data = resultSet.getString(i);
                	queryDetailMap.put(columnName, data);
                }
            }
            jsonData.put("QueryDetails", queryDetailMap);
            dataSaver(queryDetailMap, jsonData);
            if(jsonData.getString("LogDBInformation").equals("Y")) {
            	if(!jsonData.getString("QueryName").equals("") && !jsonData.getString("QueryId").equals("")) { 
            		if(!jsonData.getString("QueryName").equals(dataSaverName) && !jsonData.getString("QueryName").equals(dataSaverID)) {
            			jsonData.remove("QueryDetails");
            		}
            	}else {
            		throw new Exception("Provide DataGetterName in Key -> \"QueryName\" and Provide DataGetterId in Key -> \"QueryId\"");
            	}
            }
            else {
            	jsonData.remove("QueryDetails");
            }
            resultSet.close();
            statement.close();
            connection.close();
        }
		}catch(Exception e) {
			jsonData.clear();
			jsonData.put("MethodName", methodName);
			jsonData.put("ErrorMessage",  e.getMessage());
            e.printStackTrace();
		}
	}

	public static void dataSaver(CustomHashMap queryDetailMap, CustomHashMap jsonData) {
		try {
		 Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
		 	String sqlInsert = queryDetailMap.getString("Query");
		 	if(!queryDetailMap.getString("MultipleValues").equals("Y")) {
               	Pattern inputPattern = Pattern.compile("\\$\\{(.*?)\\}");
            	Matcher inputMatcher = inputPattern.matcher(sqlInsert);
            	int j = 0;
            	while(inputMatcher.find()) {
            		j++;
            		String matchedInput = inputMatcher.group();
            		matchedInput = matchedInput.replace("${", "").replace("}", "");
                    sqlInsert = sqlInsert.replace("${"+matchedInput+"}", "\"" + jsonData.getString(matchedInput) + "\"");
            	}
            	PreparedStatement stmt = conn.prepareStatement(sqlInsert);
            	int rowsUpdated = stmt.executeUpdate(sqlInsert);
            	if(rowsUpdated>0) {
            		jsonData.put(sqlInsert+"QueryStatus", "Updated "+rowsUpdated+" Rows");
            	}
            	else {
            		jsonData.put(sqlInsert+"QueryStatus", "0 rows updated, Check the query or records to be updated");
            	}
            	stmt.close();
			}
            else {
            	String collectionName = queryDetailMap.getString("CollectionName");
            	ArrayList inputCollection = new ArrayList();
            	if(jsonData.get(collectionName) instanceof List) {
            		inputCollection = (ArrayList<CustomHashMap>)jsonData.get(collectionName);
            	}
            	int executedCount = 0;
            	for (int input = 0; input < inputCollection.size(); input++) {
                    CustomHashMap<String, Object> inputValueMap = new CustomHashMap<>();
                    inputValueMap.putAllData(inputCollection.get(input));
                    Pattern variablePattern = Pattern.compile("\\$\\{(.*?)\\}");
                    Matcher variableMatcher = variablePattern.matcher(sqlInsert);
                    String processedSqlInsert = sqlInsert;  // Create a copy of the original SQL template

                    while (variableMatcher.find()) {
                        String matchedVariable = variableMatcher.group();
                        String variableName = matchedVariable.replace("${", "").replace("}", "");
                        	processedSqlInsert = processedSqlInsert.replace(matchedVariable, "\"" + inputValueMap.getString(variableName) + "\"");
                    }
                    PreparedStatement stmt = conn.prepareStatement(processedSqlInsert);
                    int rowsUpdated = stmt.executeUpdate();

                    if (rowsUpdated > 0 && input == inputCollection.size()-1) {
                    	executedCount++;
                    	jsonData.put(processedSqlInsert + " QueryStatus", "Updated "+ executedCount + " Rows");
                    }
                    else if(rowsUpdated > 0 ){
                        executedCount++;
                    }
                    else {
                        jsonData.put(processedSqlInsert + " QueryStatus", "0 rows updated, Check the query or records to be updated");
                    }
                    stmt.close();
                }
            }
	        conn.close();
		}catch(SQLException e){
			jsonData.clear();
			jsonData.put("MethodName", "dataSaver");
			jsonData.put("ErrorMessage", e.getMessage());
		}
	}
}