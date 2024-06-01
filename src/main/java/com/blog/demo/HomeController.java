package com.blog.demo;

import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.blog.event.User;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class HomeController {
	
	@PostMapping("/webV1")
	public Map processJson(@RequestBody CustomHashMap jsonData, @RequestHeader("APIName") String ApiName) {
//		jsonParser.jsonParser(jsonData);
        String methodName = (String)jsonData.get("MethodName");
		
		try {
			
			jsonData.put("Bold", "\u001B[1m");
			jsonData.put("ResetBold", "\u001B[0m");
			
			GetAPIDetails getApiDetails = new GetAPIDetails();
					
			CustomHashMap apiDetailMap = new CustomHashMap();
			apiDetailMap.put("ApiName", ApiName);
			getApiDetails.getAPIDetails(apiDetailMap);
			
			if(!apiDetailMap.getString("PackageName").equals("") 
					&& !apiDetailMap.getString("ClassName").equals("") 
					&& !apiDetailMap.getString("MethodName").equals("")) {
				Class<?> userClass = Class.forName(apiDetailMap.getString("PackageName")+"."+apiDetailMap.getString("ClassName"));
            	Method method = userClass.getMethod(apiDetailMap.getString("MethodName"), CustomHashMap.class);
            	User myClass = new User();
            	method.invoke(myClass, (CustomHashMap)jsonData);
			}
			else {
				throw new Exception("ApiDetails are not configured properly");
			}
			
			DBManagement dbManagement = new DBManagement();
			dbManagement.saveData("SaveSampleDetails", "2", jsonData, "OwnerConnection");
			
        } catch (Exception e) {
        	jsonData.put("MethodName", methodName);
            jsonData.put("Error:-:Message",  e.getMessage());
            jsonData.put("ErrorMessage",  e.getLocalizedMessage());
            jsonData.put("ErrorReason",  e.getCause());
            e.printStackTrace();
        }
        return jsonData;
    }
	
	@PostMapping("/webV2")
	public CustomHashMap processJsonV2(@RequestBody CustomHashMap jsonData, @RequestHeader("APIName") String ApiName, @RequestHeader("APIId") String ApiId) {
        
        String methodName = (String)jsonData.get("MethodName");
        JsonStructure jsonStructure = new JsonStructure();
		
		try {	
			
			GetAPIDetails getApiDetails = new GetAPIDetails();
			
			CustomHashMap apiDetailMap = new CustomHashMap();
			apiDetailMap.put("ApiName", ApiName);
			apiDetailMap.put("ApiId", ApiId);
			getApiDetails.getAPIDetails(apiDetailMap);
			DBManagement dbManagement = new DBManagement();
			
			System.out.println(apiDetailMap);
			
			if(!apiDetailMap.getString("PackageName").equals("") 
					&& !apiDetailMap.getString("ClassName").equals("") 
					&& !apiDetailMap.getString("MethodName").equals("")) {
				Class<?> userClass = Class.forName(apiDetailMap.getString("PackageName")+"."+apiDetailMap.getString("ClassName"));
            	Method method = userClass.getMethod(apiDetailMap.getString("MethodName"), CustomHashMap.class);
            	User myClass = new User();
            	method.invoke(myClass, (CustomHashMap)jsonData);
			}
			else {
				throw new Exception("ApiDetails are not configured properly");
			}
			
            if(jsonData.containsKey("ErrorMessage") && jsonData.containsKey("ErrorReason")) {
            	String keysToKeep = "Num1,Num2,Result";
            	CustomHashMap filterJson = new CustomHashMap();
            	filterJson.putAll(jsonStructure.filterJsonData((CustomHashMap) jsonData, keysToKeep));
            	jsonData.clear();
            	jsonData.putAll(filterJson);
            }
            else {
            	jsonData.put("ApiName", ApiName);
            	jsonData.put("ApiId", ApiId);
            	dbManagement.getData("GetResponseJsonData", "2", jsonData, "OwnerConnection");
            	jsonStructure.filterJsonData(jsonData);
            }
            
            	System.out.println(jsonData);
            
        } catch (Exception e) {
        	jsonData.put("MethodName", methodName);
            jsonData.put("Error",  e.getMessage());
            jsonData.put("ErrorMessage",  e.getLocalizedMessage());
            jsonData.put("ErrorReason",  e.getCause());
            jsonData.put("APIName", ApiName);
            e.printStackTrace();
        }
        return jsonData;
    }	
}