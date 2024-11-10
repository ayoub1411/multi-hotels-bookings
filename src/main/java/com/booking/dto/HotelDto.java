package com.booking.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class HotelDto {



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public CityDto getCity() {
        return city;
    }

    public void setCity(CityDto city) {
        this.city = city;
    }

    Long id;
    @NotBlank(message = "Le nom ne doit pas etre vide")
    String name;


    @Max(value = 5,message = "Nombre d'etoiles d'hotel ne doit pas depasser 5")
    @Min(value = 1,message = "Nombre d'etoiles d'hotel ne doit pas etre inferieur a 1")
    int stars;

    CityDto city;

    @NotBlank(message = "L'address ne doit pas etre vide")
    String address;

    String imageName;







    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }



    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


}
