package com.booking.auth;


import com.booking.dto.*;
import com.booking.dto.RegistrationRequest;
import com.booking.entities.AppUser;
import com.booking.security.JwtService;
import com.booking.storage.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/auth")

@CrossOrigin("*")
public class AuthController {


    AuthService authService;
    JwtService jwtService;

    FileStorageService storageService;

    public AuthController(AuthService authService, JwtService jwtService, FileStorageService storageService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.storageService = storageService;

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestPart("request") RegistrationRequest request,@RequestPart("image")MultipartFile image) {
        authService.registerClient(request,image);
        return ResponseEntity.status(HttpStatus.CREATED).body("An activation code send to your email for activation ! ");

    }


    @PutMapping("/upload-image")
    public ResponseEntity<String> upload(Authentication connectedUser, @RequestParam(value = "image", required = true) MultipartFile image) {

        authService.uploadProfilePicture(connectedUser, image);

        return ResponseEntity.ok("Image uploaded ");
    }

    @GetMapping("/image/{image}")
    public ResponseEntity<byte[]> profilelImage(@PathVariable("image") String imageName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity.ok().headers(headers).body(storageService.getProfilelImage(imageName));

    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        System.out.println("login !!");
        return ResponseEntity.ok(authService.login(request));

    }


    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {

        authService.activateAccount(token);

        return ResponseEntity.ok("Your account is verifed !");

    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestPart("request") RegistrationRequest request,
                                           @RequestPart("image") MultipartFile image) {
        authService.registerAdmin(request,image);
        return ResponseEntity.status(HttpStatus.CREATED).body("An activation code send to your email for activation ! ");


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
