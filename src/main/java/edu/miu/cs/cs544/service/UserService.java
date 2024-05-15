package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.dto.response.CustomerResponseDTO;

public interface UserService {

    CustomerResponseDTO getLoggedInUser();
}
