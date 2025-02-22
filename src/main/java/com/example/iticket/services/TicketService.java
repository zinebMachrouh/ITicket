package com.example.iticket.services;

import com.example.iticket.dto.TicketRequest;
import com.example.iticket.dto.TicketResponse;

import java.util.List;

public interface TicketService {
    TicketResponse createTicket(TicketRequest ticketDTO);
    TicketResponse getTicket(String ticketId);
    List<TicketResponse> getTickets();
    List<TicketResponse> getLoggedUserTickets();
    TicketResponse updateTicketStatus(String ticketId, String status);
    List<TicketResponse> getTicketsByStatus(String status);
}
