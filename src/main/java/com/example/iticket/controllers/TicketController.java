package com.example.iticket.controllers;

import com.example.iticket.dto.TicketRequest;
import com.example.iticket.dto.TicketResponse;
import com.example.iticket.services.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/tickets")
@Profile("prod")
public class TicketController {
    private final TicketService ticketService;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest ticketDTO) {
        return ResponseEntity.ok(ticketService.createTicket(ticketDTO));
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/my-tickets")
    public ResponseEntity<List<TicketResponse>> getLoggedUserTickets() {
        return ResponseEntity.ok(ticketService.getLoggedUserTickets());
    }

    @PreAuthorize("hasRole('IT_SUPPORT')")
    @GetMapping
    public ResponseEntity<List<TicketResponse>> getTickets() {
        return ResponseEntity.ok(ticketService.getTickets());
    }

    @PreAuthorize("hasRole('IT_SUPPORT')")
    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> updateTicketStatus(@PathVariable String ticketId, @RequestParam String status) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(ticketId, status));
    }


    @PreAuthorize("hasAnyRole('EMPLOYEE', 'IT_SUPPORT')")
    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable String ticketId) {
        return ResponseEntity.ok(ticketService.getTicket(ticketId));
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'IT_SUPPORT')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TicketResponse>> getTicketsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
    }

}
