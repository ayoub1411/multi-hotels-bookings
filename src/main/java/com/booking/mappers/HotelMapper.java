package com.booking.mappers;


import com.booking.storage.FileStorageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.booking.entities.*;
import com.booking.dto.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelMapper {


    FileStorageService storageService;

    public HotelMapper(FileStorageService service) {
        storageService = service;
    }

    public CityDto fromCity(City city) {


        CityDto dto = new CityDto();


        BeanUtils.copyProperties(city, dto);


        return dto;
    }


    public City fromCityDto(CityDto dto) {
        City c = new City();
        BeanUtils.copyProperties(dto, c);
        return c;

    }

    public Hotel fromHotelRequest(HotelRequestDto dto) {

        Hotel hotel = new Hotel();
        hotel.setAddress(dto.getAddress());
        hotel.setName(dto.getName());

        hotel.setStars(dto.getStars());

        return hotel;
    }


    public Address fromAddresDto(AddressDto dto) {

        Address address = new Address();
        address.setAdresse1(dto.getAdresse1());
        address.setAdresse2(dto.getAdresse2());
        address.setPays(address.getPays());
        address.setCodePostale(dto.getCodePostale());
        return address;
    }

    public HotelDto fromHotel(Hotel hotel) {

        HotelDto dto = new HotelDto();

        BeanUtils.copyProperties(hotel, dto);

        dto.setCity(this.fromCity(hotel.getCity()));


        return dto;


    }

    public Hotel fromHotelDto(HotelDto hotelDto) {

        Hotel hotel = new Hotel();

        BeanUtils.copyProperties(hotelDto, hotel);

        hotel.setCity(this.fromCityDto(hotelDto.getCity()));


        return hotel;
    }

    RoomImageDto fromRoomImage(RoomImage room) {

        RoomImageDto roomImageDto = new RoomImageDto();
        BeanUtils.copyProperties(room, roomImageDto);

        return roomImageDto;
    }

    public RoomDto fromRoom(Room room) {

        RoomDto roomDto = new RoomDto();
        BeanUtils.copyProperties(room, roomDto);

        roomDto.setHotelDto(this.fromHotel(room.getHotel()));
        List<RoomImageDto> rooms;

        if (room.getRoomImages() != null) {
            rooms = room.getRoomImages().stream()
                    .map(this::fromRoomImage).collect(Collectors.toList());

            roomDto.setRoomImages(rooms);
        }
        return roomDto;
    }

    public AddressDto fromAddress(Address address) {
        AddressDto dto = new AddressDto();
        dto.setAdresse1(address.getAdresse1());
        dto.setAdresse2(address.getAdresse2());
        dto.setPays(address.getPays());
        dto.setCodePostale(address.getCodePostale());
        dto.setVille(address.getVille());

        return dto;
    }

    public ClientResponse fromAppUser(AppUser user) {
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setAddress(this.fromAddress(user.getAddress()));
        clientResponse.setEmail(user.getEmail());
        clientResponse.setLastname(user.getLastname());
        clientResponse.setFirstname(user.getFirstname());
        clientResponse.setPhoneNumber(user.getPhoneNumber());
        clientResponse.setDateOfBirth(user.getDateOfBirth());
        clientResponse.setActive(user.isEnabled());
        clientResponse.setImage(user.getImageName());


        return clientResponse;
    }

    public ReservationResponseDto fromReservation(Reservation reservation) {

        ReservationResponseDto reservationResponseDto = new ReservationResponseDto();

        reservationResponseDto.setId(reservation.getId());
        reservationResponseDto.setCheckInDate(reservation.getCheckInDate());
        reservationResponseDto.setCheckOutDate(reservation.getCheckOutDate());

        reservationResponseDto.setTotalPrice(reservation.getTotalPrice());

        reservationResponseDto.setState(reservation.getState());

        reservationResponseDto.setClient(this.fromAppUser(reservation.getClient()));


        RoomDto roomDto = this.fromRoom(reservation.getRoom());

        reservationResponseDto.setRoom(roomDto);

        return reservationResponseDto;

    }


}
