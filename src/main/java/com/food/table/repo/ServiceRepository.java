package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Services;

@Repository
public interface ServiceRepository extends JpaRepository<Services,Integer>{

}
