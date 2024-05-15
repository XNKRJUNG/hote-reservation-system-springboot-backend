package edu.miu.cs.cs544.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AuditDTO {
    private String updatedBy;
    private LocalDateTime updatedOn;
}
