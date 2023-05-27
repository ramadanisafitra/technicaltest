package com.ramadanisafitra.technicaltest.controller;

import com.ramadanisafitra.technicaltest.dto.AuthenticationRequest;
import com.ramadanisafitra.technicaltest.dto.AuthenticationResponse;
import com.ramadanisafitra.technicaltest.dto.RegisterRequest;
import com.ramadanisafitra.technicaltest.dto.RegisterResponse;
import com.ramadanisafitra.technicaltest.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request){

        return ResponseEntity.ok(authenticationService.register(request));

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){

        return ResponseEntity.ok(authenticationService.authenticate(request));

    }

}
