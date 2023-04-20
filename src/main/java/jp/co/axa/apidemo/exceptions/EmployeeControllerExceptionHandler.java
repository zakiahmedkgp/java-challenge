package jp.co.axa.apidemo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "jp.co.axa.apidemo.controllers")
public class EmployeeControllerExceptionHandler {
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> MultipartExceptionHandler(Exception ex){
		
		ex.printStackTrace();
		System.out.println("**************************");
		System.out.println("Default Exception handler called");
		System.out.println("**************************");
    	
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.header("description", "Bad request")
				.body(ex.getMessage());
		
	}

}
