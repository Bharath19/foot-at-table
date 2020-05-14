package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

}
