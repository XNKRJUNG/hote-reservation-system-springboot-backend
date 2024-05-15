package edu.miu.cs.cs544.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@NoArgsConstructor
public class Address {
    @NotBlank(message = "line1 is required")
    private String line1;

    private String line2;
    @NotBlank(message = "city is required")
    private String city;
    @NotBlank(message = "zip is required")
    private String zip;

    @ManyToOne
    @JoinColumn(name = "state_id", insertable = false, updatable = false)
    private State state;
}
