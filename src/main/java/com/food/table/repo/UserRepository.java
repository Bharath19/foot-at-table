package com.food.table.repo;

import com.food.table.dto.UserAccount;
import com.food.table.model.FavoriteRestaurantModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Integer> {

	@Query(value = "SELECT * FROM user_account u where u.email=:email", nativeQuery = true)
	UserAccount findUserByEmailId(@Param("email") String email);

	@Query(value = "SELECT * FROM user_account u where u.phone_no=:phone_no", nativeQuery = true)
	UserAccount findUserByPhoneNo(@Param("phone_no") long phoneNo);

	@Query(value = "SELECT * FROM user_account u where u.refresh_token=:refresh_token", nativeQuery = true)
	UserAccount findUserByRefreshToken(@Param("refresh_token") String refreshToken);

	@Query(value = "SELECT distinct(res.id) as restaurantId, res.restaurant_name as restaurantName, res.image_url as ImageUrl, res.rating as rating, T.distance as distance, res.status as status FROM restaurant_detail res JOIN(SELECT  ad.id, (6371 * acos (cos ( radians(:latitude) ) * cos( radians( ad.lattitude ) )* cos( radians( ad.longitude ) - radians(:longitude) ) + sin ( radians(:latitude) )* sin( radians( ad.lattitude ) ))) AS distance FROM address_detail ad) AS T ON res.address_id = T.id JOIN timings tim ON tim.restaurant_id=res.id JOIN user_favorite_restaurants ufr on ufr.restaurant_id = res.id where res.state=:state and ufr.user_id = :userId ORDER BY T.distance", nativeQuery = true)
	Page<FavoriteRestaurantModel> findByFavoriteRestaurant(@Param("state") String state, @Param("latitude") String latitude,
			@Param("longitude") String longitude, @Param("userId") int userId,
			Pageable pageable);
}
