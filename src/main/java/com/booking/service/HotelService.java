package com.booking.service;

import com.booking.dto.CityDto;
import com.booking.dto.HotelDto;
import com.booking.dto.HotelRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HotelService {
    public HotelDto getHotelById(Long id);

    public void likeHotel(Long hotelId, Authentication authentication);

    public List<HotelDto> favoriteHotels(Authentication authentication);


    public List<CityDto> getAllCities();

    public HotelDto saveHotel(HotelRequestDto hotel, MultipartFile image);


    public void deleteHotelById(Long id);

    public List<HotelDto> findHotels(String name, Long city, int stars, Pageable pageable);


    public HotelDto uploadHotelImage(Long hotelId, MultipartFile file);

    public HotelDto updateHotel(Long hotelId, HotelRequestDto hotel, MultipartFile image);


}
