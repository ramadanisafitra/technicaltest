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
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    //Register
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request){

        return ResponseEntity.ok(authenticationService.register(request));

    }

    //Request Token
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){

        return ResponseEntity.ok(authenticationService.login(request));

    }

}
