//package edu.miu.cs.cs544.controller;
//
//import edu.miu.cs.cs544.domain.Address;
//import edu.miu.cs.cs544.dto.request.UserDTO;
//import edu.miu.cs.cs544.dto.response.CustomerResponseDTO;
//import edu.miu.cs.cs544.service.AuthenticationService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@SpringBootTest
//public class AuthenticationControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    AuthenticationService authenticationService;
//
//    @Test
//    public void loginWithNull() throws Exception {
//        Mockito.when(authenticationService.login(new UserDTO()))
//                .thenReturn(new CustomerResponseDTO("firstName", "lastName", "String email", "String phone_num", new UserDTO(), "String token", 401, new Address(), new Address()));
//        mockMvc.perform(post("/api/login"))
//                .andExpect(status().isUnauthorized());
//
//
//    }
//}
