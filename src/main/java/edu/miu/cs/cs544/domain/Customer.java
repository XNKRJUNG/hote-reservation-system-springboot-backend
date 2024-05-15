package edu.miu.cs.cs544.domain;

import jakarta.persistence.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Valid
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @NotBlank(message = "Phone_num is required")
    private String phone_num;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @Valid
    private User user;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "customer")
    private List<Reservation> reservations = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "physical_street")),
            @AttributeOverride(name = "city", column = @Column(name = "physical_city")),
            @AttributeOverride(name = "state", column = @Column(name = "physical_state_id")),
            @AttributeOverride(name = "zip", column = @Column(name = "physical_zip")),
            @AttributeOverride(name = "line1", column = @Column(name = "physical_line1")),
            @AttributeOverride(name = "line2", column = @Column(name = "physical_line2"))})
    @Valid
    private Address physicalAddress;
    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "street", column = @Column(name = "bill_street")),
            @AttributeOverride(name = "city", column = @Column(name = "bill_city")),
            @AttributeOverride(name = "state", column = @Column(name = "bill_state_id")),
            @AttributeOverride(name = "zip", column = @Column(name = "bill_zip")),
            @AttributeOverride(name = "line1", column = @Column(name = "bill_line1")),
            @AttributeOverride(name = "line2", column = @Column(name = "bill_line2"))})
    @Valid
    private Address billingAddress;
    @Embedded
    private AuditData auditData;

    public Customer() {
        this.user = new User();
        this.auditData = new AuditData();
        this.billingAddress = new Address();
        this.physicalAddress = new Address();
    }

}

