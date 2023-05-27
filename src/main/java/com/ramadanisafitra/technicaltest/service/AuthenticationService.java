package com.ramadanisafitra.technicaltest.service;

import com.ramadanisafitra.technicaltest.dto.AuthenticationRequest;
import com.ramadanisafitra.technicaltest.dto.AuthenticationResponse;
import com.ramadanisafitra.technicaltest.dto.RegisterRequest;
import com.ramadanisafitra.technicaltest.dto.RegisterResponse;
import com.ramadanisafitra.technicaltest.config.JwtServie;
import com.ramadanisafitra.technicaltest.user.User;
import com.ramadanisafitra.technicaltest.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServie jwtServie;
    private final AuthenticationManager authenticationManager;
    public RegisterResponse register(RegisterRequest request) {

        var user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        return RegisterResponse.builder()
                .response("Registrasion Success")
                 .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.getUsername(),
//                            request.getPassword()
//                    )
//            );

            var userOptional = userRepository.findByUsername(request.getUsername());
            log.info("isis :{}",userOptional)   ;
            var user = userOptional.orElseThrow(() -> new NoSuchElementException("User not found"));

            var jwtToken = jwtServie.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new BadCredentialsException("Invalid username or password");
        }
    }

}
