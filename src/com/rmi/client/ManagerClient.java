package com.rmi.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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

		System.out.println("Enter your username: ");

		// System.out.println("You are phone number is " +
		// getIntFieldFromUser("phone",""));

		// Scanner scanner = new Scanner(System.in);

		String username = scanner.nextLine().toUpperCase();

		System.out.println("You are loging as " + username);

		// System.out.println(isManagerIDValid(username));

		// scanner.close();

		if (!isManagerIDValid(username)) {
			System.out.println("Wrong ID please enter a correct Manager ID.");
			startSystem();
			return;
		}

		String accessParameter = username.substring(0, 3);

		System.out.println("Log in successful your primary center is " + accessParameter);

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

		Registry registry = LocateRegistry.getRegistry(serverPort);

		System.out.println(" 1. Create Teacher's Record \n " + "2. Create Student Record \n "
				+ "3. Get Total Record's Count \n " + "4. Edit a Record \n " + "5. End Session");

		System.out.println("Select the option you want to do: ");
		System.out.println("Port # " + serverPort);

		// Scanner scanner = new Scanner(System.in);

		String menuSelection = scanner.nextLine();

		if (menuSelection.equals("1")) {

			String firstName = getFieldFromUser("FirstName");
			String lastName = getFieldFromUser("LastName");
			String address = getFieldFromUser("address");
			String location = getFieldFromUser("Location");
			String phoneNumber = getPhoneNumberFromUser("phone Number");
			String specialization = getFieldFromUser("Specialization");

			System.out.println(String.format("\n\nFirstName: %s, LastName: %s, add: %s, loc: %s, phon: %s, spec: %s",
					firstName, lastName, address, location, phoneNumber, specialization));

			CenterServer obj = (CenterServer) registry.lookup("CenterServer");

			boolean status = obj.createTRecord(firstName, lastName, address, phoneNumber, specialization, location);

			System.out.println("Create Teacher's Record Created status: " + status);

			managerActions(username);

		} else if (menuSelection.equals("2")) {

			// AddItemInterface obj = (AddItemInterface) registry.lookup("AddItem");
			// boolean n = obj.removeItem(username, itemId,itemQty);
			System.out.println("Create Student Record");

			managerActions(username);
		} else if (menuSelection.equals("3")) {
			System.out.println("Get Total Record's Count ");

			managerActions(username);
		} else if (menuSelection.equals("4")) {
			System.out.println("Edit a Record ");

			managerActions(username);
		} else if (menuSelection.equals("5")) {

			System.out.println("Session ended successfully ");

			startSystem();
		} else {

			System.out.println("Invalid option selected. ");
			managerActions(username);
		}

	}

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

		System.out.println("\nEnter " + fieldName + ": ");

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

			isValid = Pattern.matches("[MTLVOD]{3}[0-9]{5}", id); // This will check if the format is correct or not

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

}
