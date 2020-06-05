package com.food.table.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.food.table.constant.ApplicationConstants;
import com.food.table.constant.OfferStateEnum;
import com.food.table.constant.OfferTypeEnum;
import com.food.table.dto.OfferMonitor;
import com.food.table.dto.Offers;
import com.food.table.dto.Order;
import com.food.table.dto.Restaurant;
import com.food.table.dto.RestaurantOffers;
import com.food.table.dto.UserAccount;
import com.food.table.dto.UserOffers;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.OfferResponseModel;
import com.food.table.model.OffersModel;
import com.food.table.model.RestaurantOfferModel;
import com.food.table.model.UserOfferModel;
import com.food.table.model.UserOfferMonitorResponse;
import com.food.table.model.ValidateCouponRequest;
import com.food.table.model.ValidateCouponResponse;
import com.food.table.repo.OfferMonitorRepository;
import com.food.table.repo.OffersRepository;
import com.food.table.repo.OrderRepository;
import com.food.table.repo.RestaurantOffersRepository;
import com.food.table.repo.RestaurantRepository;
import com.food.table.repo.UserOfferRepository;
import com.food.table.repo.UserRepository;
import com.food.table.service.OfferService;
import com.food.table.util.UserUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OfferServiceImpl implements OfferService {

	@Autowired
	private OffersRepository offerRepository;

	@Autowired
	private RestaurantOffersRepository restaurantOfferRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserOfferRepository userOfferRepository;

	@Autowired
	private OfferMonitorRepository offerMonitorRepository;
	
	@Autowired
	private UserUtil userUtil;
	
	@Override
	public void addoffers(OffersModel offersModel) {
		Offers offers = new Offers();
		offers.setOfferCode(offersModel.getOfferCode());
		offers.setOfferAmount(offersModel.getOfferAmount());
		offers.setPercentage(offersModel.isPercentage());
		offers.setMinBillAmount(offersModel.getMinBillAmount());
		offers.setMaxOfferAmount(offersModel.getMaxOfferAmount());
		offers.setState(OfferStateEnum.getValue(offersModel.getState()));
		offers.setExpirationDate(offersModel.getExpirationDate());
		offers.setUsageCount(offersModel.getUsageCount());
		offers.setUsageType(offersModel.getUsageType());
		offers.setOfferType(OfferTypeEnum.getValue(offersModel.getOfferType()));
		offers.setAllRestaurant(offersModel.isAllRestaurant());
		offers.setAllUsers(offersModel.isAllUsers());
		Offers offerResponse = offerRepository.save(offers);
		List<RestaurantOffers> restaurantOffersList = new ArrayList<RestaurantOffers>();
		List<UserOffers> userOffersList = new ArrayList<UserOffers>();
		if (offersModel.isAllRestaurant() && !offersModel.isAllUsers()) {
			List<UserOfferModel> users = offersModel.getUsers();
			users.parallelStream().forEach(data -> {
				Optional<UserAccount> useraccount = userRepository.findById(data.getUserId());
				if (useraccount.isPresent()) {
					UserOffers user = UserOffers.builder().allrestaurant(offersModel.isAllRestaurant())
							.useraccount(useraccount.get()).offers(offerResponse).usageCount(data.getUsageCount())
							.expirationDate(data.getExpirationDate()).build();
					userOffersList.add(user);
				} else {
					log.error("Invalid user id for adding offers" + offersModel.getOfferCode());
					offerRepository.delete(offers);
					throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_USER_ID);

				}
			});
		} else if ((!offersModel.isAllRestaurant() && offersModel.isAllUsers())
				|| (!offersModel.isAllRestaurant() && !offersModel.isAllUsers())) {
			List<RestaurantOfferModel> restaurantId = offersModel.getRestaurants();
			restaurantId.parallelStream().forEach(res -> {
				Optional<Restaurant> restaurant = restaurantRepository.findById(res.getRestaurantId());
				if (restaurant.isPresent()) {
					if (!res.isAllUsers()) {
						List<UserOfferModel> userOfferModel = res.getUsers();
						userOfferModel.parallelStream().forEach(user -> {
							Optional<UserAccount> useraccount = userRepository.findById(user.getUserId());
							if (useraccount.isPresent()) {
								RestaurantOffers restaurantOffers = RestaurantOffers.builder().offers(offerResponse)
										.restaurant(restaurant.get()).expirationDate(res.getExpirationDate())
										.allUsers(res.isAllUsers()).usageCount(user.getUsageCount())
										.useraccount(useraccount.get()).userExpirationDate(user.getExpirationDate())
										.build();
								restaurantOffersList.add(restaurantOffers);
							} else {
								log.error("Invalid user id for adding offers" + offersModel.getOfferCode());
								offerRepository.delete(offers);
								throw new ApplicationException(HttpStatus.BAD_REQUEST,
										ApplicationErrors.INVALID_USER_ID);

							}
						});
					} else {
						RestaurantOffers restaurantOffers = RestaurantOffers.builder().offers(offerResponse)
								.restaurant(restaurant.get()).expirationDate(res.getExpirationDate())
								.allUsers(res.isAllUsers()).userExpirationDate(res.getExpirationDate()).build();
						restaurantOffersList.add(restaurantOffers);
					}
				} else {
					log.error("Invalid restaurant id for adding offers" + offersModel.getOfferCode());
					offerRepository.delete(offers);
					throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
				}
			});
		}
		userOfferRepository.saveAll(userOffersList);
		restaurantOfferRepository.saveAll(restaurantOffersList);
	}

	@Override
	public List<OfferResponseModel> getCouponsForUser(Integer restaurantId) {
		UserAccount currentUser = userUtil.getCurrentUserId();
		List<OfferResponseModel> offerResponseModel = new ArrayList<OfferResponseModel>();
		List<Offers> offers = offerRepository.findallCoupons(OfferStateEnum.getValue(ApplicationConstants.ACTIVE));
		parseOfferResponse(offerResponseModel, offers);
		offers.clear();
		offers = offerRepository.findallRestaurantCoupons(OfferStateEnum.getValue(ApplicationConstants.ACTIVE),
				restaurantId, currentUser.getId());
		parseOfferResponse(offerResponseModel, offers);
		offers.clear();
		offers = offerRepository.findallUserCoupons(OfferStateEnum.getValue(ApplicationConstants.ACTIVE), currentUser.getId());
		parseOfferResponse(offerResponseModel, offers);
		return offerResponseModel;
	}

	@Override
	public ValidateCouponResponse validateCouponsService(ValidateCouponRequest validateCouponRequest) {
		OfferMonitor offerMonitor = null;
		UserAccount currentUser = ObjectUtils.defaultIfNull(validateCouponRequest.getUserAccount(), userUtil.getCurrentUserId());
		ValidateCouponResponse validateResponse = new ValidateCouponResponse();
		Optional<Restaurant> restaurant = restaurantRepository.findById(validateCouponRequest.getRestaurantId());
		Optional<Order> order = orderRepository.findById(validateCouponRequest.getOrderId());
		double finalAmount = 0;
		Date currentDate = new Date();
		if (restaurant.isPresent() && order.isPresent()) {
			Offers offer = offerRepository.findByOfferCode(validateCouponRequest.getCouponCode());
			if(Objects.isNull(offer)) {
				log.error("Offer code is not exist");
				throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_OFFER_CODE);
			}
			DateTime currentDateTime=new DateTime();
			validateResponse.setOffer(offer);
			long count= offerMonitorRepository.countByCreatedAtBetweenAndOffers(currentDateTime.minusDays(offer.getUsageType()).toDate(), currentDateTime.toDate(), offer);
			if (offer != null && offer.getMinBillAmount() <= validateCouponRequest.getBillAmount()
					&& checkDate(currentDate, offer.getExpirationDate()) && count <offer.getUsageCount()) {
				if (offer.isAllRestaurant() && offer.isAllUsers()) {
					finalAmount = calculateOfferAmount(validateCouponRequest, offer);
					offerMonitor = OfferMonitor.builder().offers(offer).restaurant(restaurant.get())
							.useraccount(currentUser).order(order.get()).offerType(offer.getOfferType())
							.billAmount(validateCouponRequest.getBillAmount()).offerAmount(finalAmount).build();
				} else if (offer.isAllRestaurant() && !offer.isAllUsers()) {
					UserOffers useroffers = userOfferRepository.findByUseraccount(currentUser);
					if (useroffers != null) {
						if (checkDate(currentDate, useroffers.getExpirationDate())) {
							finalAmount = calculateOfferAmount(validateCouponRequest, offer);
							useroffers.setUsageCount(useroffers.getUsageCount()-1);
							userOfferRepository.save(useroffers);
							offerMonitor = OfferMonitor.builder().offers(offer).restaurant(restaurant.get())
									.useraccount(currentUser).order(order.get()).offerType(offer.getOfferType())
									.billAmount(validateCouponRequest.getBillAmount()).offerAmount(finalAmount).build();
						} else {
							log.error("Offer is expired");
							throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.OFFER_EXPIRED);
						}
					} else {
						log.error("User is not eligible for this offer");
						throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.NOT_ELIGIBLE);
					}
				} else {
					List<RestaurantOffers> restaurantOffer = restaurantOfferRepository.findByOffers(offer);
					for(RestaurantOffers restaurantOffers:restaurantOffer){
						if ((restaurantOffers.isAllUsers()
								&& restaurantOffers.getRestaurant().getId() == validateCouponRequest.getRestaurantId())
								|| (restaurantOffers.getUseraccount().getId() == currentUser.getId()
										&& restaurantOffers.getRestaurant().getId() == validateCouponRequest
												.getRestaurantId())) {
							if (checkDate(currentDate, restaurantOffers.getExpirationDate())
									&& checkDate(currentDate, restaurantOffers.getUserExpirationDate())) {
								finalAmount = calculateOfferAmount(validateCouponRequest, offer);
								if(!restaurantOffers.isAllUsers()) {
								restaurantOffers.setUsageCount(restaurantOffers.getUsageCount()-1);
								restaurantOfferRepository.save(restaurantOffers);
								}
								offerMonitor = OfferMonitor.builder().offers(offer).restaurant(restaurant.get())
										.useraccount(currentUser).order(order.get()).offerType(offer.getOfferType())
										.billAmount(validateCouponRequest.getBillAmount()).offerAmount(finalAmount).build();
							} else {
								log.error("Offer is expired");
								throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.OFFER_EXPIRED);
							}
						} else {
							log.error("User is not eligible for this offer");
							throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.NOT_ELIGIBLE);
						}
					}					
				}
			} else {
				log.error("Invalid Coupoun Code");
				throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_COUPON_CODE);
			}
		} else {
			log.error("Invalid user ID");
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_REQUIRED_ID_FOR_OFFER);
		}
		if (finalAmount > 0) {
			validateResponse.setBillAmount(validateCouponRequest.getBillAmount());
			validateResponse.setOfferAmount(finalAmount);
			validateResponse.setOfferCode(validateCouponRequest.getCouponCode());
		}
		if (offerMonitor != null) {
			offerMonitorRepository.save(offerMonitor);
		}
		return validateResponse;
	}

	private double calculateOfferAmount(ValidateCouponRequest validateCouponRequest, Offers offer) {
		double finalAmount;
		if (offer.isPercentage()) {
			double percentagevalue = offer.getOfferAmount() / 100;
			finalAmount = validateCouponRequest.getBillAmount() * percentagevalue;
			if (finalAmount > offer.getMaxOfferAmount()) {
				finalAmount = offer.getMaxOfferAmount();
			}
		} else {
			finalAmount = validateCouponRequest.getBillAmount() - offer.getMaxOfferAmount();
			if (finalAmount > offer.getMaxOfferAmount()) {
				finalAmount = offer.getMaxOfferAmount();
			}
		}
		return finalAmount;
	}

	private void parseOfferResponse(List<OfferResponseModel> offerResponseModel, List<Offers> offers) {
		if (offers != null && !offers.isEmpty()) {
			offers.parallelStream().forEach(data -> {
				offerResponseModel.add(new OfferResponseModel(data.getOfferCode(), data.getOfferAmount(),
						data.getMinBillAmount(), data.getMaxOfferAmount(), data.getExpirationDate()));
			});
		}
	}

	private boolean checkDate(Date currentDate, Date expirationDate) {
		return currentDate.equals(expirationDate) || currentDate.before(expirationDate);
	}

	@Override
	public List<UserOfferMonitorResponse> getUserUsedOffers() {
		UserAccount currentUser = userUtil.getCurrentUserId();
		List<UserOfferMonitorResponse> userOfferMonitorResponse = new ArrayList<UserOfferMonitorResponse>();
		List<OfferMonitor> offerMonitor = offerMonitorRepository.findByUseraccount(currentUser);
		offerMonitor.parallelStream().forEach(offer -> {
			UserOfferMonitorResponse userOffer=new UserOfferMonitorResponse();
			userOffer.setCreatedAt(offer.getCreatedAt());
			userOffer.setOfferAmount(offer.getOfferAmount());
			userOffer.setRestaurantName(offer.getRestaurant().getRestaurantName());
			userOfferMonitorResponse.add(userOffer);
		});
		return userOfferMonitorResponse;
	}
}
