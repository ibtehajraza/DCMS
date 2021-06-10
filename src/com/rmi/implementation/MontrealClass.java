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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.rmi.common.CenterServer;
import com.rmi.common.IDGenerator;
import com.rmi.common.Records;
import com.rmi.common.entities.TeacherRecord;

public class MontrealClass extends UnicastRemoteObject implements CenterServer {

	private static final long serialVersionUID = 471941797268559074L;

	private int serverPortMTL = 6666;
	private int serverPortLVL = 8888;
	private int serverPortDDO = 7777;

	ConcurrentHashMap<String, List<Records>> montrealRecords;

	// Default constructor.
	public MontrealClass() throws RemoteException {
		super();

		// Creating some seed records.

		this.montrealRecords = new ConcurrentHashMap<String, List<Records>>();

		List<Records> records = new ArrayList<Records>();

		Records teacherRecord = new Records();

		teacherRecord.setId("TR10000");
		teacherRecord.setFirstName("Ali");
		teacherRecord.setLastName("Ajmal");
		teacherRecord.setAddress("355 rue de la montag");
		teacherRecord.setPhone("090078601");
		teacherRecord.setSpecialization("French");
		teacherRecord.setLocation("mtl");

		records.add(teacherRecord);

		teacherRecord = new Records();

		teacherRecord.setId("TR20000");
		teacherRecord.setFirstName("Ajmal");
		teacherRecord.setLastName("Mulla");
		teacherRecord.setAddress("356 rue de la montag");
		teacherRecord.setPhone("090078602");
		teacherRecord.setSpecialization("Math");
		teacherRecord.setLocation("lvl");

		records.add(teacherRecord);

		teacherRecord = new Records();

		teacherRecord.setId("TR10001");
		teacherRecord.setFirstName("Kamal");
		teacherRecord.setLastName("Abdul");
		teacherRecord.setAddress("376 rue de la montage");
		teacherRecord.setPhone("090078605");
		teacherRecord.setSpecialization("English");
		teacherRecord.setLocation("ddo");

		records.add(teacherRecord);

		addListToHashmap(records);

	}

	static long longID = 10;

