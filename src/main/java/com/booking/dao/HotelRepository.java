package com.booking.dao;


import com.booking.entities.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HotelRepository extends JpaRepository<Hotel,Long> {



Page<Hotel> findAll(Specification<Hotel> specification,Pageable pageable);


    @Query("select h from Hotel h where h.name like %:x% ")
    Page<Hotel> findHotelsByName(@Param("x") String name, Pageable pageable);

    @Query("select h from Hotel h where h.city.id= :id ")
    Page<Hotel> findHotelsByCity(@Param("id") Long id,Pageable pageable);

    @Query("select h from Hotel h where h.stars= :y ")
    Page<Hotel> findHotelsByStar(@Param("y") int star,Pageable pageable);

    @Query("select h from Hotel h where h.city.id= :y  and h.stars= :x ")
   Page<Hotel> findHotelByStarAndCity(@Param("x") int star,@Param("y") long id,Pageable pageable);


}
