package com.booking.security;

import com.booking.entities.Client;
import com.booking.entities.Room;
import com.booking.exception.UserNotFoundException;
import jakarta.persistence.Access;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserDetailsService userDetailsService;
    private boolean isAuthenticationRequired(HttpServletRequest request){

        return !request.getServletPath().contains("/auth")
                && SecurityContextHolder.getContext().getAuthentication()==null;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("filter jwt ...");

        String authHeader=request.getHeader("Authorization");

        if(authHeader==null || !authHeader.startsWith("Bearer ") ||!isAuthenticationRequired(request)) {
            filterChain.doFilter(request, response);

            System.out.println("No Auth needed ");

            return;
        }


        //authentication is null here
        try {

            System.out.println("start jwt auth");
            String token = authHeader.substring(7);


            String username = jwtService.getSubject(token);

            UserDetails userDetails =userDetailsService.loadUserByUsername(username);

            if (userDetails == null) throw new UserNotFoundException("User not found with email : " + username);


            if(jwtService.isValidAndNoExpierd(userDetails,token)){


                //authenticate user

                Authentication authentication=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("user is authenticated");



            }

        }catch (Exception e){
            //for handling any kind of exception with @ControllerAdvice
            System.out.println("exception in jwt filter : "+e.getMessage());
            resolver.resolveException(request,response,null,e);
            return;
        }


        filterChain.doFilter(request,response);
















    }



}
