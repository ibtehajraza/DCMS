/**
 * 
 */
package com.rmi.common.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.rmi.common.Records;

/**
 * @author ibtehajraza
 *
 */
public class StudentRecord extends Records implements Serializable {

	/**
	 * Generated a serial version to maintain the integrity of the de-serialized
	 * object
	 */
	private static final long serialVersionUID = 2322732084920285357L;

	public List<String> coursesRegistered; // For Example maths/french/science

	public boolean status; // active/inactive

	public Date statusDate; // date when student became active inactive

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

}
