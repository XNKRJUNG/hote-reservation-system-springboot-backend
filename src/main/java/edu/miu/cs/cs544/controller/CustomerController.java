package edu.miu.cs.cs544.controller;

import edu.miu.cs.cs544.domain.Address;
import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.dto.response.CustomerResponseDTO;
import edu.miu.cs.cs544.dto.response.ResponseDTO;
import edu.miu.cs.cs544.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getCustomers() {
        return new ResponseEntity<>(customerService.getCustomers(), HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long customerId) {
        return new ResponseEntity<>(customerService.getCustomerById(customerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.addCustomer(customer), HttpStatus.OK);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomerById(@PathVariable Long customerId, @RequestBody Customer updatedCustomer) {
        return new ResponseEntity<>(customerService.updateCustomerById(customerId, updatedCustomer), HttpStatus.OK);
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<?> partialUpdateCustomerById(@PathVariable Long customerId, @RequestBody Customer partialCustomer) {
        return new ResponseEntity<>(customerService.partialUpdateCustomerById(customerId, partialCustomer), HttpStatus.OK);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable Long customerId) {
        customerService.deleteCustomerById(customerId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
