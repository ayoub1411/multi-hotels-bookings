package com.booking.dao;

import com.booking.entities.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ActivationTokenRespository extends JpaRepository<ActivationToken, Long> {

    public Optional<ActivationToken> findByToken(String token);


    @Transactional
    @Modifying
    @Query("update ActivationToken token set token.validatedAt =:date where token.token= :token ")

    public void updateConfirmationDate(@Param("token") String token, @Param("date") LocalDateTime date);


}
