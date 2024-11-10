package com.booking.dao;

import com.booking.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long> {


    @Query("SELECT r FROM Room r WHERE r.price BETWEEN :minPrice AND :maxPrice AND r.hotel.id = :hotelId AND r.numberOfRooms = :rooms AND r.id NOT IN (" +
            "SELECT res.room.id FROM Reservation res WHERE res.checkOutDate > :checkInDate AND res.checkInDate < :checkOutDate)")
    List<Room> findAvailableRooms(@Param("hotelId")Long hotelId,@Param("minPrice") double minPrice,
                                  @Param("maxPrice") double maxPrice,
                                  @Param("checkInDate") LocalDate checkInDate  ,
                                  @Param("checkOutDate") LocalDate checkOutDate,@Param("rooms")int rooms);

    public Page<Room> findAll(Specification<Room> specification, Pageable pageable);
    @Query("select r from Room r where r.hotel.id= :x")
    Page<Room> findRoomByHotelId(@Param("x") Long id, Pageable pageable);





    @Query("SELECT r FROM Room r WHERE r.hotel.id = :id AND r.price BETWEEN :min AND :max")
    List<Room> findRoomByHotelIdAndPriceBetween(@Param("id") Long id,
                                                @Param("min") double min,
                                                @Param("max") double max);








}
