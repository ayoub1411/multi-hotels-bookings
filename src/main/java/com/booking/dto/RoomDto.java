package com.booking.dto;

import java.util.List;

public class RoomDto {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public HotelDto getHotelDto() {
        return hotelDto;
    }

    public void setHotelDto(HotelDto hotelDto) {
        this.hotelDto = hotelDto;
    }

    Long id ;

    double price;

    double surface ;

    int numberOfRooms;


    HotelDto hotelDto;

    public List<RoomImageDto> getRoomImages() {
        return roomImages;
    }

    public void setRoomImages(List<RoomImageDto> roomImages) {
        this.roomImages = roomImages;
    }

    List<RoomImageDto> roomImages;

}
