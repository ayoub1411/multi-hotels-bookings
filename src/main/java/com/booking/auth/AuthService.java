package com.booking.auth;

import com.booking.dto.LoginRequest;
import com.booking.dto.LoginResponse;
import com.booking.dto.RegistrationRequest;
import com.booking.entities.AppUser;

public interface AuthService {


    public void registerClient(RegistrationRequest request);
    public void registerAdmin(RegistrationRequest request);


    public void activateAccount(String token);


    public LoginResponse login(LoginRequest request);

    public AppUser loadUserByUsername(String email);


}
