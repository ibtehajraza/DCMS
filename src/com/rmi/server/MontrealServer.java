/**
 * 
 */
package com.rmi.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import com.rmi.implementation.MontrealClass;

public class MontrealServer {

	// Registering this server on the registry

	public static void main(String args[]) {

		try {

			MontrealClass stub = new MontrealClass();

			Registry registry = LocateRegistry.createRegistry(2965);

			registry.bind("CenterServer", stub);

			System.out.println("Montral Server is up.");

			Runnable task = () -> {
				receive(stub);
			};
			Thread thread = new Thread(task);
			thread.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void receive(MontrealClass stub) {
		
		DatagramSocket aSocket = null;
		String sendingResult = "";
		
		try {
			
			aSocket = new DatagramSocket(6666);
			
			byte[] buffer = new byte[1000];
			
			System.out.println("Montreal UDP Server 6666 Started............");
			
			while (true) {
				
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				
				aSocket.receive(request);
				
				String sentence = new String( request.getData(), 0,
						request.getLength() );
				
//				String[] parts = sentence.split(";");
//				String function = parts[0];
//				String userID = parts[1];
//				String itemName = parts[2];
//				String itemId = parts[3];
				
				System.out.println("Found something in MONTREAL");
				System.out.println(sentence);
				
				sendingResult= "this si a response;";
				
				byte[] sendData = sendingResult.getBytes();
				DatagramPacket reply = new DatagramPacket(sendData, sendingResult.length(), request.getAddress(),
						request.getPort());
				aSocket.send(reply);
			}
			
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
		
		
	}

}
