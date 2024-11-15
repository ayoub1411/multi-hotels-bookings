package com.booking.auth;

import com.booking.dao.ActivationTokenRespository;
import com.booking.dao.UserRepository;
import com.booking.dto.LoginRequest;
import com.booking.dto.LoginResponse;
import com.booking.dto.RegistrationRequest;
import com.booking.entities.ActivationToken;
import com.booking.entities.Admin;
import com.booking.entities.AppUser;
import com.booking.entities.Client;
import com.booking.exception.PasswordConfirmationException;
import com.booking.exception.UserAlreadyExistException;
import com.booking.mappers.HotelMapper;
import com.booking.notifications.NotificationService;
import com.booking.security.JwtService;
import com.booking.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;


    NotificationService emailService;
    @Value("${activation.token.length}")
    private int activationTokenLength;

    @Value("${activation.token.delay}")
    private int activationTokenDelay;

    ActivationTokenRespository activationTokenRespository;
    HotelMapper mapper;
    AuthenticationManager authenticationManager;

    UserDetailsService userDetailsService;
    JwtService jwtService;
    FileStorageService storageService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder encoder, @Qualifier("email") NotificationService notificationService,

                           FileStorageService storageService, HotelMapper mapper, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, ActivationTokenRespository tokenRespository, JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = encoder;
        activationTokenRespository = tokenRespository;
        this.emailService = notificationService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.mapper = mapper;
        this.storageService = storageService;

    }

    @Override
    @Transactional
    public void registerClient(RegistrationRequest request, MultipartFile image) {

        register(request, image, false);


    }

    @Override
    @Transactional
    public void registerAdmin(RegistrationRequest request, MultipartFile image) {

        register(request, image, true);


    }


    @Override
    public LoginResponse login(LoginRequest request) {


        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        Object principal = authentication.getPrincipal();

        if (principal instanceof Client)
            System.out.println("client login !");

        if (principal instanceof Admin)
            System.out.println("admin login !");

        String acces = jwtService.buildAccesToken((UserDetails) principal);
        String refresh = jwtService.buildRefreshToken((UserDetails) principal);

        LoginResponse response = new LoginResponse();
        response.setAccesToken(acces);
        response.setRefreshToken(refresh);
        return response;
    }

    @Override
    public AppUser loadUserByUsername(String email) {
        return (AppUser) userDetailsService.loadUserByUsername(email);
    }

    @Override
    public void uploadProfilePicture(Authentication connectedUser, MultipartFile image) {

        AppUser user = (AppUser) connectedUser.getPrincipal();

        String name = storageService.saveProfileImage(image);
        user.setImageName(name);

        userRepository.save(user);


    }


    private void register(RegistrationRequest request, MultipartFile image, boolean isAdmin) {

        if (!userRepository.findAppUserByEmail(request.getEmail()).isEmpty())
            throw new UserAlreadyExistException("User with email " + request.getEmail() + " already exist !");

        if (!request.getPassword().equals(request.getConfirmPassword()))
            throw new PasswordConfirmationException(" Passwords do not mach");

        AppUser user = null;
        if (isAdmin)
            user = new Admin();
        else
            user = new Client();

        user.setAddress(mapper.fromAddresDto(request.getAddress()));


        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDateOfBirth(request.getDateOfBirth());
        user.setEnabled(false);
        String imagePath = storageService.saveProfileImage(image);
        user.setImageName(imagePath);
        user = userRepository.save(user);//Registration done

        //create activation token


        ActivationToken token = new ActivationToken();
        token.setToken(generateToken(activationTokenLength));

        token.setUser(user);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(activationTokenDelay));
        activationTokenRespository.save(token);

        System.out.println(activationTokenDelay);
        System.out.println(activationTokenLength);

        //send email contain token
        emailService.sendMessage(request.getEmail(), "Confirmation Email for Booking App "
                , "This is the activatin code : " + token.getToken());


    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})

    public void activateAccount(String token) {

        ActivationToken savedToken = activationTokenRespository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));


        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            throw new RuntimeException("your activation token is expired");

        }

        activationTokenRespository.updateConfirmationDate(token, LocalDateTime.now());

        userRepository.enableUser(savedToken.getUser().getEmail());


    }

    private String generateToken(int length) {
        String token = "";

        SecureRandom secureRandom = new SecureRandom();
        String numbers = "0123456789";
        for (int i = 0; i < length; i++) {

            token += secureRandom.nextInt(numbers.length());
        }

        return token;

    }


}
