package com.booking.web;


import com.booking.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import com.booking.dto.*;
import com.booking.dao.*;
import com.booking.entities.*;
import com.booking.mappers.*;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("cities")
public class CityController {

@Autowired
    HotelService hotelService;
@Autowired
HotelMapper mapper;

    @GetMapping
    public List<CityDto> cityDtos(){

        return hotelService
                .getAllCities()
               ;

    }
}
