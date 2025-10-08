package com.wecp.progressive.controller;


import com.wecp.progressive.dto.LoginRequest;
import com.wecp.progressive.dto.LoginResponse;
import com.wecp.progressive.entity.Customers;
import com.wecp.progressive.jwt.JwtUtil;
import com.wecp.progressive.service.CustomerLoginService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CustomerLoginController {
    private final CustomerLoginService customerLoginService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    public CustomerLoginController(CustomerLoginService customerLoginService,
        AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
            this.customerLoginService = customerLoginService;
            this.authenticationManager = authenticationManager;
            this.jwtUtil = jwtUtil;
    }
    
    @PostMapping("user/register")
    public ResponseEntity<Customers> registerUser(@RequestBody Customers user) {
        return new ResponseEntity<>(customerLoginService.createCustomer(user), HttpStatus.CREATED);
    }
    
    @PostMapping("user/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        }
        catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username or Password", e);
        }
        final UserDetails userDetails = customerLoginService.loadUserByUsername(loginRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails.getUsername());
        Customers customer = customerLoginService.getCustomerByName(userDetails.getUsername());
        return new ResponseEntity<>(new LoginResponse(token, customer.getRole(), customer.getCustomerId()), HttpStatus.OK);
    }
}