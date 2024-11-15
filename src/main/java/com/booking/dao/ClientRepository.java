package com.booking.dao;

import com.booking.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, String> {

    @Query("SELECT c FROM Client c WHERE c.email LIKE %:keyword% OR c.lastname LIKE %:keyword% OR c.firstname LIKE %:keyword%")
    Page<Client> findClientByLnameOrFnameOrEmail(@Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT c from Client c where c.enabled = false ")
    Page<Client> findPassiveClients(Pageable pageable);


}
