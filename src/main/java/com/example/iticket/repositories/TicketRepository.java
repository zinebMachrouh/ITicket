package com.example.iticket.repositories;

import com.example.iticket.entities.Ticket;
import com.example.iticket.entities.User;
import com.example.iticket.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findByCreatedBy(User user);
    List<Ticket> findByStatus(Status status);
}
