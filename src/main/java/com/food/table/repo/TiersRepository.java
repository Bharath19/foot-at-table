package com.food.table.repo;

import com.food.table.dto.Tiers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiersRepository extends JpaRepository<Tiers, Long> {
}
