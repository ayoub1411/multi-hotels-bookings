package com.booking.specifications;

import com.booking.entities.Room;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
public class RoomSpecification {


   public Specification<Room> priceBetween(double min,double max){

        return new Specification<Room>() {

            @Override
            public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {


                return  criteriaBuilder.between(root.get("price"),min,max);

            }
        };


    }

    public Specification<Room> hotelId(Long id){

        return (root,query,builder)-> builder.equal(root.get("hotel").get("id"),id);
    }

    public Specification<Room> numberOfRooms(int numberOfRooms){

        return (root,query,builder)-> builder.equal(root.get("numberOfRooms"),numberOfRooms);


    }


}
