package com.rmi.implementation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
import com.rmi.common.IDGenerator;
import com.rmi.common.Records;

public class DDOClass extends UnicastRemoteObject implements CenterServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 471941797268559074L;

	public static int teacherCount = 0;
	public static int studentCount = 0;

	private int serverPortMTL = 6666;
	private int serverPortLVL = 8888;
	private int serverPortDDO = 7777;
	
	ConcurrentHashMap<String, List<Records>> ddoRecords;
	
	
	// Default constructor.
	public DDOClass() throws RemoteException {
		super();
		
		this.ddoRecords = new ConcurrentHashMap<String, List<Records>>();

		List<Records> records = new ArrayList<Records>();

		Records teacherRecord = new Records();

		teacherRecord.setId("TR60000");
		teacherRecord.setFirstName("Angila");
		teacherRecord.setLastName("Beans");
		teacherRecord.setAddress("322 rue de la montag");
		teacherRecord.setPhone("090078610");
		teacherRecord.setSpecialization("French");
		teacherRecord.setLocation("ddo");

		records.add(teacherRecord);
		teacherCount++;

		teacherRecord = new Records();

		teacherRecord.setId("TR60001");
		teacherRecord.setFirstName("Michal");
		teacherRecord.setLastName("Scoot");
		teacherRecord.setAddress("321 rue de la montag");
		teacherRecord.setPhone("090078611");
		teacherRecord.setSpecialization("Math");
		teacherRecord.setLocation("ddo");

		records.add(teacherRecord);
		teacherCount++;
		
		teacherRecord = new Records();

		teacherRecord.setId("TR60003");
		teacherRecord.setFirstName("Jim");
		teacherRecord.setLastName("Hallpert");
		teacherRecord.setAddress("322 rue de la montag");
		teacherRecord.setPhone("090078612");
		teacherRecord.setSpecialization("Science");
		teacherRecord.setLocation("ddo");

		records.add(teacherRecord);
		teacherCount++;

		addListToHashmap(records);
		
	}

	@Override
	public boolean createTRecord(String managerID, String firstName, String lastName, String address, String phone,
			String specialization, String location) throws RemoteException {

		String method = "createTRecord()";

		Records teacherRecord = new Records();

		int idCount = 0 ;
		try {
			idCount = Integer.parseInt(findItem(managerID, "getTeacherID").trim());
		} catch (NumberFormatException e1) {
			System.out.println("Integer parssing error");
		}

		String id = new IDGenerator().getId(false, idCount);
		
		teacherRecord.setId(id);
		teacherRecord.setFirstName(firstName);
		teacherRecord.setLastName(lastName);
		teacherRecord.setAddress(address);
		teacherRecord.setPhone(phone);
		teacherRecord.setSpecialization(specialization);
		teacherRecord.setLocation(location);

		boolean status = addItemToHashmap(teacherRecord);
		
		if(status) {
			teacherCount++;
		}

		try {

			serverLogCreate(managerID, method, "Teacher Record Added: " + status, status ? "Success" : "Failure",
					String.format(
							"[id: %s], [FirstName: %s], [LastName: %s], [Address: %s], [phone:%s], [specialization: %s],[Location: %s]",
							id, firstName, lastName, address, phone, specialization, location));

		} catch (Exception e) {
			System.out.println("\n\nUnable to add logging for edit record.");
		}

		System.out.println("create a teacher record in DDO center. Status: " + status);

		return status;
	}

	@Override
	public boolean createSRecord(String managerID, String firstName, String lastName, List<String> courseRegistered,
			boolean status, Date statusDate) throws RemoteException {

		String method = "createSRecord()";

		Records studentRecord = new Records();

		int idCount = 0 ;
		try {
			idCount = Integer.parseInt(findItem(managerID, "getStudentID").trim());
		} catch (NumberFormatException e1) {
			System.out.println("Integer parssing error");
		}

		String id = new IDGenerator().getId(true, idCount);
		
		studentRecord.setId(id);
		studentRecord.setFirstName(firstName);
		studentRecord.setLastName(lastName);
		studentRecord.setCoursesRegistered(courseRegistered);
		studentRecord.setStatus(status);
		studentRecord.setStatusDate(statusDate);
		
		boolean result = addItemToHashmap(studentRecord);
		
		if(status) {
			studentCount++;
		}
		
		try {
			
			String courses = "";
			
			for(String course : courseRegistered) {
				courses += course + " ";
			}

			serverLogCreate(managerID, method, "Student Record Added: " + status, status ? "Success" : "Failure",
					String.format(
							"[id: %s], [FirstName: %s], [LastName: %s], [status: %s], [setStatusDate: %s], [courseRegistered: %s]",
							id, firstName, lastName, String.valueOf(status), statusDate.toString(), courses));

		} catch (Exception e) {
			System.out.println("\n\nUnable to add logging for edit record.");
		}

		System.out.println("created a student record in DDO center. Status: " + status);
		
		return result;
	}

	@Override
	public int getRecordCounts(String managerID) throws RemoteException {

		System.out.println("\nReturning Record Count for DDO Server\n\n");

		int count = 0;

		try {
			for (List<Records> val : ddoRecords.values())
				count += val.size();

			serverLogCreate(managerID, "getRecordCounts()", "DDO count: " + count, "success",
					"Total number of records: " + count);

			System.out.println("\nRecord Count for DDO Server is ["+ count +"]\n\n");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return count;
	}
	
	@Override
	public String findItem(String managerID, String functionName) throws RemoteException {

		String itemList = "No items are available";

		//System.out.println("Starting FindItem DDO for function: " + functionName);

		String serverPrefix = managerID.substring(0, Math.min(managerID.length(), 3)).toUpperCase();

		System.out.println(serverPrefix);

		if (serverPrefix.equals("DDO")) {

			if (functionName.equalsIgnoreCase("GetRecordCount")) {
				String resultMTL = sendMessage(serverPortMTL, functionName, managerID);
				String resultLVL = sendMessage(serverPortLVL, functionName, managerID);
				int resultDDO = getRecordCounts(managerID);
				itemList = "MTL: " + resultMTL + ", LVL: " + resultLVL + ", DDO: " + resultDDO;
			}
			
			if(functionName.equalsIgnoreCase("getTeacherID")) {
				
				String resultMTL = sendMessage(serverPortMTL, functionName, managerID);
				String resultLVL = sendMessage(serverPortLVL, functionName, managerID);
				
					
				int mtlCount;
				try {
					mtlCount = Integer.parseInt(resultMTL.trim());
				} catch (NumberFormatException e) {
					mtlCount = 0;
				}
				
				int lvlCount;
				try {
					lvlCount = Integer.parseInt(resultLVL.trim());
				} catch (NumberFormatException e) {
					lvlCount = 0;
				}
				
				System.out.println(mtlCount+", "+ lvlCount);
				
				int sum = lvlCount + mtlCount + DDOClass.teacherCount;
				
				itemList = String.valueOf(sum);
				
			}
			
			if(functionName.equalsIgnoreCase("getStudentID")) {
				
				String resultMTL = sendMessage(serverPortMTL, functionName, managerID);
				String resultLVL = sendMessage(serverPortLVL, functionName, managerID);
				
					
				int mtlCount;
				try {
					mtlCount = Integer.parseInt(resultMTL.trim());
				} catch (NumberFormatException e) {
					mtlCount = 0;
				}
				
				int lvlCount;
				try {
					lvlCount = Integer.parseInt(resultLVL.trim());
				} catch (NumberFormatException e) {
					lvlCount = 0;
				}
				
				System.out.println(mtlCount+", "+ lvlCount);
				
				int sum = lvlCount + mtlCount + DDOClass.studentCount;
				
				itemList = String.valueOf(sum);
				
			}

		}

		return itemList;
	}
	
	@Override
	public boolean editRecord(String recordId, String fieldName, String newValue) throws RemoteException {

		boolean status = false;
		
		//System.out.println("I will edit the record for you.");

		System.out.println(recordId + " " + fieldName + " " + newValue);

		// boolean val = montrealRecords.editRecord(recordId, fieldName, newValue);

		for (List<Records> val : ddoRecords.values()) {

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

				if (ddoRecords.containsKey(key)) {

					// Updating the list
					ddoRecords.get(key).add(record);

				} else {

					// Creating a new list
					List<Records> list = new ArrayList<Records>();

					list.add(record);

					// Creating a new entry in the hashmap
					ddoRecords.put(key, list);
				}

			}
		}

	}

	private boolean addItemToHashmap(Records record) {

		boolean status = false;

		if (record != null) {

			String key = String.valueOf(record.getLastName().charAt(0));

			if (ddoRecords.containsKey(key)) {

				// Updating the list
				ddoRecords.get(key).add(record);

				status = true;

			} else {

				// Creating a new list
				List<Records> list = new ArrayList<Records>();

				list.add(record);

				// Creating a new entry in the hashmap
				ddoRecords.put(key, list);

				status = true;
			}

		}

		return status;
	}
	
	private static String sendMessage(int serverPort,String function,String userID) {
		
		DatagramSocket aSocket = null;
		String result ="";
		String dataFromClient = function+";"+userID ;
		
		System.out.println(dataFromClient + ": DDO  ");
		
		try {
		
			aSocket = new DatagramSocket();
			byte[] message = dataFromClient.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");
			DatagramPacket request = new DatagramPacket(message, dataFromClient.length(), aHost, serverPort);
			
			aSocket.setSoTimeout(10000);
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
			
			//e.printStackTrace();
			System.out.println("IO: " + e.getMessage());
			
			result = e.getLocalizedMessage();
		
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
