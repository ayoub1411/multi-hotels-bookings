package com.booking.dao;


import com.booking.entities.Reservation;
import com.booking.entities.ReservationState;
import com.booking.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    @Query("SELECT r FROM Reservation r  ORDER BY r.checkInDate DESC")
    Page<Reservation> findAllReservationsSortedByCheckIn(Pageable pageable);


    List<Reservation> findByRoomId(Long id);

    @Query("select r from Reservation r where r.client.id= :x ")
    Page<Reservation> findReservationByClientId(@Param("x") String id, Pageable pageable);

    public Page<Reservation> findAll(Specification<Reservation> specification, Pageable pageable);

}
