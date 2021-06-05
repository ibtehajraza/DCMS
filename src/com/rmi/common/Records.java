/**
 * 
 */
package com.rmi.common;

/**
 * @author ibtehajraza
 *
 */
public abstract class Records {

	public String id = "";
	public String firstName = "";
	public String lastName = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
