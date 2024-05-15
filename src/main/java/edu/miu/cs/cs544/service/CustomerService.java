package edu.miu.cs.cs544.service;


import edu.miu.cs.cs544.domain.Address;
import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.domain.User;
import edu.miu.cs.cs544.dto.response.CustomerResponseDTO;
import edu.miu.cs.cs544.dto.response.ResponseDTO;
import edu.miu.cs.cs544.repository.CustomerRepository;
import edu.miu.cs.cs544.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CustomerService {

    List<Customer> getCustomers();

    Customer getCustomerById(Long customerId);

    Customer addCustomer(Customer customer);

    Customer updateCustomerById(Long customerId, Customer updatedCustomer);

    Customer partialUpdateCustomerById(Long customerId, Customer partialCustomer);

    void deleteCustomerById(Long customerId);

    CustomerResponseDTO loadUserByUsername(String username);

}
