package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Diets;

@Repository
public interface DietRepository  extends JpaRepository<Diets, Integer>{

}
