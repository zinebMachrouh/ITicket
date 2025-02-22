package com.example.iticket.services.impl;

import com.example.iticket.dto.TicketRequest;
import com.example.iticket.dto.TicketResponse;
import com.example.iticket.entities.Ticket;
import com.example.iticket.entities.User;
import com.example.iticket.entities.enums.Category;
import com.example.iticket.entities.enums.Priority;
import com.example.iticket.entities.enums.Status;
import com.example.iticket.repositories.TicketRepository;
import com.example.iticket.services.TicketService;
import com.example.iticket.utils.UUIDGenerator;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private final HttpSession HttpSession;

    @Override
    public TicketResponse createTicket(TicketRequest ticketDTO) {
        User user = (User) HttpSession.getAttribute("loggedUser");

        Ticket ticket = Ticket.builder()
                .id(UUIDGenerator.generate())
                .title(ticketDTO.getTitle())
                .description(ticketDTO.getDescription())
                .priority(Priority.valueOf(ticketDTO.getPriority()))
                .category(Category.valueOf(ticketDTO.getCategory()))
                .createdBy(user)
                .build();

        return mapToTicketResponse(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponse getTicket(String ticketId) {
        return ticketRepository.findById(ticketId)
                .map(this::mapToTicketResponse)
                .orElse(null);
    }

    @Override
    public List<TicketResponse> getTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToTicketResponse)
                .toList();
    }

    @Override
    public List<TicketResponse> getLoggedUserTickets() {
        User user = (User) HttpSession.getAttribute("loggedUser");

        return ticketRepository.findByCreatedBy(user).stream()
                .map(this::mapToTicketResponse)
                .toList();

    }

    @Override
    public TicketResponse updateTicketStatus(String ticketId, String status) {
        return ticketRepository.findById(ticketId)
                .map(ticket -> {
                    ticket.setStatus(Status.valueOf(status));
                    return mapToTicketResponse(ticketRepository.save(ticket));
                })
                .orElse(null);
    }

    @Override
    public List<TicketResponse> getTicketsByStatus(String status) {
        return ticketRepository.findByStatus(Status.valueOf(status)).stream()
                .map(this::mapToTicketResponse)
                .toList();
    }

    private TicketResponse mapToTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .priority(ticket.getPriority().name())
                .category(ticket.getCategory().name())
                .status(ticket.getStatus().name())
                .createdBy(ticket.getCreatedBy().getId())
                .createdAt(ticket.getCreatedAt())
                .build();
    }
}
