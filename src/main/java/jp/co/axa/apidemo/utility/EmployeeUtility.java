package jp.co.axa.apidemo.utility;

import jp.co.axa.apidemo.entities.Employee;

public class EmployeeUtility {
	
	public static void copy(Employee existing, Employee updated) {
		existing.setName(updated.getName());
		existing.setSalary(updated.getSalary());
		existing.setDepartment(updated.getDepartment());
		
	}

}
