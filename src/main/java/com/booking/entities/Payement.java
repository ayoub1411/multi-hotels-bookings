package com.booking.entities;

import jakarta.persistence.*;

@Entity
public class Payement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    double amount;
    @OneToOne(cascade = CascadeType.REMOVE)
    Reservation reservation;

    boolean payed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }
}
