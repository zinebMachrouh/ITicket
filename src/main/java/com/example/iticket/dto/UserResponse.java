package com.example.iticket.dto;

import com.example.iticket.entities.enums.Role;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponse {
    String token;
    String name;
    String username;
    Role role;
}