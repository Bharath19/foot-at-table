package com.food.table.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Offers;

@Repository
public interface OffersRepository extends JpaRepository<Offers, Long> {

	@Query(value = "select * from offers off where off.state=:state and off.all_restaurant=true and off.all_users=true and off.expiration_date>=now()", nativeQuery = true)
	List<Offers> findallCoupons(@Param(value = "state") String state);

	@Query(value = "select off.* from offers off join restaurant_offers  res on off.id=res.offers_id where  off.state=:state and off.expiration_date>=now() and res.expiration_date>=now() and res.user_expiration_date>=now() and res.restaurant_id=:restaurantId and (res.all_users=true or res.user_id=:userId)", nativeQuery = true)
	List<Offers> findallRestaurantCoupons(@Param(value = "state") String state,
			@Param(value = "restaurantId") Integer restaurantId, @Param(value = "userId") Integer userId);

	@Query(value = "select off.* from offers off join user_offers  usr on off.id=usr.offers_id where  off.state=:state and off.expiration_date>=now() and usr.expiration_date>=now() and usr.user_id=:userId", nativeQuery = true)
	List<Offers> findallUserCoupons(@Param(value = "state") String state,
			 @Param(value = "userId") Integer userId);

	Offers findByOfferCode(String couponCode);

}
