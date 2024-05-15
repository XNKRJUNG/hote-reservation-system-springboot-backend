package edu.miu.cs.cs544.controller;

import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.dto.request.CustomerDTO;
import edu.miu.cs.cs544.dto.request.UserDTO;
import edu.miu.cs.cs544.dto.response.CustomerResponseDTO;
import edu.miu.cs.cs544.dto.response.ResponseDTO;
import edu.miu.cs.cs544.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO user) {
        try {
            ResponseDTO response = authenticationService.login(user);
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody Customer customer) {
        try {
            ResponseDTO response = authenticationService.register(customer);
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/add/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody Customer customer) {
        try {
            ResponseDTO response = authenticationService.registerAdmin(customer);
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<?> registerAdminForTheFirstTime(@RequestBody Customer customer) {
        try {
            ResponseDTO response = authenticationService.registerAdmin(customer);
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        if (authenticationService.logout()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("not Successfully LoggedOut", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product")
    public ResponseEntity<?> getproduct() {
            return new ResponseEntity<>("not Successfully LoggedOut", HttpStatus.OK);
    }
}
