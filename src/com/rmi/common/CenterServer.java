package com.rmi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface CenterServer extends Remote {

	HashMap<String, List<Records>> lists = new HashMap<>();

	boolean createTRecord( String managerID, String firstName, String lastName, String address, String phone, String specialization,
			String location) throws RemoteException;

	boolean createSRecord(String managerID, String firstName, String lastName, String[] courseRegistered, boolean status, Date statusDate)
			throws RemoteException;

	int getRecordCounts() throws RemoteException;
	
	public String findItem(String userID, String itemName) throws RemoteException;

}
