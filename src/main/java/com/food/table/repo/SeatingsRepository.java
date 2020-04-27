package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Seatings;

@Repository
public interface SeatingsRepository extends JpaRepository<Seatings, Integer>{

}
