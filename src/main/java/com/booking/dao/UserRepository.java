package com.booking.dao;


import com.booking.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser,String> {


public Optional<AppUser> findAppUserByEmail(String email);


@Modifying
@Transactional
@Query("update AppUser user set user.enabled=true where user.email= :email ")
public void enableUser(@Param("email") String email);



}

