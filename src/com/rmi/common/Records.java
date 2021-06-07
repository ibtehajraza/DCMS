/**
 * 
 */
package com.rmi.common;

import java.util.Date;
import java.util.List;

/**
 * @author ibtehajraza
 *
 */
public class Records {

	private String id;

	private String firstName;

	private String lastName;

	private String address;

	private String phone;

	private String specialization;

	private String location;

	// For Student
	private List<String> coursesRegistered; // For Example maths/french/science

	private boolean status; // active/inactive

	private Date statusDate; // date when student became active inactive

	public Records() {

	}

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

	public List<String> getCoursesRegistered() {
		return coursesRegistered;
	}

	public void setCoursesRegistered(List<String> coursesRegistered) {
		this.coursesRegistered = coursesRegistered;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	@Override
	public String toString() {
		return "Records [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", address=" + address
				+ ", phone=" + phone + ", specialization=" + specialization + ", location=" + location
				+ ", coursesRegistered=" + coursesRegistered + ", status=" + status + ", statusDate=" + statusDate
				+ "]";
	}

}
