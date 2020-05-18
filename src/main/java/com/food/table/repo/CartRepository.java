package com.food.table.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	Cart findByIdAndOrderId(int id, int order_id);

	@Query(value = "SELECT * FROM carts c where c.order_status= :orderStatus and DATE(c.created_at)= DATE(:orderDate)", nativeQuery = true)
	ArrayList<Cart> findByOrderStatusAndUpdatedAt(String orderStatus, String orderDate);

}