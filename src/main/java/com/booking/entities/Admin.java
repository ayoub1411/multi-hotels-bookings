package com.booking.entities;


import jakarta.persistence.Entity;

import java.util.Set;

@Entity

public class Admin extends AppUser {

    public Admin() {

        this.setAuthorities(Set.of(
                Authorities.CLIENT.toString(),
                Authorities.ADMIN.toString()
        ));

    }


}
