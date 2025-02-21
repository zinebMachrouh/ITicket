package com.example.iticket.services;

import com.example.iticket.dto.LoginRequest;
import com.example.iticket.dto.UserRequest;
import com.example.iticket.dto.UserResponse;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    UserResponse login(LoginRequest userRequest, HttpSession httpSession);
    UserResponse register(UserRequest userRequest);
    void logout(HttpSession httpSession);
}
