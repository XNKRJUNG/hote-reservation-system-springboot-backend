package edu.miu.cs.cs544.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer occupants;
	private LocalDateTime checkinDate;
	private LocalDateTime checkoutDate;
	@ManyToOne
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	@Embedded
	private AuditData auditData;
}
