package com.blog.event;

import java.util.HashMap;
import com.blog.demo.CustomHashMap;


public class Subtraction {
	public static void subOfTwoNumbers(CustomHashMap map) throws Exception {
		
		String methodName = "subOfTwoNumbers";
		
		try {
			int result = map.getInt("Num1") - map.getInt("Num2");
			map.put("Result", result);
			map.put("FinalMethodReached", "subOfTwoNumbers");

		}catch(Exception e) {
			map.clear();
        	map.put("MethodName", methodName);
            map.put("Error",  e.getMessage());
            map.put("ErrorMessage",  e.getLocalizedMessage());
            map.put("ErrorReason",  e.getMessage());
            e.printStackTrace();
		}
	}
}
