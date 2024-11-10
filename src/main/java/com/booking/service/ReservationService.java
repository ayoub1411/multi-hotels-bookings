package com.booking.service;

import com.booking.dto.ReservationRequestDto;
import com.booking.dto.ReservationResponseDto;
import com.booking.entities.Reservation;
import com.booking.entities.ReservationState;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;


import java.util.List;

public interface ReservationService {


    ReservationResponseDto createReservation(ReservationRequestDto request, Authentication client);
    ReservationResponseDto checkoutReservation(Long reservationId, Authentication client);
    ReservationResponseDto cancelReservation(Long reservationId, Authentication client);
    List<ReservationResponseDto> clientReservations(String clientId, Authentication authentication,ReservationState state, Pageable pageable);

    List<ReservationResponseDto> findAllSortedByCheckInDESC(Pageable pageable);











    // complete reservation is by spring scheduler module

}
