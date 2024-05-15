package edu.miu.cs.cs544.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
//@ToString(exclude = {"customer", "items"})
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="customerId")
	private Customer customer;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "reservation")
	private List<Item> items;
	@Enumerated(EnumType.STRING)
	private Status status;
	@Embedded
	private AuditData auditData;

}
