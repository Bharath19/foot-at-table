package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.table.dto.Cuisines;

public interface CuisinesRepository extends JpaRepository<Cuisines, Integer>{

}
