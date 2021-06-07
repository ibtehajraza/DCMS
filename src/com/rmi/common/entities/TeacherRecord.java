/**
 * 
 */
package com.rmi.common.entities;

import java.io.Serializable;

import com.rmi.common.Records;

/**
 * @author ibtehajraza
 *
 */
public class TeacherRecord extends Records implements Serializable {

	/**
	 * Generated a serial version to maintain the integrity of the de-serialized
	 * object.
	 */
	private static final long serialVersionUID = -2022244860032958323L;

	public String address;
	
	public String phone;
	
	// Assuming each teacher has one specialization
	public String specialization;
	
	public String location;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
