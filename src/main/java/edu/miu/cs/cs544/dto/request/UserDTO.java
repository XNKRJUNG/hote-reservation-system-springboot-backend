package edu.miu.cs.cs544.dto.request;

import edu.miu.cs.cs544.domain.AuditData;
import edu.miu.cs.cs544.domain.UserType;
import lombok.Data;

@Data
public class UserDTO {

    private String userName;
    private String userPass;
    private UserType userType;

    public String toString(){
        return String.format("%s %s",userName,userPass);
    }

}
