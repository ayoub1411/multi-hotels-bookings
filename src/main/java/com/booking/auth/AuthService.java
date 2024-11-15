package com.booking.auth;

import com.booking.dto.LoginRequest;
import com.booking.dto.LoginResponse;
import com.booking.dto.RegistrationRequest;
import com.booking.entities.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {


    public void registerClient(RegistrationRequest request,MultipartFile image);
    public void registerAdmin(RegistrationRequest request,MultipartFile image);


    public void activateAccount(String token);


    public LoginResponse login(LoginRequest request);

    public AppUser loadUserByUsername(String email);

    public void uploadProfilePicture(Authentication connectedUser,MultipartFile image);


}
