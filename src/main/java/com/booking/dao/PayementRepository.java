package com.booking.dao;

import com.booking.entities.Payement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayementRepository extends JpaRepository<Payement,Long> {
}
