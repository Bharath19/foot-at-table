package com.food.table.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Order;
import com.food.table.model.RevenueDetailsModel;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	@Query(value = "Select * from orders o where o.user_id = :userId", nativeQuery = true)
	Page<Order> findByUserId(int userId, Pageable pageable);

	@Query(value = "Select * from orders o where o.user_id = :userId and DATE(o.created_at)= DATE(:orderDate)", nativeQuery = true)
	Page<Order> findByUserIdAndCreatedAt(int userId, String orderDate, Pageable pageable);

	@Query(value = "Select * from orders o where o.user_id = :userId and o.state IN (:orderState)", nativeQuery = true)
	Page<Order> findByUserIdAndStateIn(int userId, List<String> orderState, Pageable pageable);

	@Query(value = "Select * from orders o where o.user_id = :userId and o.state IN (:orderState) and DATE(o.created_at)= DATE(:orderDate)", nativeQuery = true)
	Page<Order> findByUserIdAndStateInAndCreatedAt(int userId, List<String> orderState, String orderDate,
			Pageable pageable);

	@Query(value = "SELECT t.name as orderTypeName, count(o.type_id) as orderCount, sum(o.paid_price) as totalPrice FROM orders o inner join types t on t.id = o.type_id where o.restaurant_id= :restaurantId and DATE(o.created_at)= DATE(:orderDate) and  o.state = :orderState group by (type_id)", nativeQuery = true)
	List<RevenueDetailsModel> findRevenueDetais(int restaurantId, String orderDate, String orderState);

	@Query(value = "Select * from orders o inner join types t on t.id = o.type_id where o.restaurant_id = :restaurantId and t.name IN (:orderTypes) and o.state IN (:orderState)", nativeQuery = true)
	Page<Order> findByRestaurantAndTypeAndStateIn(int restaurantId, List<String> orderTypes, List<String> orderState,
			Pageable pageable);

	@Query(value = "Select * from orders o inner join types t on t.id = o.type_id where o.restaurant_id = :restaurantId and t.name IN (:orderTypes) and o.state IN (:orderState) and DATE(o.created_at)= DATE(:orderDate)", nativeQuery = true)
	Page<Order> findByRestaurantAndTypeAndStateInAndCreatedAt(int restaurantId, List<String> orderTypes,
			List<String> orderState, String orderDate, Pageable pageable);

	@Query(value = "Select * from orders o inner join types t on t.id = o.type_id where o.restaurant_id = :restaurantId and t.name IN (:orderTypes)", nativeQuery = true)
	Page<Order> findByRestaurantAndTypeIn(int restaurantId, List<String> orderTypes, Pageable pageable);

	@Query(value = "Select * from orders o inner join types t on t.id = o.type_id where o.restaurant_id = :restaurantId and t.name IN (:orderTypes) and DATE(o.created_at)= DATE(:orderDate)", nativeQuery = true)
	Page<Order> findByRestaurantAndTypeAndCreatedAt(int restaurantId, List<String> orderTypes, String orderDate,
			Pageable pageable);

	@Query(value = "Select * from orders o  inner join types t on t.id = o.type_id where o.restaurant_id = :restaurantId and restaurant_table_id = :restaurantTableId and t.name = (:orderType) and o.state IN (:orderState)", nativeQuery = true)
	Page<Order> findByRestaurantAndrestaurantTableAndStateInAndType(int restaurantId, int restaurantTableId,
			List<String> orderState, String orderType, Pageable pageable);

	@Query(value = "Select * from orders o  inner join types t on t.id = o.type_id where o.restaurant_id = :restaurantId and restaurant_table_id = :restaurantTableId and t.name = (:orderType) and o.state IN (:orderState) and DATE(o.created_at)= DATE(:orderDate)", nativeQuery = true)
	Page<Order> findByRestaurantAndrestaurantTableAndStateInAndCreatedAtAndType(int restaurantId, int restaurantTableId,
			List<String> orderState, String orderDate, String orderType, Pageable pageable);

	@Query(value = "Select * from orders o  inner join types t on t.id = o.type_id where o.restaurant_id = :restaurantId and restaurant_table_id = :restaurantTableId  and t.name = (:orderType)", nativeQuery = true)
	Page<Order> findByRestaurantAndrestaurantTableAndType(int restaurantId, int restaurantTableId, String orderType,
			Pageable pageable);

	@Query(value = "Select * from orders o inner join types t on t.id = o.type_id where o.restaurant_id = :restaurantId and restaurant_table_id = :restaurantTableId and DATE(o.created_at)= DATE(:orderDate)  and t.name = (:orderType)", nativeQuery = true)
	Page<Order> findByRestaurantAndrestaurantTableAndCreatedAtAndType(int restaurantId, int restaurantTableId,
			String orderDate, String orderType, Pageable pageable);

	Page<Order> findByRestaurant(int restaurantId, Pageable pageable);

	@Query(value = "Select * from orders o  where o.restaurant_id = :restaurantId DATE(o.created_at)= DATE(:orderDate)", nativeQuery = true)
	Page<Order> findByRestaurantAndCreatedAt(int restaurantId, String orderDate, Pageable pageable);

	@Query(value = "Select * from orders o  where o.restaurant_id = :restaurantId and o.state IN (:orderState)", nativeQuery = true)
	Page<Order> findByRestaurantAndStateIn(int restaurantId, List<String> orderState, Pageable pageable);

	@Query(value = "Select * from orders o  where o.restaurant_id = :restaurantId and o.state IN (:orderState) and DATE(o.created_at)= DATE(:orderDate)", nativeQuery = true)
	Page<Order> findByRestaurantAndStateInAndCreatedAt(int restaurantId, List<String> orderState, String orderDate,
			Pageable pageable);

}
