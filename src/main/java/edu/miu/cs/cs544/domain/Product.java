package edu.miu.cs.cs544.domain;

import jakarta.persistence.*;
import lombok.Data;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private double night_rate;
	private String description;
	private Integer number_of_beds;
	private String excerpt;
	@Enumerated(EnumType.STRING)
	private ProductType type;
	private boolean availability;
	@Embedded
	private AuditData auditData;
}
