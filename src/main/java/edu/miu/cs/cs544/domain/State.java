package edu.miu.cs.cs544.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "state code is required")
    private String code;
    @NotBlank(message = "state name is required")
    private String name;
    @ManyToOne
    @JoinColumn(name = "country-code")
    @Valid
    private Country country;
    @Embedded
    private AuditData auditData;

    public State() {
        auditData = new AuditData();
    }
}
