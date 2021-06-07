/**
 * 
 */
package com.rmi.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.rmi.common.Records;

/**
 * @author ibtehajraza
 *
 */

public class Data {

	
	
	
	public List<Records> createTeacherRecords(){
		
		List<Records> records = new ArrayList<>();

		String firstName[] = {"Lillian","Yaseen","Keziah","Izzy","Clodagh"};
		String lastName[] = {"Penn","Oconnell","Rahman","Proctor","Nash"};
		String specialization[] = {"french","math","english","science","german"};
		
		for(int i = 0 ; i < 5 ; i++) {

			Records teacherRecord = new Records();
			
			teacherRecord.setId("TR "+(i+1)+"0000");
			
			teacherRecord.setFirstName(firstName[i]);
			
			teacherRecord.setLastName(lastName[i]);
			
			teacherRecord.setAddress("35"+(i+2)+" rue de la montag");
			
			teacherRecord.setPhone("09007860"+i);
			
			teacherRecord.setSpecialization(specialization[i]);
			
			teacherRecord.setLocation("mtl");
			
			records.add(teacherRecord);
			
		}
		
		
		return records;
		
	}

}
