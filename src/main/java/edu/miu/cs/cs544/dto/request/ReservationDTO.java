package edu.miu.cs.cs544.dto.request;

import edu.miu.cs.cs544.domain.Status;

import java.util.List;

public class ReservationDTO {
    private Long id;
    private Long customerId;
    private List<Long> itemIds;
    private Status status;
    private AuditDataDTO auditData;

}
