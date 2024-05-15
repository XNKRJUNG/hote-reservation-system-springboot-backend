package edu.miu.cs.cs544.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Country {
    @Id
    @NotBlank(message = "country code is required")
    private String code;
    @NotBlank(message = "country name is required")
    private String name;
    private Integer population;
    @Embedded
    private AuditData auditData;

    public Country() {
        auditData = new AuditData();
    }

}
