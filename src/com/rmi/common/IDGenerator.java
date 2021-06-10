package com.rmi.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class IDGenerator {
	private int data;
	
	public IDGenerator(){data = 0;}
	
	public int getFile(boolean isStudent)
	{
		int value = -1;
		try {
			  File idMemory;
			  if(isStudent)
				  idMemory = new File("studentMemory.txt");
			  else idMemory = new File("teacherMemory.txt");
			  
		      if (idMemory.createNewFile()) {
		        //Write 0
		    	 FileWriter textWriter;
		    	 if(isStudent)
					  textWriter = new FileWriter("studentMemory.txt");
				  else textWriter = new FileWriter("teacherMemory.txt");
		    	 
		    	 textWriter.write("0");
		    	 textWriter.close();
		      } 
		     
		      //Increase value 
		      Scanner textReader = new Scanner(idMemory);
		      while (textReader.hasNextInt()) {
		        int data = textReader.nextInt();
		        value = data;
				data++;
				
			
				 FileWriter textWriter;
		    	 if(isStudent)
					  textWriter = new FileWriter("studentMemory.txt");
				  else textWriter = new FileWriter("teacherMemory.txt");
		    	 
		    	textWriter.write(Integer.toString(data));
		    	textWriter.close();
		    	//textReader.close();
		    	
		      }
		     
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	
		return value;
	}
	
	public synchronized String getId(boolean isStudent)
	{
		int temp = getFile(isStudent);
		String ans = "";
		if(isStudent)
			ans = "SR";
		else ans = "TR";
		ans += String.format("%05d", temp);
		return ans;
	}
}