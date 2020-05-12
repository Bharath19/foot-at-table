package com.food.table.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

	ArrayList<Order> findByUserId(int userId);

}
