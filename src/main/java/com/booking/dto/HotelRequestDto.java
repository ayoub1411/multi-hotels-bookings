package com.booking.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HotelRequestDto {

    @NotBlank(message = "Le nom ne doit pas etre vide")
    private String name;

    @Max(value = 5, message = "Nombre d'etoiles d'hotel ne doit pas depasser 5")
    @Min(value = 1, message = "Nombre d'etoiles d'hotel ne doit pas etre inferieur a 1")
    private int stars;

    @NotBlank(message = "L'address ne doit pas etre vide")
    private String address;

    @NotNull(message = "La ville ne doit pas etre vide")
    private Long cityId;  // Assuming that the request will send the ID of the city

    // Constructors
    public HotelRequestDto() {}

    public HotelRequestDto(String name, int stars, String address, Long cityId) {
        this.name = name;
        this.stars = stars;
        this.address = address;
        this.cityId = cityId;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}