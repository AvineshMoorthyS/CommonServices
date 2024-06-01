package com.blog.demo;

import java.io.StringReader;

public class JsonStructure {

	public static CustomHashMap filterJsonData(CustomHashMap jsonData) {
		
		String keys = jsonData.getString("ResponseJsonData");
        String[] keysArray = keys.split(",");
        CustomHashMap filteredJson = new CustomHashMap<>();
        for (String key : keysArray) {
            key = key.trim();
            if (jsonData.containsKey(key)) {
            	filteredJson.put(key, jsonData.get(key));
            }
        }
        return filteredJson;
    }
	
public static CustomHashMap filterJsonData(CustomHashMap jsonData, String keys) {
		
        String[] keysArray = keys.split(",");
        CustomHashMap filteredJson = new CustomHashMap<>();
        for (String key : keysArray) {
            key = key.trim();
            if (jsonData.containsKey(key)) {
            	filteredJson.put(key, jsonData.get(key));
            }
        }
        return filteredJson;
    }
	
}
