package com.rmi.client;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.rmi.common.CenterServer;

public class ManagerClient {

	public static void main(String args[]) {

		startSystem();

	}

	private static Scanner scanner = new Scanner(System.in);

	// Initiating client site program
	private static void startSystem() {

		System.out.println("Enter your username: \n");

		// System.out.println("You are phone number is " +
		// getIntFieldFromUser("phone",""));

		// Scanner scanner = new Scanner(System.in);

		String username = scanner.nextLine().toUpperCase();


		// System.out.println(isManagerIDValid(username));

		// scanner.close();

		if (!isManagerIDValid(username)) {
			System.out.println("\n\nWrong ID please enter a correct Manager ID.\n");
			startSystem();
			return;
		}

		System.out.println("\n\nYou are logging in as " + username);

		String accessParameter = username.substring(0, 3);

		System.out.println("\n\nLog in successful your primary center is " + accessParameter);

		try {
			managerActions(username);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void managerActions(String username) throws Exception {
		int serverPort = decideServerport(username);

		if (serverPort == 1) {
			return;
		}

		try {
			Registry registry = LocateRegistry.getRegistry(serverPort);

			createLog(username, "Start Session ", "Session Started successfully");
			
			System.out.println("\n\n .:: Main Menu ::. ");
			
			System.out.println("\n 1. Create Teacher's Record \n " + "2. Create Student Record \n "
					+ "3. Get Total Record's Count \n " + "4. Edit a Record \n " + "5. End Session\n\n");

			System.out.println("Select the option you want to do: ");
			// System.out.println("Port # " + serverPort);

			// Scanner scanner = new Scanner(System.in);

			String menuSelection = scanner.nextLine();

			CenterServer obj = (CenterServer) registry.lookup("CenterServer");

			if (menuSelection.equals("1")) {

				boolean status = createTeacherRecord(username, obj);

				createLog(username, "Create Teacher's Record | status: ", (status) ? "Successful" : "Failure");

				System.out.println("Create Teacher's Record | status: " + ((status) ? "Successful" : "Failure"));

				managerActions(username);

			} else if (menuSelection.equals("2")) {

				createStudentRecord(username, obj);

				managerActions(username);

			} else if (menuSelection.equals("3")) {

				System.out.println("Get Total Record's Count ");

				getTotalRecordCount(username, obj);

				managerActions(username);

			} else if (menuSelection.equals("4")) {

				editRecord(username, obj);

				managerActions(username);

			} else if (menuSelection.equals("5")) {

				createLog(username, "End Session ", "Session ended successfully");

				System.out.println("Session ended successfully ");

				startSystem();
			} else {

				System.out.println("Invalid option selected. ");
				managerActions(username);
			}
		} catch (AccessException e) {

			System.out.println("\n\nAccess voilation! Please try again.");

		} catch (RemoteException e) {

			System.out.println("\n\nServer is down! Please run the servers first.");

		} catch (NotBoundException e) {
			
			System.out.println("\n\nNo assosiated binding found. Please contact the support.");
			
		} catch (IOException e) {

			System.out.println("\n\nUnable to add logging.");
			
		} catch (Exception e) {

			System.out.println("\n\nGot an error please try again.");
			
		}

	}

	// Business functions:

	private static boolean createTeacherRecord(String username, CenterServer obj) {

		boolean status = false;

		try {

			String firstName = getFieldFromUser("FirstName");
			String lastName = getFieldFromUser("LastName");
			String address = getFieldFromUser("address");
			String location = getFieldFromUser("Location");
			String phoneNumber = getPhoneNumberFromUser("phone Number");
			String specialization = getFieldFromUser("Specialization");

			System.out.println(String.format(
					"\n\nFirstName: %s, LastName: %s, address: %s, location: %s, phone: %s, Specialization: %s",
					firstName, lastName, address, location, phoneNumber, specialization));

			createLog(username, "createTeacherRecord ", String.format(
					"\n\nFirstName: %s, LastName: %s, address: %s, location: %s, phone: %s, Specialization: %s",
					firstName, lastName, address, location, phoneNumber, specialization));

			status = obj.createTRecord(username, firstName, lastName, address, phoneNumber, specialization, location);

		} catch (RemoteException e) {
			System.out.println("[Error: "+e.getLocalizedMessage()+"");
			
		} catch (IOException e) {
			System.out.println("Unable to create logs");
		}

		return status;
	}

	private static boolean createStudentRecord(String username, CenterServer obj) {

		boolean result = false;

		String firstName = getFieldFromUser("FirstName");
		String lastName = getFieldFromUser("LastName");
		String courses = getFieldFromUser("comma\",\" separated Courses");

		String courseArr[] = courses.split(",");

		//System.out.println("courseArr != null && courseArr.length > 0: " + (courseArr != null && courseArr.length > 0));
		//System.out.println("courseArr.length: " + courseArr.length);

		// Looping for the courses till we get the valid values.

		List<String> courseList = new ArrayList<String>();

		boolean loop = true;
		do {
			if (courseArr != null && courseArr.length > 0) {

				for (String course : courseArr) {
					courseList.add(course);
				}

				loop = false;
			} else {
				System.out.println("\n\nPlease Enter a Valid Value\n\n");
				courses = getFieldFromUser("comma\",\" separated courses");
			}
		} while (loop);

		// End

		try {

			String status = getFieldFromUser("Status (T,F): T => True and F => False");

			loop = true;
			do {
				if (status != null && status.equalsIgnoreCase("T") || status.equalsIgnoreCase("F")) {

					loop = false;
				} else {
					System.out.println("\n\nPlease Enter a Valid Value\n\n");
					status = getFieldFromUser("Status (T,F): T => True and F => False");
				}
			} while (loop);

			String statusDate = getFieldFromUser("Status Date: dd/MM/yyyy. e.g 12/01/2021");

			Date date = new SimpleDateFormat("dd/MM/yyyy").parse(statusDate);

			System.out
					.println(String.format("\n\nFirstName: %s, LastName: %s, Courses: [%s], Status: %s, statusDate: %s",
							firstName, lastName, courses, status, statusDate));

			createLog(username, "createStudentRecord ", String.format("\n\nFirstName: %s, LastName: %s, Courses: [%s], Status: %s, statusDate: %s",
					firstName, lastName, courses, status, statusDate));
			
//			CenterServer obj = (CenterServer) registry.lookup("CenterServer");

			result = obj.createSRecord(username, firstName, lastName, courseList,
					(status.equalsIgnoreCase("T") ? true : false), date);

			createLog(username, "Create Student's Record | status: ", (result) ? "Successful" : "Failure");

			System.out.println("Create Student's Record | status: " + ((result) ? "Successful" : "Failure"));

		} catch (ParseException e) {

			System.out.println("\n\nInvalid date. Please try entering correct date.\n\n");
		} catch (Exception e) {

			System.out.println("Got an error please try again.");
		}

		return result;
	}

	private static void getTotalRecordCount(String username, CenterServer obj) throws IOException {

		// Getting records from all the servers.
		String result = obj.findItem(username, "GetRecordCount");

		createLog(username, "Get Total Record's Count: ", "Total Records: [ " + result + " ]");

		System.out.println("Total Records: [ " + result + " ]\n\n");

	}

	private static void editRecord(String username, CenterServer obj) throws IOException {

		String recordID = getFieldFromUser("record Id to edit");

		boolean isrecordIDValid = validateRecordID(recordID);

		if (isrecordIDValid) {

			// CenterServer obj = (CenterServer) registry.lookup("CenterServer");

			String accessParameter = recordID.substring(0, 2);

			System.out.println("\n .:: Record Edit Menu::.\n");

			if (accessParameter.equalsIgnoreCase("TR")) {

				// Teacher's Menu
				System.out.println(" 1 - Teacher's Address");
				System.out.println(" 2 - Teacher's Phone Number");
				System.out.println(" 3 - Teacher's Location\n");

				String option = scanner.nextLine();

				String fieldName = "";
				String newValue = "";

				switch (option.toUpperCase()) {
				case "1":

					fieldName = "address";

					newValue = getFieldFromUser("Address");

					break;

				case "2":

					fieldName = "Phone";

					newValue = getFieldFromUser("Phone Number");

					break;

				case "3":

					fieldName = "Location";

					newValue = getFieldFromUser("Location");

					break;

				default:
					System.out.println("\n\nInvalid entry!\n");
					break;
				}

				System.out.println(String.format("\nRecord Id: %s, field Name: %s, New Value: %s", recordID, fieldName,
						newValue));

				boolean result = obj.editRecord(recordID, fieldName, newValue);

				createLog(username, "Edit a Record ",
						String.format("Record updated for a teacher [ ID:%s, FieldName: %s, NewValue: %s, Status: %s ]",
								recordID, fieldName, newValue, String.valueOf(result)));

				System.out.println("Edit Record status: " + result);
			}

			if (accessParameter.equalsIgnoreCase("SR")) {

				// Student's Menu
				System.out.println("\n 1 - Student's Registered Courses\n");
				System.out.println("\n 2 - Student's Status (T,F)\n");
				System.out.println("\n 3 - Student's Status Date (dd/MM/yyyy)\n");

				String option = scanner.nextLine();

				String fieldName = "";
				String newValue = "";

				switch (option.toUpperCase()) {

				case "1":

					fieldName = "COURSES";

					String courses = getFieldFromUser("comma \",\" separated Courses");

					String courseArr[] = courses.split(",");

					System.out.println("courseArr != null && courseArr.length > 0: "
							+ (courseArr != null && courseArr.length > 0));
					System.out.println("courseArr.length: " + courseArr.length);

					// Looping for the courses till we get the valid values.

					// List<String> courseList = new ArrayList<String>();

					boolean loop = true;
					do {
						if (courseArr != null && courseArr.length > 0) {

							loop = false;
						} else {
							System.out.println("\n\nPlease Enter a Valid Value\n\n");
							courses = getFieldFromUser("comma\",\" separated courses");
						}
					} while (loop);

					newValue = courses;

					// End

					break;

				case "2":

					fieldName = "STATUS";

					String status = getFieldFromUser("Status (T,F): T => True and F => False");

					loop = true;
					do {
						if (status != null && status.equalsIgnoreCase("T") || status.equalsIgnoreCase("F")) {

							loop = false;
						} else {
							System.out.println("\n\nPlease Enter a Valid Value\n\n");
							status = getFieldFromUser("Status (T,F): T => True and F => False");
						}
					} while (loop);

					newValue = status;

					break;

				case "3":

					fieldName = "STATUSDATE";

					String statusDate = getFieldFromUser("Status Date: dd/MM/yyyy. e.g 12/01/2021");
					try {

						Date date = new SimpleDateFormat("dd/MM/yyyy").parse(statusDate);

						newValue = getFieldFromUser("statusDate");

					} catch (ParseException e) {
						System.out.println("\n\n Invalid date. Please try again with the correct formate.");
					}

					break;

				default:
					System.out.println("\n\nInvalid entry!\n");
					break;
				}

				System.out.println(String.format("\n\record Id: %s, field Name: %s, New Value: %s", recordID, fieldName,
						newValue));

				boolean result = obj.editRecord(recordID, fieldName, newValue);

				createLog(username, "Edit a Record ",
						String.format(
								"Record updated for a student [ ID: %s, FieldName: %s, NewValue: %s, Status: %s ]",
								recordID, fieldName, newValue, String.valueOf(result)));

				System.out.println("Edit Record status: " + result);

			}

		} else {

			System.out.println("Invalid Record ID. Please try again.");
		}
	}
	// End

	private static int decideServerport(String username) {

		int serverPort = 1;

		String serverDirection = username.substring(0, 3);

		if (serverDirection.equalsIgnoreCase("LVL")) {
			serverPort = 2964;

		} else if (serverDirection.equalsIgnoreCase("MTL")) {
			serverPort = 2965;

		} else if (serverDirection.equalsIgnoreCase("DDO")) {
			serverPort = 2966;

		} else {

			System.out.println("This is an invalid request. Please check your username");
			startSystem();
		}

		return serverPort;
	}

	private static String getFieldFromUser(String fieldName) {

		System.out.println("\nEnter " + fieldName + ": \n");

		String field = scanner.nextLine().toUpperCase();

		return field;
	}

	private static String getPhoneNumberFromUser(String fieldName) {

		System.out.println("\nEnter " + fieldName + ": ");

		boolean isValid = false;

		String field;

		do {

			field = scanner.nextLine();

			try {

				Integer.parseInt(field);

				isValid = true;

			} catch (Exception ex) {

				System.out.println("\nPlease enter a valid phone number.\n");

			}

		} while (!isValid);

		return field;
	}

//	private static String[] getArrayOfValuesFromUser(String fieldName) {
//		
//		System.out.println("Please enter the number of ");
//		
//	}

	private static boolean isManagerIDValid(String id) {

		boolean isValid = false;

		try {

			isValid = Pattern.matches("[MTLVOD]{3}[0-9]{4}", id); // This will check if the format is correct or not

			if (isValid) {

				String accessParameter = id.substring(0, 3);

				// This is to validate the location of the centers.
				if (accessParameter.equalsIgnoreCase("MTL") || accessParameter.equalsIgnoreCase("LVL")
						|| accessParameter.equalsIgnoreCase("DDO")) {

					isValid = true;

				} else {

					isValid = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isValid;
	}

	private static boolean validateRecordID(String id) {

		boolean isValid = false;

		try {

			isValid = Pattern.matches("[TSR]{2}[0-9]{5}", id); // This will check if the format is correct or not

			if (isValid) {

				String accessParameter = id.substring(0, 2);

				// This is to validate the location of the centers.
				if (accessParameter.equalsIgnoreCase("TR") || accessParameter.equalsIgnoreCase("SR")) {

					isValid = true;

				} else {

					isValid = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isValid;
	}

	private static void createLog(String id, String action, String response) throws IOException {

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
}
