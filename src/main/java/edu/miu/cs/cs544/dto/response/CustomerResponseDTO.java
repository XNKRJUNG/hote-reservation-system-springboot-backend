package edu.miu.cs.cs544.dto.response;


import edu.miu.cs.cs544.domain.Address;
import edu.miu.cs.cs544.domain.AuditData;
import edu.miu.cs.cs544.dto.request.UserDTO;
import lombok.Data;

@Data
public class CustomerResponseDTO implements ResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone_num;
    private UserDTO user;
    private String token;
    private int statusCode;
    private AuditData auditData;
    private Address physicalAddress;
    private Address billingAddress;
    public CustomerResponseDTO() {
        this.user = new UserDTO();
    }
    public CustomerResponseDTO(String firstName, String lastName, String email, String phone_num, UserDTO user, String token, int statusCode, AuditData auditData, Address physicalAddress, Address billingAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone_num = phone_num;
        this.user = user;
        this.token = token;
        this.statusCode = statusCode;
        this.auditData = auditData;
        this.physicalAddress = physicalAddress;
        this.billingAddress = billingAddress;
    }
}
