package com.food.table.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Setup;

@Repository
public interface SetupRepository extends JpaRepository<Setup, Integer>{

	List<Setup> findByCodeIn(List<String> codes);

}
