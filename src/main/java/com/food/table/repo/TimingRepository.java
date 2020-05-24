package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.table.dto.Timings;

public interface TimingRepository extends JpaRepository<Timings, Long> {

}
