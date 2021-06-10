package com.rmi.implementation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.rmi.common.CenterServer;
import com.rmi.common.Records;

public class LavalClass extends UnicastRemoteObject implements CenterServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 471941797268559074L;

	private int serverPortMTL = 6666;
	private int serverPortLVL = 8888;
	private int serverPortDDO = 7777;
	
	ConcurrentHashMap<String, List<Records>> lavalRecords;
	
	
	// Default constructor.
	public LavalClass() throws RemoteException {
		super();
		
		this.lavalRecords = new ConcurrentHashMap<String, List<Records>>();

		List<Records> records = new ArrayList<Records>();

		Records teacherRecord = new Records();

		teacherRecord.setId("TR50000");
		teacherRecord.setFirstName("John");
		teacherRecord.setLastName("Snow");
		teacherRecord.setAddress("340 rue de la montag");
		teacherRecord.setPhone("090078609");
		teacherRecord.setSpecialization("French");
		teacherRecord.setLocation("lvl");

		records.add(teacherRecord);

		teacherRecord = new Records();

		teacherRecord.setId("TR50001");
		teacherRecord.setFirstName("Hunter");
		teacherRecord.setLastName("Kal");
		teacherRecord.setAddress("340 rue de la montag");
		teacherRecord.setPhone("090078607");
		teacherRecord.setSpecialization("Math");
		teacherRecord.setLocation("lvl");

		records.add(teacherRecord);
		
		teacherRecord = new Records();

		teacherRecord.setId("TR50003");
		teacherRecord.setFirstName("Kali");
		teacherRecord.setLastName("Kappor");
		teacherRecord.setAddress("341 rue de la montag");
		teacherRecord.setPhone("090078608");
		teacherRecord.setSpecialization("Math-1");
		teacherRecord.setLocation("lvl");

		records.add(teacherRecord);

		addListToHashmap(records);
		
	}

	@Override
	public boolean createTRecord(String managerID, String firstName, String lastName, String address, String phone, String specialization,
			String location) throws RemoteException {
		// TODO Auto-generated method stub

		System.out.println("Hi I am going to create a teacher record for you In Laval.");
		return true;
	}

	@Override
	public boolean createSRecord(String managerID, String firstName, String lastName, List<String> courseRegistered, boolean status,
			Date statusDate) throws RemoteException {
		// TODO Auto-generated method stub

		System.out.println("Hi I am going to create a student record for you.");
		return false;
	}

	@Override
	public int getRecordCounts(String managerID) throws RemoteException {
		
		System.out.println("I will return the count of LAVAL for you.");

		int size = 0;

		try {

			size = lavalRecords.size();

			createLog(managerID, "getRecordCounts()", "Laval Size: " + size);

			serverLogCreate(managerID, "getRecordCounts()", "Laval Size: " + size, "success",
					"Total number of records: " + size);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return size;
	}
	
	@Override
	public boolean editRecord(String recordId, String fieldName, String newValue) throws RemoteException {

		boolean status = false;
		
		System.out.println("I will edit the record for you.");

		System.out.println(recordId + " " + fieldName + " " + newValue);

		// boolean val = montrealRecords.editRecord(recordId, fieldName, newValue);

		for (List<Records> val : lavalRecords.values()) {

			for (int i = 0; i < val.size(); i++) {

				if (val.get(i).getId().equals(recordId)) {
					
					
					Records record = new Records();
					
					String recordType = record.getId().substring(0, 2);
					
					if(recordType.equalsIgnoreCase("TR")) {
						
						
						if (fieldName.equalsIgnoreCase("adderess")) {
							
							val.get(i).setAddress(newValue);
							
							status = true;
						}
						
						if (fieldName.equalsIgnoreCase("phone")) {
							
							val.get(i).setPhone(newValue);
							
							status = true;
						}
						
						if (fieldName.equalsIgnoreCase("location")) {
							
							switch (newValue.toLowerCase()) {
							case "lvl":
								
							case "mtl":
								
							case "ddo":

								val.get(i).setPhone(newValue.toLowerCase());								
								
								status = true;
								
								break;

							default:
								System.out.println("Wrong input");

								break;
							}
							
							
						}
						
					}else if(recordType.equalsIgnoreCase("SR")) {
						
						if (fieldName.equalsIgnoreCase("STATUS")) {
							
							try {
								
								val.get(i).setStatus(Boolean.valueOf(newValue));
								
								status = true;
							
							} catch (Exception e) {
								System.out.println("Only boolean value is allowed.");
							}
							
						}
						
						
						if (fieldName.equals("COURSE")) {
							
							String courses[] = newValue.split(",");
							
							if(courses!= null && courses.length > 0) {

								List<String> registeredCourse = new ArrayList<String>();
								for(String course : courses) {
									registeredCourse.add(course);
								}
								
								if(registeredCourse!= null && registeredCourse.size() > 0) {

									val.get(i).setCoursesRegistered(registeredCourse);
									
									status = true;
								}
								
							} //End of if(courses!= null && courses.length > 0)
							
						}	// End of fieldName.equals("COURSE")

						if (fieldName.equalsIgnoreCase("STATUSDATE")) {
							
							try {
								
								Date date =new SimpleDateFormat("dd/MM/yyyy").parse(newValue);  
								
								val.get(i).setStatusDate(date);
								
								status = true;
								
							} catch (ParseException e) {
								
								System.out.println("Invalid date");
							}
							
							
						}
						
					}	// End of	recordType.equalsIgnoreCase("SR")
					
				}
			}
		}

		System.out.println("Result of edit: " + status);

		return status;
	}
	
	@Override
	public String findItem(String managerID, String functionName) throws RemoteException {

		String itemList = "No items are available";

		System.out.println("Starting FindItem Montreal");

//		for (Map.Entry<String, Item> entry : itemsMaps.entrySet()) {
//			System.out.println(itemName);
//			String name = entry.getValue().getitemName();
//			if(name.equalsIgnoreCase(itemName)) {
//				String action = "find an Item: "+itemName;
//				try {
//					logCreate(userID, action, entry.toString());
//					serverLogCreate(userID,action, entry.toString(), "Success",  "USER ID: "+userID+"/ Item Name: "+itemName);
//				} catch (IOException e) {
//
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				itemList = entry.toString();
//				
//			}
//
//		}
		String serverPrefix = managerID.substring(0, Math.min(managerID.length(), 3)).toUpperCase();

		System.out.println(serverPrefix);

		if (serverPrefix.equals("LVL")) {

			String resultMTL = sendMessage(serverPortMTL, functionName, managerID);
			//String resultDDO = sendMessage(serverPortDDO, "GetRecordCount", managerID, itemName, null);
			int resultLVL = getRecordCounts(managerID);

			itemList = "MTL: " + resultMTL + ", LVL: " + String.valueOf(resultLVL) + ".";
			//String resultLVL = sendMessage(serverPortLVL, "GetRecordCount", managerID, itemName, null);

		} else if (serverPrefix.equals("MTL")) {

			// String resultDDO = sendMessage(ddoServerPort, "getID", userID, itemName,
			// null);
			
			
			//int resultLVL = getRecordCounts(managerID);
			//String resultMTL = sendMessage(serverPortMTL, "GetRecordCount", managerID, itemName, null);
			// itemList = itemList + resultDDO + resultLVL;

//			itemList = String.valueOf(resultLVL);
			
//			System.out.println(resultMTL);
			System.out.println("In LAVAL Server in MTL condition");

		} else if (serverPrefix.equals("DDO")) {

			System.out.println("In LAVAL Server in DDO condition");
//			String resultMcgill = sendMessage(serverPortLVL, "GetRecordCount", managerID, itemName, null);
//			String resultMon = sendMessage(serverPortMTL, "GetRecordCount", managerID, itemName, null);

//			itemList = itemList + resultMon + resultMcgill;
		}

		return itemList;
	}
		
	private void addListToHashmap(List<Records> records) {

		if (records != null && !records.isEmpty()) {

			for (Records record : records) {

				String key = String.valueOf(record.getLastName().charAt(0));

				if (lavalRecords.containsKey(key)) {

					// Updating the list
					lavalRecords.get(key).add(record);

				} else {

					// Creating a new list
					List<Records> list = new ArrayList<Records>();

					list.add(record);

					// Creating a new entry in the hashmap
					lavalRecords.put(key, list);
				}

			}
		}

	}

	private boolean addItemToHashmap(Records record) {

		boolean status = false;

		if (record != null) {

			String key = String.valueOf(record.getLastName().charAt(0));

			if (lavalRecords.containsKey(key)) {

				// Updating the list
				lavalRecords.get(key).add(record);

				status = true;

			} else {

				// Creating a new list
				List<Records> list = new ArrayList<Records>();

				list.add(record);

				// Creating a new entry in the hashmap
				lavalRecords.put(key, list);

				status = true;
			}

		}

		return status;
	}
	
	private static String sendMessage(int serverPort,String function,String userID/*,String itemName, String itemId*/) {
		
		DatagramSocket aSocket = null;
		String result ="";
		String dataFromClient = function+";"+userID /*+ ";"+itemName+";"+itemId*/;
		
		System.out.println(dataFromClient + ": LVL  ");
		
		try {
		
			aSocket = new DatagramSocket();
			byte[] message = dataFromClient.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");
			DatagramPacket request = new DatagramPacket(message, dataFromClient.length(), aHost, serverPort);
			aSocket.send(request);

			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

			aSocket.receive(reply);
			result = new String(reply.getData());
			String[] parts = result.split(";");
			result = parts[0];
		
		} catch (SocketException e) {
		
			System.out.println("Socket: " + e.getMessage());
		
		} catch (IOException e) {
			
			e.printStackTrace();
			System.out.println("IO: " + e.getMessage());
		
		} finally {
		
			if (aSocket != null)
				aSocket.close();
		}
		
		return result;

	}
	
	private void createLog(String id, String action, String response) throws IOException {

		String serverPrefix = id.substring(0, Math.min(id.length(), 3)).toUpperCase();

		final String dir = System.getProperty("user.dir");

		String fileName = dir;

		if (serverPrefix.equalsIgnoreCase("MTL")) {
			fileName = dir + "/src/Log/Client/Montreal/" + id + ".txt";
		} else if (serverPrefix.equalsIgnoreCase("LVL")) {
			fileName = dir + "/src/Log/Client/Laval/" + id + ".txt";
		} else if (serverPrefix.equalsIgnoreCase("DDO")) {
			fileName = dir + "/src/Log/Client/Dollard_des_Ormeaux/" + id + ".txt";
		}

		Date date = new Date();

		String strDateFormat = "yyyy-MM-dd hh:mm:ss a";

		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

		String formattedDate = dateFormat.format(date);
		
		
		
		FileWriter fileWriter = new FileWriter(fileName, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.println(formattedDate + ": Action: " + action + " | Response: " + response);

		printWriter.close();

	}

	public void serverLogCreate(String id, String action, String response, String requestResult, String peram)
			throws IOException {

		String serverPrefix = id.substring(0, Math.min(id.length(), 3));

		final String dir = System.getProperty("user.dir");

		String fileName = dir;

		if (serverPrefix.equalsIgnoreCase("MTL")) {
			fileName = dir + "/src/Log/Server/Montreal.txt";

		} else if (serverPrefix.equalsIgnoreCase("LVL")) {
			fileName = dir + "/src/Log/Server/Laval.txt";

		} else if (serverPrefix.equalsIgnoreCase("DDO")) {
			fileName = dir + "/src/Log/Server/Dollard_des_Ormeaux.txt";
		}

		Date date = new Date();

		String strDateFormat = "yyyy-MM-dd hh:mm:ss a";

		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

		String formattedDate = dateFormat.format(date);

		FileWriter fileWriter = new FileWriter(fileName, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.println( formattedDate + ": Action: " + action + " | args: " + peram
				+ " | Action Status: " + requestResult + " | Response: " + response);

		printWriter.close();

	}


}
