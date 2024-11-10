package com.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {


    @JsonProperty("acces-token")
    private String accesToken;
    @JsonProperty("refresh-token")
    private String refreshToken;

    public String getAccesToken() {
        return accesToken;
    }

    public void setAccesToken(String accesToken) {
        this.accesToken = accesToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
