package com.booking.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Client extends AppUser {

    public Client(){
        this.setAuthorities(Set.of(Authorities.CLIENT.toString()));

    }



    @OneToMany(fetch = FetchType.LAZY,mappedBy = "client")


    List<Reservation> reservations;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "client_favorites",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "hotel_id")
    )

    List<Hotel> favorites;



    public List<Hotel> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Hotel> favorites) {
        this.favorites = favorites;
    }



    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }




}
