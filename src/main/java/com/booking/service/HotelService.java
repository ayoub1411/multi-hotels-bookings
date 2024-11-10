package com.booking.service;

import com.booking.entities.*;
import com.booking.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import com.booking.exception.*;

public interface HotelService {
    public HotelDto getHotelById(Long id) ;
    public void likeHotel(Long hotelId, Authentication authentication);

    public List<HotelDto> favoriteHotels(Authentication authentication);


    public List<CityDto> getAllCities();

    public HotelDto saveHotel(HotelRequestDto hotel,MultipartFile image) ;


    public void deleteHotelById(Long id);

     public  List<HotelDto> findHotels(String name,Long city,int stars,Pageable pageable);


    public HotelDto uploadHotelImage(Long hotelId,MultipartFile file);

    public HotelDto updateHotel(Long hotelId,HotelRequestDto hotel,MultipartFile image);






}
