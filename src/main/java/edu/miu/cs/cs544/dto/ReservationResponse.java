package edu.miu.cs.cs544.dto;

import edu.miu.cs.cs544.domain.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationResponse {
    private Long reservationId;
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone_num;
    private String user_name;
    private Boolean active;
    private UserType userType;
    private String physicalLine1;
    private String physicalline2;
    private String created_by;
    private String updated_by;
    private LocalDateTime created_date;
    private LocalDateTime updated_date;
    private Status status;
    private List<ItemDTO> items ;
    private Double totalPayment=0.0;

    public ReservationResponse() {
    }

    public ReservationResponse(Long reservationId, Long customerId, String firstName, String lastName, String email, String phone_num, String user_name) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone_num = phone_num;
        this.user_name = user_name;
    }
}
