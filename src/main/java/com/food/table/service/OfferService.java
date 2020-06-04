package com.food.table.service;

import java.util.List;

import com.food.table.model.OfferResponseModel;
import com.food.table.model.OffersModel;
import com.food.table.model.UserOfferMonitorResponse;
import com.food.table.model.ValidateCouponRequest;
import com.food.table.model.ValidateCouponResponse;

public interface OfferService {
	
	public void addoffers(OffersModel offersModel);
	
	public List<OfferResponseModel> getCouponsForUser(Integer restaurantId);
	
	public ValidateCouponResponse validateCouponsService(ValidateCouponRequest validateCouponRequest);
	
	public List<UserOfferMonitorResponse> getUserUsedOffers();
}
