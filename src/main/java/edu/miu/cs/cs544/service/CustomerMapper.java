package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.domain.User;
import edu.miu.cs.cs544.dto.request.CustomerDTO;
import edu.miu.cs.cs544.dto.request.UserDTO;
import edu.miu.cs.cs544.dto.response.CustomerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer mapToEntity(CustomerResponseDTO customerDTO);
    Customer mapToEntity(CustomerDTO customerDTO);
    CustomerResponseDTO mapToDto(Customer customer);
    User mapToEntity(UserDTO userDTO);
    UserDTO mapToDto(User user);

}

