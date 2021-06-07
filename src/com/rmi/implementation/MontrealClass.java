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

public class MontrealClass extends UnicastRemoteObject implements CenterServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 471941797268559074L;

	private int serverPortMTL = 6666;
	private int serverPortLVL = 8888;
	private int serverPortDDO = 7777;

	// Default constructor.
	public MontrealClass() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean createTRecord(String managerID, String firstName, String lastName, String address, String phone,
			String specialization, String location) throws RemoteException {
		// TODO Auto-generated method stub

		System.out.println("Hi I am going to create a teacher record for you In Montreal.");
		return true;
	}

	@Override
	public boolean createSRecord(String managerID, String firstName, String lastName, String[] courseRegistered,
			boolean status, Date statusDate) throws RemoteException {
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
		String userPrefix = userID.substring(0, Math.min(userID.length(), 3)).toUpperCase();
		
		System.out.println(userPrefix);

		if (userPrefix.equals("LVL")) {

			String resultMon = sendMessage(serverPortMTL, "getID", userID, itemName, null);
			String resultCon = sendMessage(serverPortDDO, "getID", userID, itemName, null);
			itemList = itemList + resultMon + resultCon;
			
		} else if (userPrefix.equals("MTL")) {

			//String resultDDO = sendMessage(ddoServerPort, "getID", userID, itemName, null);
			String resultLVL = sendMessage(serverPortLVL, "getID", userID, itemName, null);
			//itemList = itemList + resultDDO + resultLVL;
			
			System.out.println(resultLVL);
		
		} else if (userPrefix.equals("DDO")) {
		
			String resultMcgill = sendMessage(serverPortLVL, "getID", userID, itemName, null);
			String resultMon = sendMessage(serverPortMTL, "getID", userID, itemName, null);

			itemList = itemList + resultMon + resultMcgill;
		}

		return itemList;
	}
	
	
	private static String sendMessage(int serverPort,String function,String userID,String itemName, String itemId) {
		
		DatagramSocket aSocket = null;
		String result ="";
		String dataFromClient = function+";"+userID+";"+itemName+";"+itemId;
		
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

}
