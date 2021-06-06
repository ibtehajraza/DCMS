package com.rmi.implementation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import com.rmi.common.CenterServer;

public class LavalClass extends UnicastRemoteObject implements CenterServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 471941797268559074L;

	// Default constructor.
	public LavalClass() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean createTRecord(String firstName, String lastName, String address, String phone, String specialization,
			String location) throws RemoteException {
		// TODO Auto-generated method stub

		System.out.println("Hi I am going to create a teacher record for you In Laval.");
		return true;
	}

	@Override
	public boolean createSRecord(String firstName, String lastName, String[] courseRegistered, boolean status,
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

}
