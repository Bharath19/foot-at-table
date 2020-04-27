package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Types;

@Repository
public interface TypesRepository extends JpaRepository<Types, Integer>{

}
