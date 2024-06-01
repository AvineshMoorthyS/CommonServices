package com.blog.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LogManagement {

	public void updateLog(String textToAddInFile) {

		LocalDateTime currentTime = LocalDateTime.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedTime = currentTime.format(formatter);
		
		String filePath = "D:\\PetCare\\serverlog.txt";

        try {
        	// Create a File object to represent the file
            File file = new File(filePath);

            // Check if the file already exists
            if (!file.exists()) {
                // Create a FileWriter with the specified file path
                FileWriter fileWriter = new FileWriter(file);

                // Create a BufferedWriter to write efficiently
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                // Write the text to the file
                bufferedWriter.write(formattedTime+textToAddInFile);

                // Close the writer to free up resources
                bufferedWriter.close();

                System.out.println("New file created and text has been added successfully.");
            } else {
                // Append the text to the existing file
                FileWriter fileWriter = new FileWriter(file, true); // true for append mode
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(formattedTime+textToAddInFile);
                bufferedWriter.newLine(); // Add a new line after appending text
                bufferedWriter.close();

                System.out.println("Text has been added to the existing file successfully.");
            }
        } catch (IOException e) {
            System.err.println("Error occurred while writing to the file: " + e.getMessage());
        }
		
	}
	
}
