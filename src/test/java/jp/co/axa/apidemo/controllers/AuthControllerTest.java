package jp.co.axa.apidemo.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.axa.apidemo.model.AuthRequest;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class AuthControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeAll
    public void setup() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("username");
        authRequest.setPassword("password");
        String json = objectMapper.writeValueAsString(authRequest);
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }
    
    @Test
    public void testAuthenticateUser_01() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("username");
        authRequest.setPassword("password");
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk());
        
    }
    
    @Test
    public void testAuthenticateUser_02() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("userna");
        authRequest.setPassword("password");
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Username does not exist!"));
        
    }
    
    @Test
    public void testSignupUser() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("username");
        authRequest.setPassword("password");
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Username is already taken!"));
    }

}
