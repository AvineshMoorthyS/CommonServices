package com.blog.event;

import java.util.ArrayList;
import java.util.HashMap;
import com.blog.demo.CustomHashMap;
import java.util.Map;

public class User {
  
	public static void sumOfTwoNumbers(CustomHashMap map) {
		
		String methodName = "sumOfTwoNumbers";
		
		try {
			map.put("FinalMethodReached", "sumOfTwoNumbers");
			int result = map.getInt("Num1") + map.getInt("Num2");
			CustomHashMap map1 = new CustomHashMap();
			map1.put("SummaOruValue", "1");
			map1.put("SummaRenduValue", 2);
			map.put("SummaOruMap", map1);
			ArrayList list1 = new ArrayList();
			list1.add(map1);
			map.put("SumaaOruList", list1);
			map.put("Result", result);
		}catch(Exception e) {
			map.clear();
        	map.put("MethodName", methodName);
            map.put("Error",  e.getMessage());
            map.put("ErrorMessage",  e.getLocalizedMessage());
            map.put("ErrorReason",  e.getCause());
            e.printStackTrace();
		}
	}
	
}
