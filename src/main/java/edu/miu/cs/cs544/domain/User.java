package edu.miu.cs.cs544.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "UserName is required")
    private String userName;
    @NotBlank(message = "UserPass is required")
    private String userPass;
    private Boolean active;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "UserType cannot be null")
    private UserType userType;

    @Embedded
    private AuditData auditData;

    public User(){
        this.auditData = new AuditData();
    }

}
