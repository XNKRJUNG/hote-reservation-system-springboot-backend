package edu.miu.cs.cs544.service;


import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.domain.State;
import edu.miu.cs.cs544.domain.User;
import edu.miu.cs.cs544.domain.UserType;
import edu.miu.cs.cs544.dto.request.UserDTO;
import edu.miu.cs.cs544.dto.response.CustomerResponseDTO;
import edu.miu.cs.cs544.dto.response.ErrorResponseDTO;
import edu.miu.cs.cs544.dto.response.ResponseDTO;
import edu.miu.cs.cs544.exception.UsernameExistsException;
import edu.miu.cs.cs544.repository.CustomerRepository;
import edu.miu.cs.cs544.repository.UserRepository;
import edu.miu.cs.cs544.security.JwtTokenService;
import edu.miu.cs.cs544.validation.Validation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Validation validation;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    UserService userService;

    @Autowired
    StateService stateService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenService jwtTokenService;


    public ResponseDTO login(UserDTO userDTO) {
        try {
            if (userDTO.getUserName() == null || userDTO.getUserPass() == null) {
                throw new UsernameNotFoundException("You should enter user name and password");
            }
            User loggedInUser = userRepository.findByUserName(userDTO.getUserName());
            if (loggedInUser != null) {
                boolean isPasswordCorrect = passwordEncoder.matches(userDTO.getUserPass(), loggedInUser.getUserPass());
                if (isPasswordCorrect) {
                    CustomerResponseDTO response = CustomerMapper.INSTANCE.mapToDto(customerRepository.findByUser_id(loggedInUser.getId()));
                    response.setToken(jwtTokenService.generateToken(response));
                    response.setStatusCode(HttpStatus.OK.value());
                    response.getUser().setUserPass("");
                    return response;
                } else {
                    throw new UsernameNotFoundException("Invalid password");
                }
            } else {
                throw new UsernameNotFoundException("Invalid Username");
            }
        } catch (UsernameNotFoundException e) {
            System.out.println(e.getMessage());
            return new ErrorResponseDTO(e.getMessage(), HttpStatus.CONFLICT.value());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e);
            return new ErrorResponseDTO("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Transactional
    public ResponseDTO register(Customer customer) {
        try {
            validation.isEntityValid(customer);
            if (userRepository.existsByUserName(customer.getUser().getUserName())) {
                throw new UsernameExistsException("Username already exists");
            }
            //encrypt the password by BCryptPasswordEncoder
            customer.getUser().setUserPass(passwordEncoder.encode(customer.getUser().getUserPass()));
            customer.getAuditData().setCreatedOn(LocalDateTime.now());
            customer.getAuditData().setCreatedBy(customer.getUser().getUserName());
            customer.getAuditData().setUpdatedBy(customer.getUser().getUserName());
            customer.getAuditData().setUpdatedOn(LocalDateTime.now());
            customer.getUser().getAuditData().setCreatedOn(LocalDateTime.now());
            customer.getUser().getAuditData().setUpdatedOn(LocalDateTime.now());
            customer.getUser().getAuditData().setCreatedBy(customer.getUser().getUserName());
            customer.getUser().getAuditData().setUpdatedBy(customer.getUser().getUserName());
            //Validate the state of the data using its ID and retrieve the corresponding set of records from the database
            if (customer.getPhysicalAddress().getState().getId() == null || customer.getBillingAddress().getState().getId() == null) {
                throw new UsernameExistsException("you should provide state id");
            }
            State physical = stateService.getStateById(customer.getPhysicalAddress().getState().getId());
            State bill = stateService.getStateById(customer.getBillingAddress().getState().getId());
            if (physical == null || bill == null) {
                throw new UsernameExistsException("provided wrong state id");
            }
            customer.getPhysicalAddress().setState(physical);
            customer.getBillingAddress().setState(bill);
            //The customer entity is saved to the database, and the resulting entity is then mapped to a DTO using the CustomerMapper.
            CustomerResponseDTO response = CustomerMapper.INSTANCE.mapToDto(customerRepository.save(customer));
             Integer integer = customerRepository.updateStateId(response.getId(), response.getPhysicalAddress().getState().getId());
            //generate Token and set on token attribute
            response.setToken(jwtTokenService.generateToken(response));
            response.setStatusCode(HttpStatus.CREATED.value());
            response.getUser().setUserPass("");
            return response;
        } catch (UsernameExistsException e) {
            System.out.println(e.getMessage());
            return new ErrorResponseDTO(e.getMessage(), HttpStatus.CONFLICT.value());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return new ErrorResponseDTO("An unexpected error occurred", HttpStatus.CONFLICT.value());
        }
    }

    @Override
    @Transactional
    public ResponseDTO registerAdmin(Customer customer) {
        try {
            validation.isEntityValid(customer);

            if (!customer.getUser().getUserType().toString().equals(UserType.ADMIN.toString()) &&
                    userService.getLoggedInUser() != null &&
                    userService.getLoggedInUser().getUser().getUserType().toString().equals(UserType.ADMIN.toString())) {
                throw new UsernameExistsException("Admin Can't create customer");
            }
            if (userService.getLoggedInUser() != null &&
                    userService.getLoggedInUser().getUser().getUserType().toString().equals(UserType.CLIENT.toString())) {
                throw new UsernameExistsException("Customer Can't create Admin");
            }
            if (userRepository.existsByUserName(customer.getUser().getUserName())) {
                throw new UsernameExistsException("Username already exists");
            }
            //encrypt the password by BCryptPasswordEncoder
            customer.getUser().setUserPass(passwordEncoder.encode(customer.getUser().getUserPass()));
            customer.getAuditData().setCreatedOn(LocalDateTime.now());
            customer.getAuditData().setUpdatedBy(customer.getUser().getUserName());
            customer.getAuditData().setUpdatedBy(customer.getUser().getUserName());
            customer.getUser().getAuditData().setCreatedOn(LocalDateTime.now());
            customer.getUser().getAuditData().setUpdatedOn(LocalDateTime.now());
            customer.getUser().getAuditData().setCreatedBy(customer.getUser().getUserName());
            customer.getUser().getAuditData().setUpdatedBy(customer.getUser().getUserName());

            //The customer entity is saved to the customer repository, and the resulting entity is then mapped to a DTO using the CustomerMapper.
            CustomerResponseDTO response = CustomerMapper.INSTANCE.mapToDto(customerRepository.save(customer));

            //generate Token and set on token attribute
            response.setToken("");
            response.setStatusCode(HttpStatus.CREATED.value());
            response.getUser().setUserPass("");
            return response;
        } catch (UsernameExistsException e) {
            System.out.println(e.getMessage());
            return new ErrorResponseDTO(e.getMessage(), HttpStatus.CONFLICT.value());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return new ErrorResponseDTO("An unexpected error occurred", HttpStatus.CONFLICT.value());
        }
    }

    @Transactional
    public boolean logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            CustomerResponseDTO customerDetails = (CustomerResponseDTO) authentication.getPrincipal();
            System.out.println(customerDetails);
//            SecurityContextHolder.clearContext();
            return jwtTokenService.deleteToken(customerDetails.getId());
        }
        return false;
    }


}
