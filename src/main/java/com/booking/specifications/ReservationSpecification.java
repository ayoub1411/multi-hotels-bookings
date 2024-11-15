package com.booking.specifications;

import com.booking.entities.Reservation;
import com.booking.entities.ReservationState;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ReservationSpecification {

    public Specification<Reservation> clientId(String id) {


        return (root, criteria, builder) -> {

            return builder.equal(root.get("client").get("id"), id);


        };
    }

    public Specification<Reservation> state(ReservationState state) {

        return (root, criteria, builder) -> {

            return builder.equal(root.get("state"), state);


        };
    }
}
