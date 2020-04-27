package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Payments;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Integer> {

}
