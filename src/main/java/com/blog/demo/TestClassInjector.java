package com.blog.demo;

import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;

public class TestClassInjector {

	public static void main(String args[]) {
		
		CustomHashMap lhm = new CustomHashMap();
		lhm.put("Key", "value");
		ArrayList al = new ArrayList();
		al.add(lhm);
		for(int  i =0;i<al.size();i++) {
			CustomHashMap chm = new CustomHashMap();
			chm = (CustomHashMap)al.get(i);
			System.out.println(chm);
		}
		System.out.println();
		DBManagement dbm = new DBManagement();
//		dbm.setData("SaveAdditionDetails", "1", jsonData, "OwnerConnection");
//		dbm.sampleSaver();
	}
	
}