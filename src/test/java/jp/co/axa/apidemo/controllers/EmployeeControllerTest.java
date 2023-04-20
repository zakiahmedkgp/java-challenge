package jp.co.axa.apidemo.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EmployeeControllerExceptionHandler;
import jp.co.axa.apidemo.services.EmployeeService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class EmployeeControllerTest {
	
	private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    public void beforeEach() {
    	MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
        		.setControllerAdvice(EmployeeControllerExceptionHandler.class)
        		.build();
    }
    
    @Test
    public void testGetEmployees() throws Exception {
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("Ken Kaneki");
        employee1.setSalary(25000);
        employee1.setDepartment("IT");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Itadori Yuji");
        employee2.setSalary(2000);
        employee2.setDepartment("HR");

        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(employeeService.retrieveEmployees()).thenReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/app/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Ken Kaneki"))
                .andExpect(jsonPath("$[0].salary").value(25000))
                .andExpect(jsonPath("$[0].department").value("IT"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Itadori Yuji"))
                .andExpect(jsonPath("$[1].salary").value(2000))
                .andExpect(jsonPath("$[1].department").value("HR"));
    }
    
    @Test
    public void testGetEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Eren Yeager");
        employee.setSalary(1000);
        employee.setDepartment("IT");

        when(employeeService.getEmployee(1L)).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/app/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Eren Yeager"))
                .andExpect(jsonPath("$.salary").value(1000))
                .andExpect(jsonPath("$.department").value("IT"));
    }
    
    @Test
    public void testGetEmployeeWhenEmployeeIsNull() throws Exception {
        Long employeeId = 1L;
        when(employeeService.getEmployee(employeeId)).thenReturn(null);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/app/employees/{employeeId}", employeeId))
        		.andExpect(status().isBadRequest())
        		.andExpect(content().string(containsString("Employee does not exist with this employee ID!")));
                       
    }
    
    @Test
    public void testSaveEmployeeSuccess() throws Exception {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Mikasa Ackermann");
        employee.setSalary(1000);
        employee.setDepartment("IT");

        when(employeeService.existsByUsername(employee.getName())).thenReturn(false);
        when(employeeService.saveEmployee(employee)).thenReturn(employee);

        String json = objectMapper.writeValueAsString(employee);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/app/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee saved successfully!"));

    }
    
    @Test
    public void testSaveEmployeeInvalidDetails() throws Exception {
        Employee employee = new Employee();
        employee.setName("");
        employee.setSalary(1000);
        employee.setDepartment("IT");

        String json = objectMapper.writeValueAsString(employee);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/app/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Employee details. Please confirm the retry!"));

    }
    
    @Test
    public void testSaveEmployeeUsernameExists() throws Exception {
        Employee employee = new Employee();
        employee.setName("baadshah");
        employee.setSalary(12000);
        employee.setDepartment("Infra");

        String json = objectMapper.writeValueAsString(employee);
        
        when(employeeService.existsByUsername(employee.getName())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/app/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Username is already taken. Username is same as name!"));

    }
    
    @Test
    void testDeleteEmployee() throws Exception {
        Long employeeId = 1L;

        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        when(employeeService.getEmployee(employeeId)).thenReturn(existingEmployee);
        doNothing().when(employeeService).deleteEmployee(employeeId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/app/employees/{employeeId}", employeeId))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully!"));

        Mockito.verify(employeeService).deleteEmployee(employeeId);
    }
    
    @Test
    void testDeleteInvalidEmployee() throws Exception {
        Long employeeId = 1L;

        when(employeeService.getEmployee(employeeId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/app/employees/{employeeId}", employeeId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: employee does not exist. Please check Empoyee ID!"));

    }
    
    @Test
    public void testUpdateEmployee_success() throws Exception {

    	Long employeeId = 1L;
        Employee existingEmployee = new Employee();
        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("Armin");
        updatedEmployee.setSalary(10000);
        updatedEmployee.setDepartment("IT");
        
        when(employeeService.getEmployee(employeeId)).thenReturn(existingEmployee);
        when(employeeService.updateEmployee(updatedEmployee)).thenReturn(updatedEmployee);
        
        String json = objectMapper.writeValueAsString(updatedEmployee);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/app/employees/{employeeId}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee updated successfully!"));

    }
    
    @Test
    public void testUpdateEmployee_exception() throws Exception {

    	Long employeeId = 1L;
    	Employee updatedEmployee = new Employee();
        updatedEmployee.setName("Armin");
        updatedEmployee.setSalary(10000);
        updatedEmployee.setDepartment("IT");
        
        when(employeeService.getEmployee(employeeId)).thenReturn(null);
        
        String json = objectMapper.writeValueAsString(updatedEmployee);
        
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/app/employees/{employeeId}", employeeId)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: employee does not exist. Please check Empoyee ID!"));

    }
	
}
