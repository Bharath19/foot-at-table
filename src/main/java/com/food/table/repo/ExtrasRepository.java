package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Extras;

@Repository
public interface ExtrasRepository extends JpaRepository<Extras, Long>{

}
