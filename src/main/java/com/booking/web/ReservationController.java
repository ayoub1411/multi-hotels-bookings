package com.booking.web;


import com.booking.dto.ReservationRequestDto;
import com.booking.dto.ReservationResponseDto;
import com.booking.entities.ReservationState;
import com.booking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/reservations")
public class ReservationController {
@Autowired
ReservationService reservationService;

    @PostMapping
    public ReservationResponseDto createReservation(@RequestBody ReservationRequestDto request, Authentication authentication){

        return reservationService.createReservation(request,authentication);


    }

    @GetMapping("/confirm/{id}")
    public ReservationResponseDto confirm(@PathVariable("id")Long id,Authentication authentication){
        return reservationService.checkoutReservation(id,authentication);
    }

    @GetMapping("/cancel/{id}")
    public ReservationResponseDto cancel(@PathVariable("id") Long id,Authentication authentication){

        return reservationService.cancelReservation(id,authentication);

    }


    @GetMapping("/client/{id}")
  ResponseEntity< List<ReservationResponseDto> >reservations(@PathVariable("id")String clientId
            , @RequestParam(value = "state",required = false)ReservationState state
            , @RequestParam(value = "page",defaultValue = "0") int page
            ,@RequestParam(value = "items",defaultValue = "4")int items
            ,Authentication authentication){
        return ResponseEntity.ok(reservationService.clientReservations(clientId,authentication,state,PageRequest.of(page,items)))
                ;

    }
    @GetMapping("/admin/history")
    List<ReservationResponseDto> find( @RequestParam(value = "page",defaultValue = "0") int page
            ,@RequestParam(value = "items",defaultValue = "4")int items){

        return reservationService.findAllSortedByCheckInDESC(PageRequest.of(page,items));



    }



}
