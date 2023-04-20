package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.utility.EmployeeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/app")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return employees;
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable(name="employeeId")Long employeeId) throws Exception {
    	
    	Employee employee = employeeService.getEmployee(employeeId);
    	if(employee == null) {
    		throw new Exception("Employee does not exist with this employee ID!");
    	}
        return employeeService.getEmployee(employeeId);
    }

    @PostMapping("/employees")
    public ResponseEntity<String> saveEmployee(@RequestBody Employee employee) throws Exception{
    	
    	if (employeeService.existsByUsername(employee.getName())) {
			return ResponseEntity
					.badRequest()
					.body("Error: Username is already taken. Username is same as name!");
		}
    	
    	if(EmployeeUtility.isInvalidEmployee(employee)) {
    		throw new Exception("Invalid Employee details. Please confirm the retry!");
    	};
        employeeService.saveEmployee(employee);
        return ResponseEntity.ok("Employee saved successfully!");
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name="employeeId")Long employeeId) throws Exception{
        
        
        Employee existingEmployee = employeeService.getEmployee(employeeId);
        if(existingEmployee == null){
            throw new Exception("Error: employee does not exist. Please check Empoyee ID!");
        }
        
        employeeService.deleteEmployee(employeeId);
        
        return ResponseEntity.ok("Employee deleted successfully!");
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<String> updateEmployee(@RequestBody Employee updatedEmployee,
                               @PathVariable(name="employeeId")Long employeeId){
        Employee existingEmployee = employeeService.getEmployee(employeeId);
        if(existingEmployee == null){
            
        	return ResponseEntity
					.badRequest()
					.body("Error: employee does not exist. Please check Empoyee ID!");
        }
        
        EmployeeUtility.copy(existingEmployee, updatedEmployee);
        employeeService.updateEmployee(existingEmployee);
        
        return ResponseEntity.ok("Employee updated successfully!");
        
        

    }

}
