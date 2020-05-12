package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.table.dto.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{

	Cart findByIdAndOrderId(int id, int order_id);

}
