package com.booking.security;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.Arrays;

@Configuration

public class SecConfig {

    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception{

        security.csrf(csrf->csrf.disable());
        security.authorizeHttpRequests((auth)->{

            auth.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll();

            auth.requestMatchers("/auth/**").permitAll();
            auth.requestMatchers("/rooms/image/**").permitAll();
            auth.requestMatchers("/hotels/image/**").permitAll();
            auth.requestMatchers("/hotels/like").hasAuthority("CLIENT");
            auth.requestMatchers("/reservations/admin/**").hasAuthority("ADMIN");

            auth.requestMatchers("/reservations/**").hasAuthority("CLIENT");

            auth.requestMatchers(HttpMethod.GET).hasAuthority("CLIENT");
            auth.requestMatchers(HttpMethod.POST).hasAuthority("ADMIN");
            auth.requestMatchers(HttpMethod.PUT).hasAuthority("ADMIN");
            auth.requestMatchers(HttpMethod.DELETE).hasAuthority("ADMIN");



        });

        security.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);



        security.exceptionHandling(handler->{

            handler.authenticationEntryPoint((req,resp,exp)->{
                resolver.resolveException(req,resp,null,exp);

            });

            handler.accessDeniedHandler(((req,resp,exp) -> {

                resolver.resolveException(req,resp,null,exp);

            }));
        });
        return security.build();
    }

    @Bean

    AuthenticationProvider provider(){

        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder());

        return  authenticationProvider;




    }




    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();

    }



    @Bean
    PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();

    }



}