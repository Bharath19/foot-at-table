package com.food.table.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.UserAccount;
import com.food.table.dto.UserOffers;

@Repository
public interface UserOfferRepository extends JpaRepository<UserOffers, Long>{

	UserOffers findByUseraccount(UserAccount useraccount);

}
