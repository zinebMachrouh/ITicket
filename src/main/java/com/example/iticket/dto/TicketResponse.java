package com.example.iticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketResponse {
    private String id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private String category;
    private String createdBy;
    private LocalDateTime createdAt;
}
