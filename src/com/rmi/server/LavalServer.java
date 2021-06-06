package com.rmi.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.rmi.implementation.LavalClass;

public class LavalServer {

	public static void main(String args[]) {

		try {

			LavalClass stub = new LavalClass();

			Registry registry = LocateRegistry.createRegistry(2964);

			registry.bind("CenterServer", stub);

			System.out.println("Laval Server is up.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
