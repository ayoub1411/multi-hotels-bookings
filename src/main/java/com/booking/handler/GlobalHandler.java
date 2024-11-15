package com.booking.handler;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobalHandler {


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(DisabledException e) {

        System.out.println(e.getClass());

        return ResponseEntity.status(BusinessErrorCodes.DISABLED_USER.getHttpStatus()).body(ExceptionResponse.builder().error(e.getMessage()).businessErrorDescription(BusinessErrorCodes.DISABLED_USER.getDescription()).businessErrorCode(BusinessErrorCodes.DISABLED_USER.getCode()).build());

    }
    //BadCredentialsException

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredientalException(BadCredentialsException e) {

        System.out.println(e.getClass());

        return ResponseEntity.status(BusinessErrorCodes.BAD_CREDENTIALS.getHttpStatus()).body(ExceptionResponse.builder().error(e.getMessage()).businessErrorDescription(BusinessErrorCodes.BAD_CREDENTIALS.getDescription()).businessErrorCode(BusinessErrorCodes.BAD_CREDENTIALS.getCode()).build());

    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException e) {

        System.out.println(e.getClass());

        return ResponseEntity.status(BusinessErrorCodes.NOT_AUTHENTICATED.getHttpStatus()).body(ExceptionResponse.builder().error(e.getMessage()).businessErrorDescription(BusinessErrorCodes.NOT_AUTHENTICATED.getDescription()).businessErrorCode(BusinessErrorCodes.NOT_AUTHENTICATED.getCode()).build());

    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccesDeniedException(AccessDeniedException e) {

        return ResponseEntity.status(BusinessErrorCodes.ACCESS_DENIED.getHttpStatus()).body(ExceptionResponse.builder().businessErrorDescription(BusinessErrorCodes.ACCESS_DENIED.getDescription()).businessErrorCode(BusinessErrorCodes.ACCESS_DENIED.getCode()).error("Your authority not allowed").build());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        System.out.println("error of validation !");
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors().forEach(error -> {
            //var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().validationErrors(errors).build());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(ExpiredJwtException e) {


        return ResponseEntity.status(BusinessErrorCodes.JWT_EXPIRED.getCode()).body(ExceptionResponse.builder().businessErrorCode(BusinessErrorCodes.JWT_EXPIRED.getCode()).businessErrorDescription(BusinessErrorCodes.JWT_EXPIRED.getDescription()).error("Token expired").build());

    }


    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(SignatureException e) {

        return ResponseEntity.status(BusinessErrorCodes.INCORRECT_JWT_SIGNATURE.getCode()).body(ExceptionResponse.builder().businessErrorCode(BusinessErrorCodes.INCORRECT_JWT_SIGNATURE.getCode()).businessErrorDescription(BusinessErrorCodes.INCORRECT_JWT_SIGNATURE.getDescription()).error("Invalid token signature").build());


    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(EntityNotFoundException ex) {

        return ResponseEntity.status(BusinessErrorCodes.ENTITY_NOT_FOUND.getCode()).body(ExceptionResponse.builder().businessErrorDescription(BusinessErrorCodes.ENTITY_NOT_FOUND.getDescription()).error(ex.getMessage()).businessErrorCode(BusinessErrorCodes.ENTITY_NOT_FOUND.getCode()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp) {
        exp.printStackTrace();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ExceptionResponse.builder().businessErrorDescription("Internal error, please contact the admin").error(exp.getMessage()).businessErrorCode(INTERNAL_SERVER_ERROR.value()).build());
    }


}
