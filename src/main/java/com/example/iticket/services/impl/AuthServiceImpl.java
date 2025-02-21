package com.example.iticket.services.impl;

import com.example.iticket.dto.LoginRequest;
import com.example.iticket.dto.UserRequest;
import com.example.iticket.dto.UserResponse;
import com.example.iticket.entities.User;
import com.example.iticket.entities.enums.Role;
import com.example.iticket.repositories.UserRepository;
import com.example.iticket.services.AuthService;
import com.example.iticket.utils.JwtUtil;
import com.example.iticket.utils.TokenBlacklist;
import com.example.iticket.utils.UUIDGenerator;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse login(LoginRequest userRequest, HttpSession httpSession) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getUsername(),
                        userRequest.getPassword()
                )
        );

        User user = userRepository.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole().name());

        String token = jwtUtil.generateToken(user.getUsername(), claims);
        httpSession.setAttribute("token", token);

        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .role(user.getRole())
                .token(token)
                .build();
    }

    @Override
    public UserResponse register(UserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User newUser = User.builder()
                .id(UUIDGenerator.generate())
                .name(userRequest.getName())
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .active(true)
                .role(Role.EMPLOYEE)
                .build();

        User savedUser = userRepository.save(newUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", savedUser.getRole());
        claims.put("userId", savedUser.getId());
        String token = jwtUtil.generateToken(savedUser.getUsername(), claims);

        return UserResponse.builder()
                .name(savedUser.getName())
                .username(savedUser.getUsername())
                .role(savedUser.getRole())
                .token(token)
                .build();
    }

    @Override
    public void logout(HttpSession httpSession) {
        httpSession.getAttribute("token");
        TokenBlacklist.add((String) httpSession.getAttribute("token"));

        TokenBlacklist.clean();

        httpSession.invalidate();
    }
}
