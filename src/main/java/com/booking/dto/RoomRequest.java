package com.booking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RoomRequest {


    @NotNull(message = "Please specify the price")

    private Double price;         // Changed from double to Double
    @NotNull(message = "Please specify the surface")
    @Min(value = 10,message = "Please enter a correct value for surface,surface should be greater than 10 m2")
    private Double surface;       // Changed from double to Double
    @NotNull(message = "Please specify the number of rooms")
    @Min(value = 1,message ="Number of Roms should be greater or equal  1 " )
    private Integer numberOfRooms; // Changed from int to Integer



    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }



}
