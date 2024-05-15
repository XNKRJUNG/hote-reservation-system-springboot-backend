package edu.miu.cs.cs544.dto;

import edu.miu.cs.cs544.domain.Product;
import edu.miu.cs.cs544.domain.Reservation;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ItemDTO {
    private Integer id;

    private Integer occupants;

    private LocalDateTime checkinDate;

    private LocalDateTime checkoutDate;

    private ProductDTO productDTO;


}
