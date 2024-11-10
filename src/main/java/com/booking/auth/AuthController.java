package com.booking.auth;


import com.booking.dto.*;
import com.booking.dto.RegistrationRequest;
import com.booking.entities.AppUser;
import com.booking.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {


    AuthService authService;
    JwtService jwtService;


    public AuthController(AuthService authService,JwtService jwtService){
this.authService=authService;
this.jwtService=jwtService;

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request){
        authService.registerClient(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("An activation code send to your email for activation ! ");

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){

        System.out.println("login !!");
        return ResponseEntity.ok(authService.login(request));

    }


    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token){

        authService.activateAccount(token);

        return ResponseEntity.ok("Your account is verifed !");

    }
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegistrationRequest request){
        authService.registerAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("An activation code send to your email for activation ! ");


    }


    @PostMapping("/refresh-token")
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("start refresh");
        String refreshToken = null;
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String accessToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println("first if");

            refreshToken = authHeader.substring(7);


            String username = jwtService.getSubject(refreshToken);

            AppUser user = authService.loadUserByUsername(username);

            if (jwtService.isValidAndNoExpierd(user, refreshToken)) {


                accessToken = jwtService.buildAccesToken(user);

                LoginResponse resp = new LoginResponse();
                resp.setRefreshToken(refreshToken);
                resp.setAccesToken(accessToken);
                System.out.println("REFRESH TOKEN RESPONSE NEW JWT : " + accessToken);
                new ObjectMapper().writeValue(response.getOutputStream(), resp);
            }

        }

    }


}
