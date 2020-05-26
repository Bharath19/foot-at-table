package com.food.table.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.table.dto.OfferMonitor;
import com.food.table.dto.Offers;
import com.food.table.dto.UserAccount;

@Repository
public interface OfferMonitorRepository extends JpaRepository<OfferMonitor, Long>{
	
	long countByCreatedAtBetweenAndOffers(Date from,Date to,Offers offers);

	List<OfferMonitor> findByUseraccount(UserAccount useraccount);

}
