package com.rmi.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.rmi.implementation.DDOClass;
import com.rmi.implementation.LavalClass;
import com.rmi.implementation.MontrealClass;

public class LavalServer {

	public static void main(String args[]) {

		try {

			LavalClass stub = new LavalClass();

			Registry registry = LocateRegistry.createRegistry(2964);

			registry.bind("CenterServer", stub);

			System.out.println("Laval Server is up.");

			Runnable task = () -> {
				receive(stub);
			};
			Thread thread = new Thread(task);
			thread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void receive(LavalClass stub) {

		DatagramSocket aSocket = null;
		String sendingResult = "";

		try {

			aSocket = new DatagramSocket(8888);

			byte[] buffer = new byte[1000];

			System.out.println("Laval UDP Server 8888 Started............");

			while (true) {

				DatagramPacket request = new DatagramPacket(buffer, buffer.length);

				aSocket.receive(request);

				String sentence = new String(request.getData(), 0, request.getLength());

				String[] parts = sentence.split(";");
				String function = parts[0];
				String userID = parts[1];
				//String itemName = parts[2];
//				String itemId = parts[3];

				System.out.println(sentence);

				switch (function) {
				case "GetRecordCount":

					System.out.println("Found something in LAVAL");
					sendingResult = String.valueOf(stub.getRecordCounts(userID));
					
					break;

				case "getTeacherID":

					sendingResult = String.valueOf(LavalClass.teacherCount);

					break;
				
				case "getStudentID":
					
					sendingResult = String.valueOf(LavalClass.studentCount);
					
					break;
					
				default:
					sendingResult = "0";
					break;
				}
				

				//sendingResult = "this si a response;";

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
