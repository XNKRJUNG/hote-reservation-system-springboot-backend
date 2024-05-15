package edu.miu.cs.cs544.dto.request;


import edu.miu.cs.cs544.domain.Address;
import lombok.Data;

@Data
public class CustomerDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone_num;
    private UserDTO user;
    private AuditDataDTO auditDataDTO;
    private Address physicalAddress;
    private Address billingAddress;

    public CustomerDTO() {
        this.user = new UserDTO();
    }

    public String toString() {
        return String.format("%s %s %s %s", firstName, lastName, user.getUserPass(), user.getUserName());
    }


}
