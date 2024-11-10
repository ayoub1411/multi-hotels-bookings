package com.booking.service;

import com.booking.dao.PayementRepository;
import com.booking.dao.ReservationRepository;
import com.booking.dao.RoomRepository;
import com.booking.dao.UserRepository;
import com.booking.dto.ReservationRequestDto;
import com.booking.dto.ReservationResponseDto;
import com.booking.entities.*;
import com.booking.exception.UnavailableRoomException;
import com.booking.mappers.HotelMapper;
import com.booking.specifications.ReservationSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService{
    UserRepository userRepository;

    RoomRepository roomRepository;

    ReservationRepository reservationRepository;

    PayementRepository payementRepository;
    ReservationSpecification reservationSpecification;

    HotelMapper mapper;

    public ReservationServiceImpl(UserRepository userRepository,RoomRepository roomRepository
            ,ReservationRepository reservationRepository,PayementRepository payementRepository
            ,HotelMapper mapper,ReservationSpecification reservationSpecification){
        this.userRepository=userRepository;
        this.roomRepository=roomRepository;
        this.reservationRepository=reservationRepository;
        this.payementRepository=payementRepository;
        this.mapper=mapper;
        this.reservationSpecification=reservationSpecification;
    }

    private boolean availableForReserving(Long roomId, LocalDate in,LocalDate out){

        List<Reservation> reservations=reservationRepository.findByRoomId(roomId);

        for(Reservation reservation:reservations){

            if(out.isAfter(reservation.getCheckInDate())&&in.isBefore(reservation.getCheckOutDate()))
                return false;

        }

        return true;
    }
    private  double totalPriceOfReservation(LocalDate in,LocalDate out,double pricePerNight){
        long numberOfNights = ChronoUnit.DAYS.between(in, out);

        return numberOfNights*pricePerNight;

    }
    @Override
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto request, Authentication client) {
        Reservation reservation=new Reservation();

        reservation.setClient((Client) client.getPrincipal());

        reservation.setCheckInDate(request.getCheckIn());
        reservation.setCheckOutDate(request.getCheckOut());
        Room room=roomRepository.findById(request.getRoomId())
                .orElseThrow(()->new EntityNotFoundException("Cannot find a room with id :"+request.getRoomId()));


        if(availableForReserving(request.getRoomId(),request.getCheckIn(),request.getCheckOut()))

            reservation.setRoom(room);

        else

            throw new UnavailableRoomException("Sorry this room not available for booking in this period"+
                    " from "+request.getCheckIn() +" to "+request.getCheckOut());



        //at this point room is available
        Payement payement=new Payement();
        double totalPrice=totalPriceOfReservation(request.getCheckIn(),request.getCheckOut(),room.getPrice());
        payement.setAmount(totalPrice);

        payement.setPayed(false);
        payementRepository.save(payement);
        reservation.setPayement(payement);
        reservationRepository.save(reservation);

        return mapper.fromReservation(reservation);



    }

    @Override
    @Transactional
    public ReservationResponseDto checkoutReservation(Long reservationId, Authentication client) {

        Reservation reservation=reservationRepository.findById(reservationId)
                .orElseThrow(()->new RuntimeException("Reservation not exist to confirm"));

        if(!reservation.getClient().getEmail().equals(((Client)client.getPrincipal()).getEmail() )){
            throw new RuntimeException("cannot confirm a reservation that you do not own ");

        }

        reservation.setState(ReservationState.CONFIRMED);
        reservation.getPayement().setPayed(true);


        return mapper.fromReservation(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationResponseDto cancelReservation(Long reservationId, Authentication client) {

        Reservation reservation=reservationRepository.findById(reservationId)
                .orElseThrow(()->new RuntimeException("Reservation not exist to confirm"));

        if(reservation.getState().equals(ReservationState.COMPLETED))
            throw new RuntimeException("Cannot cancel a COMLETED reservation");


        reservation.setState(ReservationState.CANCELLED);

        reservation.getPayement().setPayed(false);


        return mapper.fromReservation(reservationRepository.save(reservation));


    }




    @Override
    public List<ReservationResponseDto> clientReservations(String clientId, Authentication connectedUser, ReservationState state, Pageable pageable) {




        Specification<Reservation> specification= Specification.where(null);
        specification=specification.and(reservationSpecification.clientId(clientId));
        if(state!=null)
            specification=specification.and(reservationSpecification.state(state));


        AppUser user=(AppUser) connectedUser.getPrincipal();

        if(! (user instanceof Admin) && !user.getId().equals(clientId))
                throw new RuntimeException("As  a client you cannot view reservation of client with id"+clientId);


        Page<Reservation> page=reservationRepository.findAll(specification,pageable);


        return page.getContent().stream()
                .map(mapper::fromReservation)
                .collect(Collectors.toList());
    }

    @Override //specific for admins
    public List<ReservationResponseDto> findAllSortedByCheckInDESC(Pageable pageable) {

        return reservationRepository.findAllReservationsSortedByCheckIn(pageable)
                .getContent().stream().map(mapper::fromReservation).collect(Collectors.toList());

    }



}
