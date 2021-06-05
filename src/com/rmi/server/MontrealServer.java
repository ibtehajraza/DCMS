/**
 * 
 */
package com.rmi.server;

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

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
