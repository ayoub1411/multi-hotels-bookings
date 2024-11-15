package com.booking.handler;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


public enum BusinessErrorCodes {

    DISABLED_USER(401, UNAUTHORIZED, "Please verify your account "),
    ENTITY_NOT_FOUND(404, NOT_FOUND, "Cannot find this entity "),
    INCORRECT_JWT_SIGNATURE(401, UNAUTHORIZED, "Invalid signature for JWT"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or Password is incorrect"),
    JWT_EXPIRED(401, UNAUTHORIZED, "Jwt expired "),
    NOT_AUTHENTICATED(401, UNAUTHORIZED, "Not Authenticated,you should send jwt token "),
    ACCESS_DENIED(403, FORBIDDEN, "Your Authority is not allowed for this action"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled");

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    private final int code;

    private final String description;

    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }


}
