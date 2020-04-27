package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.table.dto.SearchType;

public interface SearchTypeRepository extends JpaRepository<SearchType, Integer> {

}
