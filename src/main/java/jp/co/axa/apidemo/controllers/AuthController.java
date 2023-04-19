package jp.co.axa.apidemo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.model.AuthRequest;
import jp.co.axa.apidemo.model.AuthResponse;
import jp.co.axa.apidemo.repositories.UserRepository;
import jp.co.axa.apidemo.security.jwt.JwtUtils;
import jp.co.axa.apidemo.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
		
		System.out.println("**************************");
		System.out.println("Within Auth Controller");
		System.out.println("**************************");
		
		if (!userRepository.existsByUsername(authRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body("Error: Username does not exist!");
		}
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		return ResponseEntity.ok(new AuthResponse(jwt));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@Valid @RequestBody AuthRequest authRequest) {
		
		System.out.println("**************************");
		System.out.println("Within Signup Controller");
		System.out.println("**************************");
		
		if (userRepository.existsByUsername(authRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body("Error: Username is already taken!");
		}
		
		// Create new user's account
		User user = new User(authRequest.getUsername(), 
							 encoder.encode(authRequest.getPassword()));
						
		userRepository.save(user);		

		return ResponseEntity.ok("User registered successfully");
	}

}
