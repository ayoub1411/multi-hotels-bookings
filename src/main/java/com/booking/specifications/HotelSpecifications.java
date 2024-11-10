package com.booking.specifications;


import com.booking.entities.Hotel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
public class HotelSpecifications {


    public Specification<Hotel> nameContains(String keyword){




        return (root,criteria,builder)->{

           Predicate predicate= builder.like(root.get("name"),"%"+keyword+"%");
                   predicate.getExpressions()
                    .forEach(expression->{
                        System.out.println(expression.toString());
                    });
            System.out.println("operator : "+predicate.getOperator());

            return builder.like(root.get("name"),"%"+keyword+"%");


        };


    }

    public Specification<Hotel> starsEqual(int star){


        return (root,criteria,builder)->{

            return builder.equal(root.get("stars"),star);


        };
    }

    public Specification<Hotel> cityIdIs(Long cityId){


        return (root, query, criteriaBuilder) -> {

                  return criteriaBuilder.equal(root.get("city").get("id"),cityId);



        };
    }

}
