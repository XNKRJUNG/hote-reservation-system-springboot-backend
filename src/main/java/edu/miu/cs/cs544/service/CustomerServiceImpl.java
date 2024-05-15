package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.AuditData;
import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.domain.State;
import edu.miu.cs.cs544.domain.User;
import edu.miu.cs.cs544.dto.response.CustomerResponseDTO;
import edu.miu.cs.cs544.enums.Constants;
import edu.miu.cs.cs544.exception.AppException;
import edu.miu.cs.cs544.repository.CustomerRepository;
import edu.miu.cs.cs544.repository.StateRepository;
import edu.miu.cs.cs544.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StateRepository stateRepository;

    public CustomerResponseDTO loadUserByUsername(String username) {
        User user = userRepository.findByUserName(username);
        if (user != null) {
            return CustomerMapper.INSTANCE.mapToDto(customerRepository.findByUser_id(user.getId()));
        }
        return null;
    }

    @Override
    public List<Customer> getCustomers() {
        try {
            return customerRepository.findAll();
        } catch (Exception e) {
            throw new AppException(Constants.GET_CUSTOMER_FAIL.getmessage());
        }
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        try {
            return customerRepository.findById(customerId).get();
        } catch (Exception e) {
            throw new AppException(Constants.GET_CUSTOMER_FAIL.getmessage());
        }
    }

    @Override
    public Customer addCustomer(Customer customer) {
        try {
            AuditData auditEntry = new AuditData();
            auditEntry.setCreatedBy("Hardcoded AddedBy");
            auditEntry.setCreatedOn(LocalDateTime.now());
            auditEntry.setUpdatedBy("Hardcoded AddedBy");
            auditEntry.setUpdatedOn(LocalDateTime.now());
            customer.setAuditData(auditEntry);

            User userData = customer.getUser();
            userData.setAuditData(auditEntry);
            customer.setUser(userData);

            return customerRepository.save(customer);
        } catch (Exception e) {
            throw new AppException(Constants.ADD_CUSTOMER_FAIL.getmessage());
        }
    }

    @Override
    public Customer updateCustomerById(Long customerId, Customer updatedCustomer) {
        try {
            Customer existingCustomer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new AppException("Customer not found with ID: " + customerId));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomerResponseDTO customer = (CustomerResponseDTO) authentication.getPrincipal();
            String username = String.valueOf(customer.getUser()).split(" ")[0];

            User existingCustomerUserData = existingCustomer.getUser();
            User updatedCustomerUserData = updatedCustomer.getUser();
            AuditData userAuditData = existingCustomerUserData.getAuditData();

            if (!existingCustomerUserData.getUserName().equals(updatedCustomerUserData.getUserName())
                    || !existingCustomerUserData.getUserPass().equals(updatedCustomerUserData.getUserPass())
                    || existingCustomerUserData.getActive() != updatedCustomerUserData.getActive()
                    || !existingCustomerUserData.getUserType().equals(updatedCustomerUserData.getUserType())) {
                // Update user fields
                existingCustomerUserData.setUserName(updatedCustomerUserData.getUserName());
                existingCustomerUserData.setUserPass(updatedCustomerUserData.getUserPass());
                existingCustomerUserData.setActive(updatedCustomerUserData.getActive());
                existingCustomerUserData.setUserType(updatedCustomerUserData.getUserType());

                // Update audit data for user
                userAuditData.setUpdatedBy(username);
                userAuditData.setUpdatedOn(LocalDateTime.now());
                existingCustomerUserData.setAuditData(userAuditData);
            }

            // Check if any of the customer fields are changed
            if (!existingCustomer.getFirstName().equals(updatedCustomer.getFirstName())
                    || !existingCustomer.getLastName().equals(updatedCustomer.getLastName())
                    || !existingCustomer.getEmail().equals(updatedCustomer.getEmail())
                    || !existingCustomer.getPhone_num().equals(updatedCustomer.getPhone_num())) {
                // Update customer fields
                existingCustomer.setFirstName(updatedCustomer.getFirstName());
                existingCustomer.setLastName(updatedCustomer.getLastName());
                existingCustomer.setEmail(updatedCustomer.getEmail());
                existingCustomer.setPhone_num(updatedCustomer.getPhone_num());

                // Update audit data for customer
                AuditData auditEntry = existingCustomer.getAuditData();
                auditEntry.setUpdatedBy(username);
                auditEntry.setUpdatedOn(LocalDateTime.now());
                existingCustomer.setAuditData(auditEntry);
            }

            if (!existingCustomer.getBillingAddress().equals(updatedCustomer.getBillingAddress())
                    || !existingCustomer.getPhysicalAddress().equals(updatedCustomer.getPhysicalAddress())) {

                State repoBillingAddress = stateRepository.findById(updatedCustomer.getBillingAddress().getState().getId()).get();
                State repoPhysicalAddress = stateRepository.findById(updatedCustomer.getPhysicalAddress().getState().getId()).get();

                existingCustomer.setBillingAddress(updatedCustomer.getBillingAddress());
                existingCustomer.getBillingAddress().setState(repoBillingAddress);

                existingCustomer.setPhysicalAddress(updatedCustomer.getPhysicalAddress());
                existingCustomer.getPhysicalAddress().setState(repoPhysicalAddress);

                // Update audit data for customer
                AuditData auditEntry = existingCustomer.getAuditData();
                auditEntry.setUpdatedBy(username);
                auditEntry.setUpdatedOn(LocalDateTime.now());
                existingCustomer.setAuditData(auditEntry);
            }

            // Update user in Customer entity
            existingCustomer.setUser(existingCustomerUserData);

            return customerRepository.save(existingCustomer);
        } catch (Exception e) {
            throw new AppException(Constants.UPDATE_CUSTOMER_FAIL.getmessage());
        }
    }

    @Override
    public Customer partialUpdateCustomerById(Long customerId, Customer partialCustomer) {
        try {
            Customer existingCustomer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new AppException("Customer not found with ID: " + customerId));

            User existingCustomerUserData = existingCustomer.getUser();
            AuditData userAuditData = existingCustomerUserData.getAuditData();

            // Check if any of the user fields are changed
            if (partialCustomer.getUser() != null) {
                User partialUserData = partialCustomer.getUser();

                if (partialUserData.getUserName() != null
                        || partialUserData.getUserPass() != null
                        || partialUserData.getActive() != null) {
                    // Update user fields
                    if (partialUserData.getUserName() != null) {
                        existingCustomerUserData.setUserName(partialUserData.getUserName());
                    }
                    if (partialUserData.getUserPass() != null) {
                        existingCustomerUserData.setUserPass(partialUserData.getUserPass());
                    }
                    if (partialUserData.getActive() != null) {
                        existingCustomerUserData.setActive(partialUserData.getActive());
                    }
                    if (partialUserData.getUserType() != null) {
                        existingCustomerUserData.setUserType(partialUserData.getUserType());
                    }

                    // Update audit data for user
                    userAuditData.setUpdatedBy("New Hardcoded UpdatedBy");
                    userAuditData.setUpdatedOn(LocalDateTime.now());
                    existingCustomerUserData.setAuditData(userAuditData);
                }
            }

            // Check if any of the customer fields are changed
            if (partialCustomer.getFirstName() != null
                    || partialCustomer.getLastName() != null
                    || partialCustomer.getEmail() != null
                    || partialCustomer.getPhone_num() != null) {
                // Update customer fields
                if (partialCustomer.getFirstName() != null) {
                    existingCustomer.setFirstName(partialCustomer.getFirstName());
                }
                if (partialCustomer.getLastName() != null) {
                    existingCustomer.setLastName(partialCustomer.getLastName());
                }
                if (partialCustomer.getEmail() != null) {
                    existingCustomer.setEmail(partialCustomer.getEmail());
                }
                if (partialCustomer.getPhone_num() != null) {
                    existingCustomer.setPhone_num(partialCustomer.getPhone_num());
                }

                // Update audit data for customer
                AuditData auditEntry = existingCustomer.getAuditData();
                auditEntry.setUpdatedBy("New Hardcoded UpdatedBy");
                auditEntry.setUpdatedOn(LocalDateTime.now());
                existingCustomer.setAuditData(auditEntry);
            }

            // Update user in Customer entity
            existingCustomer.setUser(existingCustomerUserData);

            return customerRepository.save(existingCustomer);
        } catch (Exception e) {
            throw new AppException(Constants.UPDATE_CUSTOMER_FAIL.getmessage());
        }
    }


    @Override
    public void deleteCustomerById(Long customerId) {
        try {
            Customer existingCustomer = customerRepository.findById(customerId).get();
            customerRepository.delete(existingCustomer);
        } catch (Exception e) {
            throw new AppException(Constants.DELETE_CUSTOMER_FAIL.getmessage());
        }
    }
}