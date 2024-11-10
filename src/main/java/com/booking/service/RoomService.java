package com.booking.service;

import com.booking.dto.RoomDto;
import com.booking.dto.RoomRequest;
import com.booking.entities.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {



    public RoomDto save(RoomRequest request, Long hotelId, MultipartFile[] files);

    public List<RoomDto> roomsByHotelId(Long id,double min,double max,int numberOfRooms, Pageable pageable);



    public List<RoomDto> findAvailableRooms(Long id, double minPrice, double maxPrice, LocalDate checkIn,LocalDate checkOut,int numberofRooms);

    public RoomDto updateRoom(Long roomId,RoomRequest request,MultipartFile [] newImages);

    public RoomDto getRoomById(Long id);


    public void deleteRoom(Long id);






}
