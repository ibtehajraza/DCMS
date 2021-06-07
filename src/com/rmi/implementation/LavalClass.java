package com.rmi.implementation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import com.rmi.common.CenterServer;

public class LavalClass extends UnicastRemoteObject implements CenterServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 471941797268559074L;

	private int serverPortMTL = 6666;
	private int serverPortLVL = 8888;
	private int serverPortDDO = 7777;
	
	
	// Default constructor.
	public LavalClass() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean createTRecord(String managerID, String firstName, String lastName, String address, String phone, String specialization,
			String location) throws RemoteException {
		// TODO Auto-generated method stub

		System.out.println("Hi I am going to create a teacher record for you In Laval.");
		return true;
	}

	@Override
	public boolean createSRecord(String managerID, String firstName, String lastName, String[] courseRegistered, boolean status,
			Date statusDate) throws RemoteException {
		// TODO Auto-generated method stub

		System.out.println("Hi I am going to create a student record for you.");
		return false;
	}

	@Override
	public int getRecordCounts() throws RemoteException {
		// TODO Auto-generated method stub

		System.out.println("I will return the count for you.");
		return 0;
	}
	
	@Override
	public String findItem(String userID, String itemName) throws RemoteException {

		System.out.println("Starting FindItem Laval");
		
		String itemList = "No items are available";
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
		String userPrefix = userID.substring(0, Math.min(userID.length(), 3)).toUpperCase();

//		if (userPrefix.equals("mcg")) {
//
//			String resultMon = sendMessage(mtlServerPort, "getID", userID, itemName, null, 0);
//			String resultCon = sendMessage(lvlServerPort, "getID", userID, itemName, null, 0);
//			itemList = itemList + resultMon + resultCon;
//			
//		} else 

		if (userPrefix.equals("LVL")) {

			//String resultMcgill = sendMessage(ddoServerPort, "getID", userID, itemName, null);
			String resultMTL = sendMessage(serverPortMTL, "getID", userID, itemName, null);
			//itemList = itemList + resultMcgill + resultCon;
			
			System.out.println(resultMTL);
		}
//		} else if (userPrefix.equals("con")) {
//		
//			String resultMcgill = sendMessage(ddoServerPort, "getID", userID, itemName, null, 0);
//			String resultMon = sendMessage(mtlServerPort, "getID", userID, itemName, null, 0);
//
//			itemList = itemList + resultMon + resultMcgill;
//		}

		return itemList;
	}
	
	
	private static String sendMessage(int serverPort,String function,String userID,String itemName, String itemId) {
		
		DatagramSocket aSocket = null;
		String result ="";
		String dataFromClient = function+";"+userID+";"+itemName+";"+itemId;
		
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

}
