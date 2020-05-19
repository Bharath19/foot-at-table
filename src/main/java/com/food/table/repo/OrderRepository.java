package com.food.table.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Order;
import com.food.table.model.RevenueDetailsModel;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	ArrayList<Order> findByUserId(int userId);

	@Query(value = "SELECT t.name as orderTypeName, count(o.type_id) as orderCount, sum(o.paid_price) as totalPrice FROM orders o inner join types t on t.id = o.type_id where o.restaurant_id= :restaurantId and DATE(o.created_at)= DATE(:orderDate) and  o.state = :orderState group by (type_id)", nativeQuery = true)
	List<RevenueDetailsModel> findRevenueDetais(int restaurantId, String orderDate, String orderState);

}
