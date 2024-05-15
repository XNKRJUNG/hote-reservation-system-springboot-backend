package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.dto.request.CustomerDTO;
import edu.miu.cs.cs544.dto.request.UserDTO;
import edu.miu.cs.cs544.dto.response.ResponseDTO;

public interface AuthenticationService {

    ResponseDTO login(UserDTO userDTO);

    ResponseDTO register(Customer customer);

    ResponseDTO registerAdmin(Customer customer);

    boolean logout();
}