	@Override
	public boolean createTRecord(String managerID, String firstName, String lastName, String address, String phone,
			String specialization, String location) throws RemoteException {

		String method = "createTRecord()";

		Records teacherRecord = new Records();

		//String id = String.format("TR%05d", longID);
		String id = new IDGenerator().getId(false);

		teacherRecord.setId(id);
		teacherRecord.setFirstName(firstName);
		teacherRecord.setLastName(lastName);
		teacherRecord.setAddress(address);
		teacherRecord.setPhone(phone);
		teacherRecord.setSpecialization(specialization);
		teacherRecord.setLocation(location);

		boolean status = addItemToHashmap(teacherRecord);

		try {

			// createLog(managerID, method, "Add teacher record: " + status);

			serverLogCreate(managerID, method, "Teacher Record Added: " + status, status ? "Success" : "Failure",
					String.format(
							"[id: %s], [FirstName: %s], [LastName: %s], [Address: %s], [phone:%s], [specialization: %s],[Location: %s]",
							id, firstName, lastName, address, phone, specialization, location));

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("create a teacher record for you In Montreal Status: " + status);

		return status;
	}

	@Override
	public boolean createSRecord(String managerID, String firstName, String lastName, List<String> courseRegistered,
			boolean status, Date statusDate) throws RemoteException {

		String method = "createSRecord()";

		Records studentRecord = new Records();

		//String id = String.format("TR%05d", longID);
		String id = new IDGenerator().getId(true);
		
		studentRecord.setId(id);
		studentRecord.setFirstName(firstName);
		studentRecord.setLastName(lastName);
		studentRecord.setCoursesRegistered(courseRegistered);
		studentRecord.setStatus(status);
		studentRecord.setStatusDate(statusDate);
		
		boolean result = addItemToHashmap(studentRecord);
		
		try {

			serverLogCreate(managerID, method, "Teacher Record Added: " + status, status ? "Success" : "Failure",
					String.format(
							"[id: %s], [FirstName: %s], [LastName: %s], [status: %s], [setStatusDate:%s], [courseRegistered: %s]",
							id, firstName, lastName, String.valueOf(status), statusDate.toString(), courseRegistered.toArray().toString()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("created a teacher record in Montreal. Status: " + status);
		
		return result;
	}

	@Override
	public int getRecordCounts(String managerID) throws RemoteException {

		System.out.println("\nReturning Record Count for Montreal Server\n\n");

		int count = 0;

		try {
			for (List<Records> val : montrealRecords.values())
				count += val.size();

			serverLogCreate(managerID, "getRecordCounts()", "Montreal Size: " + count, "success",
					"Total number of records: " + count);

			System.out.println("\nRecord Count for Montreal Server is ["+ count +"]\n\n");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return count;
	}

	@Override
	public String findItem(String managerID, String functionName) throws RemoteException {

		String itemList = "No items are available";

		//System.out.println("Starting FindItem Montreal for function: " + functionName);

		String serverPrefix = managerID.substring(0, Math.min(managerID.length(), 3)).toUpperCase();

		System.out.println(serverPrefix);

		if (serverPrefix.equals("MTL")) {

			// String resultDDO = sendMessage(ddoServerPort, "getID", userID, itemName,
			// null);

			int resultMTL = getRecordCounts(managerID);
			String resultLVL = sendMessage(serverPortLVL, functionName, managerID);
			// itemList = itemList + resultDDO + resultLVL;

			System.out.println(resultMTL);
			System.out.println(resultLVL);

			// int count = resultMTL + Integer.parseInt(resultLVL);

			itemList = "MTL: " + String.valueOf(resultMTL) + ", LVL: " + resultLVL ;

		}

		return itemList;
	}

	@Override
	public boolean editRecord(String recordId, String fieldName, String newValue) throws RemoteException {

		boolean status = false;
		
		System.out.println("I will edit the record for you.");

		System.out.println(recordId + " " + fieldName + " " + newValue);

		// boolean val = montrealRecords.editRecord(recordId, fieldName, newValue);

		for (List<Records> val : montrealRecords.values()) {

			for (int i = 0; i < val.size(); i++) {

				if (val.get(i).getId().equals(recordId)) {
					
					
					Records record = val.get(i);
					
					String recordType = record.getId().substring(0, 2);
					
					if(recordType.equalsIgnoreCase("TR")) {
						
						
						if (fieldName.equalsIgnoreCase("address")) {
							
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
						
						
						if (fieldName.equalsIgnoreCase("COURSES")) {
							
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
								
								Date date = new SimpleDateFormat("dd/MM/yyyy").parse(newValue);
								
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

		
		try {
			serverLogCreate(recordId, "EditRecord", (status)?"Success":"Failure",
					("Result of edit:"  + status), 
					String.format("[fieldName: %s, newValue: %s]", fieldName,newValue));
		} catch (IOException e) {

			System.out.println("\n\nUnable to add logging for edit record.");
		}
		
		System.out.println("Result of edit: " + status);

		return status;
	}

	private void addListToHashmap(List<Records> records) {

		if (records != null && !records.isEmpty()) {

			for (Records record : records) {

				String key = String.valueOf(record.getLastName().charAt(0));

				if (montrealRecords.containsKey(key)) {

					// Updating the list
					montrealRecords.get(key).add(record);

				} else {

					// Creating a new list
					List<Records> list = new ArrayList<Records>();

					list.add(record);

					// Creating a new entry in the hashmap
					montrealRecords.put(key, list);
				}

			}
		}

	}

	private boolean addItemToHashmap(Records record) {

		boolean status = false;

		if (record != null) {

			String key = String.valueOf(record.getLastName().charAt(0));

			if (montrealRecords.containsKey(key)) {

				// Updating the list
				montrealRecords.get(key).add(record);

				status = true;

			} else {

				// Creating a new list
				List<Records> list = new ArrayList<Records>();

				list.add(record);

				// Creating a new entry in the hashmap
				montrealRecords.put(key, list);

				status = true;
			}

		}

		return status;
	}

	private static String sendMessage(int serverPort, String function,
			String userID/* , String itemName, String itemId */) {

		DatagramSocket aSocket = null;
		String result = "";
		String dataFromClient = function + ";" + userID /* + ";" + itemName + ";" + itemId */;

		System.out.println(dataFromClient + ": MTL  ");

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
		}else {
			fileName = dir + "/src/Log/Server/Montreal.txt";			
		}

		Date date = new Date();

		String strDateFormat = "yyyy-MM-dd hh:mm:ss a";

		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

		String formattedDate = dateFormat.format(date);

		FileWriter fileWriter = new FileWriter(fileName, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.println(formattedDate + ": Action: " + action + " | args: " + peram + " | Action Status: "
				+ requestResult + " | Response: " + response);

		printWriter.close();

	}

//	public static boolean hostAvailabilityCheck() { 
//		
//	    try (Socket s = new Socket(SERVER_ADDRESS, TCP_SERVER_PORT)) {
//	        return true;
//	    } catch (IOException ex) {
//	        /* ignore */
//	    }
//	    return false;
//	}
	
}
