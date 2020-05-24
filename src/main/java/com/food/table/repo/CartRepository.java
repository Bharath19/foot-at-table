package com.food.table.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Cart;
import com.food.table.model.FoodHistoryProjectionModel;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	Cart findByIdAndOrderId(int id, int order_id);

	@Query(value = "SELECT c.food_id as foodId, f.name as foodName, f.image_url as foodImageUrl, count(*) as count, (select group_concat(ft.name) from food_tag_map fm inner join food_tag ft on ft.id = fm.food_tag_id where fm.food_id = f.id) as foodTagList FROM carts c inner join foods f on c.food_id = f.id where c.order_status= :orderStatus and DATE(c.created_at)= DATE(:orderDate) group by (c.food_id)", nativeQuery = true)
	List<FoodHistoryProjectionModel> findByOrderStatusAndUpdatedAt(String orderStatus, String orderDate);

}