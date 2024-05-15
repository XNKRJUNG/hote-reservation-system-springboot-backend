package edu.miu.cs.cs544.dto;

import edu.miu.cs.cs544.domain.ProductType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;

    private String name;

    private double night_rate;

    private String description;

    private Integer number_of_beds;

    private String excerpt;

    private ProductType type;

    private boolean availability;
}
