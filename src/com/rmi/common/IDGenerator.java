package com.rmi.common;

public class IDGenerator {
	
	public synchronized String getId(boolean isStudent, int id)
	{
		//int temp = getFile(isStudent);
		String ans = "";
		if(isStudent)
			ans = "SR";
		else ans = "TR";
		ans += String.format("%05d", id);
		return ans;
	}
}