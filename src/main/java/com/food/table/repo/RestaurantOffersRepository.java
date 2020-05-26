package com.food.table.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.Offers;
import com.food.table.dto.RestaurantOffers;

@Repository
public interface RestaurantOffersRepository extends JpaRepository<RestaurantOffers, Long>{

	void deleteByOffers(Offers offers);

	List<RestaurantOffers> findByOffers(Offers offers);

}
