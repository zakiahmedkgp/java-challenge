package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"employees"})
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> retrieveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }
    
    @Cacheable(key = "#employeeId")
    public Employee getEmployee(Long employeeId) {
    	
    	System.out.println("**************************");
		System.out.println("getting employee from db");
		System.out.println("**************************");
    	
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        
        if(optEmp.isPresent())
        	return (Employee)optEmp.get();
        return null;
    }
    
    @CachePut(key = "#employee.id")
    public Employee saveEmployee(Employee employee){
    	
    	System.out.println("**************************");
		System.out.println("saving employee in db");
		System.out.println("**************************");
    	
        employeeRepository.save(employee);
        return employee;
    }
    
    @CacheEvict(key = "#employeeId")
    public void deleteEmployee(Long employeeId){
    	
    	System.out.println("**************************");
		System.out.println("deleting employee from db");
		System.out.println("**************************");
    	
        employeeRepository.deleteById(employeeId);
    }
    
    @CachePut(key = "#employee.id")
    public Employee updateEmployee(Employee employee) {
    	
    	System.out.println("**************************");
		System.out.println("updating employee in db");
		System.out.println("**************************");
    	
    	saveEmployee(employee);
    	return employee;
    }

	public boolean existsByUsername(String name) {
		return employeeRepository.existsByName(name);
	}
}