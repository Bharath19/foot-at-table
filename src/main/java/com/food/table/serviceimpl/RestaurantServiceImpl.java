package com.food.table.serviceimpl;

import com.food.table.constant.ApplicationConstants;
import com.food.table.constant.RestaurantStateEnum;
import com.food.table.constant.RestaurantStatusEnum;
import com.food.table.dto.*;
import com.food.table.email.EmailModel;
import com.food.table.email.EmailService;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.*;
import com.food.table.repo.*;
import com.food.table.service.CustomUserDetailsService;
import com.food.table.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.SloppyMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	private RestaurantRepository restaurantepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TiersRepository tiersRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private DietRepository dietRepository;

	@Autowired
	private CuisinesRepository cuisinesRepository;

	@Autowired
	private SearchTypeRepository searchTypeRepository;

	@Autowired
	private SeatingsRepository seatingsRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private PaymentsRepository paymentsRepository;

	@Autowired
	private TypesRepository typesRepository;

	@Autowired
	private TimingRepository timingRepository;

	@Autowired
	private EmailService emailService;

    @Autowired
    private CustomUserDetailsService userDetailsService;



	@Override
	public void addRestaurant(RestaurantModel restaurantModel) {
		log.info("Entering add new restaurant for : "+restaurantModel.getRestaurantName());
		Restaurant restaurant = parseRestaurantValue(restaurantModel);
        Restaurant savedRestaurant = new Restaurant();
		try {
            savedRestaurant = restaurantepository.save(restaurant);
		} catch (Exception e) {
			log.error("Addind new restaurant is failed for : "+restaurantModel.getRestaurantName());
			throw new ApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR,
					ApplicationErrors.ADD_RESTAURANT_FAILED);
		}
        AuthRequest authRequest = AuthRequest.builder().userName(restaurantModel.getEmailId()).password(restaurantModel.getPassword()).build();
        userDetailsService.createRestaurantUser(authRequest, savedRestaurant);
		log.info("Exiting add new restaurant is success for : "+restaurantModel.getRestaurantName());
	}

	@Override
	public void deleteRestaurant(int id) {
		log.info("Entering delete restaurant for : "+id);
		Optional<Restaurant> restaurant = restaurantepository.findById(id);
		if (!restaurant.isPresent()) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		}
		restaurant.get().setState(RestaurantStateEnum.getValue(ApplicationConstants.deleteState));
		try {
            userDetailsService.changeEmployeeUserStatus(restaurant.get().getEmailId(), "inactive");
			restaurantepository.save(restaurant.get());
		} catch (Exception e) {
			throw new ApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR,
					ApplicationErrors.ADD_RESTAURANT_FAILED);
		}
		log.info("Exiting delete restaurant is success for : "+id);
	}

	@Override
	public List<RestaurantGetModel> getAllRestaurant(int from, int limit, String latitude, String longitude,
			String km) {
		log.info("Entering get all confirmed restaurant from: "+from +" limit : "+limit);
		Pageable pageable = PageRequest.of(from, limit);
		Page<Restaurant> restaurants = null;
		String currentDay = LocalDate.now().getDayOfWeek().toString();
		if (latitude != null && longitude != null && km != null) {
			restaurants = restaurantepository.findByDistanceFilter(
					RestaurantStateEnum.getValue(RestaurantStateEnum.getValue(ApplicationConstants.confirmedState)),
					latitude, longitude, km, currentDay, pageable);
		} else if (latitude != null && longitude != null) {
			restaurants = restaurantepository.findByStateWithDistance(
					RestaurantStateEnum.getValue(RestaurantStateEnum.getValue(ApplicationConstants.confirmedState)),
					latitude, longitude, currentDay, pageable);
		} else {
			restaurants = restaurantepository.findByConfirmedState(
					RestaurantStateEnum.getValue(RestaurantStateEnum.getValue(ApplicationConstants.confirmedState)),
					currentDay, pageable);
		}
		log.info("Exitinf get all confirmed restaurant from: "+from +" limit : "+limit +" is success");
		return parseGetAllRestaurant(restaurants.getContent(), latitude, longitude);
	}

	@Override
	public List<RestaurantGetModel> getAllDraftedRestaurant(int from, int limit) {
		log.info("Entering get all drafted restaurant from: "+from +" limit : "+limit);
		Page<Restaurant> restaurants = null;
		restaurants = restaurantepository.findByState(
				RestaurantStateEnum.getValue(ApplicationConstants.draftState), PageRequest.of(from, limit));
		log.info("Exiting get all drafted restaurant from: "+from +" limit : "+limit);
			return parseGetAllRestaurant(restaurants.getContent(), null, null);		
	}

	@Override
	public void updateRestaurant(RestaurantModel restaurantModel) {
		log.info("Entering update restaurant for : "+restaurantModel.getId());
		int id = restaurantModel.getId();
		Optional<Restaurant> restaurant = restaurantepository.findById(id);
		if (!restaurant.isPresent()) {
			log.error("Update restaurant is requested for invalid restaurant id : "+id);
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		} else {
			parseUpdateRestaurantObject(restaurant.get(), restaurantModel);
		}
		try {
			restaurantepository.save(restaurant.get());
		} catch (Exception e) {
			throw new ApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR,
					ApplicationErrors.ADD_RESTAURANT_FAILED);
		}
		log.info("Exiting update restaurant is success for : "+restaurantModel.getId());
	}

	@Override
	public List<RestaurantGetModel> getRestaurantByRestaurantName(String restaurantName, int from, int limit,
			String latitude, String longitude) {
		log.info("Entering get restaurant by name : "+restaurantName);
		Page<Restaurant> restaurants = null;
		Pageable pageable = PageRequest.of(from, limit);
		String currentDay = LocalDate.now().getDayOfWeek().toString();
		if (latitude != null && longitude != null) {
			restaurants = restaurantepository.findByStateWithDistance(
					RestaurantStateEnum.getValue(ApplicationConstants.confirmedState), latitude, longitude, currentDay,
					PageRequest.of(from, limit));
		} else {
			restaurants = restaurantepository.findByName(
					RestaurantStateEnum.getValue(ApplicationConstants.confirmedState), restaurantName, 
					pageable);
		}
		List<RestaurantGetModel> restauGetModels = parseGetAllRestaurant(restaurants.getContent(), latitude, longitude);
		log.info("Exiting get restaurant by name is success for : "+restaurantName);
		return restauGetModels;
	}

	@Override
	public List<RestaurantGetModel> getRestaurantByFilter(String latitude, String longitude, int from, int limit,
			String km, List<String> restaurantService, List<String> restaurantSeating, List<String> restaurantCuisine,
			List<String> restaurantType, List<String> restaurantDiet) {
		log.info("Entering get restaurant by using customized filter ");
		Page<Restaurant> restaurants = null;
		String currentDay = LocalDate.now().getDayOfWeek().toString();
		if (restaurantCuisine == null || restaurantCuisine.isEmpty()) {
			List<Cuisines> cuisines = cuisinesRepository.findAll();
			restaurantCuisine = cuisines.stream().map(m -> m.getName()).collect(Collectors.toList());
		}
		if (restaurantService == null || restaurantService.isEmpty()) {
			List<Services> services = serviceRepository.findAll();
			restaurantService = services.stream().map(m -> m.getName()).collect(Collectors.toList());
		}
		if (restaurantSeating == null || restaurantSeating.isEmpty()) {
			List<Seatings> seatings = seatingsRepository.findAll();
			restaurantSeating = seatings.stream().map(m -> m.getName()).collect(Collectors.toList());
		}
		if (restaurantType == null || restaurantType.isEmpty()) {
			List<Types> types = typesRepository.findAll();
			restaurantType = types.stream().map(m -> m.getName()).collect(Collectors.toList());
		}
		if (restaurantDiet == null || restaurantDiet.isEmpty()) {
			List<Diets> diets = dietRepository.findAll();
			restaurantDiet = diets.stream().map(m -> m.getName()).collect(Collectors.toList());
		}
		if (latitude != null && longitude != null && km != null) {
			restaurants = restaurantepository.findBySearchWithKm(
					RestaurantStateEnum.getValue(ApplicationConstants.confirmedState), latitude, longitude,
					restaurantService, restaurantSeating, restaurantCuisine, restaurantType, restaurantDiet, km,
					currentDay, PageRequest.of(from, limit));
		} else if (latitude != null && longitude != null) {
			restaurants = restaurantepository.findBySearchWithLocation(
					RestaurantStateEnum.getValue(ApplicationConstants.confirmedState), latitude, longitude,
					restaurantService, restaurantSeating, restaurantCuisine, restaurantType, restaurantDiet, currentDay,
					PageRequest.of(from, limit));
		} else {
			restaurants = restaurantepository.findByConfirmedState(
					RestaurantStateEnum.getValue(ApplicationConstants.confirmedState), currentDay,
					PageRequest.of(from, limit));
		}
		List<RestaurantGetModel> restauGetModels = parseGetAllRestaurant(restaurants.getContent(), latitude, longitude);
		log.info("Exiting get restaurant by using customized filter is success : "+restauGetModels.size());
		return restauGetModels;
	}

	@Override
	public DefaultValuesResponse getDefaultTableValues() {
		log.info("Entering get default values for all restaurant ");
		DefaultValuesResponse defaultValuesResponse = new DefaultValuesResponse();
		List<Payments> payment = paymentsRepository.findAll();
		List<Seatings> seating = seatingsRepository.findAll();
		List<Services> service = serviceRepository.findAll();
		List<Types> type = typesRepository.findAll();
		List<Diets> diets = dietRepository.findAll();
		List<Cuisines> cuisines = cuisinesRepository.findAll();
		List<SearchType> searchType = searchTypeRepository.findAll();
		defaultValuesResponse.setPayments(payment.parallelStream()
				.map(data -> new BaseModel(data.getId(), data.getName())).collect(Collectors.toList()));
		defaultValuesResponse.setSeatings(seating.parallelStream()
				.map(data -> new BaseModel(data.getId(), data.getName())).collect(Collectors.toList()));
		defaultValuesResponse.setServices(service.parallelStream()
				.map(data -> new BaseModel(data.getId(), data.getName())).collect(Collectors.toList()));
		defaultValuesResponse.setTypes(type.parallelStream().map(data -> new BaseModel(data.getId(), data.getName()))
				.collect(Collectors.toList()));
		defaultValuesResponse.setDiets(diets.parallelStream().map(data -> new BaseModel(data.getId(), data.getName()))
				.collect(Collectors.toList()));
		defaultValuesResponse.setCuisines(cuisines.parallelStream()
				.map(data -> new BaseModel(data.getId(), data.getName())).collect(Collectors.toList()));
		defaultValuesResponse.setSearchTags(searchType.parallelStream()
				.map(data -> new BaseModel(data.getId(), data.getName())).collect(Collectors.toList()));
		log.info("Exiting get default values for all restaurant is success");
		return defaultValuesResponse;
	}

	private Restaurant parseRestaurantValue(RestaurantModel restaurantModel) {
		String state = RestaurantStateEnum.getValue(restaurantModel.getState());
		String status = RestaurantStatusEnum.getValue(restaurantModel.getStatus());
		AddressModel addressModel = restaurantModel.getAddress();
		Address address = Address.builder().line1(addressModel.getLine1()).line2(addressModel.getLine2())
				.district(addressModel.getDistrict()).city(addressModel.getCity()).state(addressModel.getState())
				.country(addressModel.getCountry()).pincode(addressModel.getPincode())
				.lattitude(addressModel.getLattitude()).longitude(addressModel.getLongitude()).build();
		List<Diets> dietList = dietRepository.findAllById(restaurantModel.getDietId());
		if (restaurantModel.getDietId().size() > 0 && (dietList == null || dietList.isEmpty())) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_DIET_ID);
		}
		List<SearchType> searchType = searchTypeRepository.findAllById(restaurantModel.getSearchTags());
		if (restaurantModel.getSearchTags().size() > 0 && (searchType == null || searchType.isEmpty())) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_SEARCH_TAG_ID);
		}
		List<Cuisines> cuisines = cuisinesRepository.findAllById(restaurantModel.getCuisines());
		if (restaurantModel.getCuisines().size() > 0 && (cuisines == null || cuisines.isEmpty())) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_CUISINE_ID);
		}
		List<Payments> payment = paymentsRepository.findAllById(restaurantModel.getPayment());
		if (restaurantModel.getPayment().size() > 0 && (payment == null || payment.isEmpty())) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_PAYMENT_ID);
		}
		List<Seatings> seating = seatingsRepository.findAllById(restaurantModel.getSeating());
		if (restaurantModel.getSeating().size() > 0 && (seating == null || seating.isEmpty())) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_SEATING_ID);
		}
		List<Services> service = serviceRepository.findAllById(restaurantModel.getServices());
		if (restaurantModel.getServices().size() > 0 && (service == null || service.isEmpty())) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_SERVICE_ID);
		}
		List<Types> type = typesRepository.findAllById(restaurantModel.getType());
		if (restaurantModel.getType().size() > 0 && (type == null || type.isEmpty())) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_TYPE_ID);
		}
		List<Timings> timings = parseRestaurantTiming(restaurantModel.getTimings());
		Optional<Account> account = accountRepository.findById(restaurantModel.getAccountId());
		if (!account.isPresent()) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ACCOUNT_ID);
		}
		Optional<Tiers> tiers = tiersRepository.findById((long) restaurantModel.getTierId());
		if (!tiers.isPresent()) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_TIER_ID);
		}
		Restaurant restaurant = Restaurant.builder().restaurantName(restaurantModel.getRestaurantName())
				.restaurantPhoneNumber(restaurantModel.getRestaurantPhoneNumber())
				.restaurantEmailId(restaurantModel.getRestaurantEmailId())
				.fssaiLicenseNo(restaurantModel.getFssaiLicenseNo()).website(restaurantModel.getWebsite())
				.submittedBy(restaurantModel.getSubmittedBy()).phoneNumber(restaurantModel.getPhoneNumber())
				.emailId(restaurantModel.getEmailId()).restaurantIsOpened(restaurantModel.getRestaurantIsOpened())
				.restaurantOpenDate(restaurantModel.getRestaurantOpenDate()).account(account.get()).status(status)
				.state(state).tier(tiers.get()).avgPricePerPerson(restaurantModel.getAvgPricePerPerson())
				.description(restaurantModel.getDescription()).alcoholServed(restaurantModel.getAlcoholServed())
				.address(address).services(service).seatings(seating).payments(payment).types(type).diets(dietList)
				.timings(timings).cuisines(cuisines).searchType(searchType).build();

		return restaurant;
	}

	private List<Timings> parseRestaurantTiming(List<TimingModel> timingModel) {
		List<Timings> timings = new ArrayList<Timings>();
		timingModel.forEach(data -> {
			timings.add(
					Timings.builder().day(data.getDay()).openingTime(data.getFrom()).ClosingTime(data.getTo()).build());
		});
		return timings;
	}

	public List<RestaurantGetModel> parseGetAllRestaurant(List<Restaurant> restaurants, String latitude,
			String longitude) {
		List<RestaurantGetModel> restaurantGetModels = new ArrayList<RestaurantGetModel>();
		if(!restaurants.isEmpty() && restaurants!=null) {
		restaurants.forEach(res -> {
			List<Timings> timings = res.getTimings();
			parseInternalValueOfRestaurant(latitude, longitude, restaurantGetModels, res, timings);

		});
		}
		return restaurantGetModels;
		
	}

	public void parseInternalValueOfRestaurant(String latitude, String longitude,
			List<RestaurantGetModel> restaurantGetModels, Restaurant res, List<Timings> timeForCurrentDay) {
		RestaurantGetModel restaurantGetModel = new RestaurantGetModel();
		restaurantGetModel.setTimingModel(timeForCurrentDay.parallelStream()
				.map(data -> new TimingModel(data.getDay(), data.getOpeningTime(), data.getClosingTime()))
				.collect(Collectors.toList()));
		restaurantGetModel.setId(res.getId());
		restaurantGetModel.setName(res.getRestaurantName());
		restaurantGetModel.setDescription(res.getDescription());
		restaurantGetModel.setStatus(res.getStatus());
		restaurantGetModel.setRating(res.getRating());
		Address address = res.getAddress();
		if (latitude == null && longitude == null) {
			restaurantGetModel.setAddress(AddressModel.builder().line1(address.getLine1()).line2(address.getLine2())
					.district(address.getDistrict()).city(address.getCity()).state(address.getState())
					.lattitude(address.getLattitude()).longitude(address.getLongitude()).country(address.getCountry())
					.pincode(address.getPincode()).build());
		} else {
			restaurantGetModel.setAddress(AddressModel.builder().line1(address.getLine1()).line2(address.getLine2())
					.district(address.getDistrict()).city(address.getCity()).state(address.getState())
					.lattitude(address.getLattitude()).longitude(address.getLongitude()).country(address.getCountry())
					.pincode(address.getPincode())
					.distance(calculateDistance(latitude, longitude, address.getLattitude(), address.getLongitude()))
					.build());
		}
		List<Cuisines> cuisines = res.getCuisines();
		List<String> cuisinesName = cuisines.stream().map(m -> m.getName()).collect(Collectors.toList());
		restaurantGetModel.setCuisines(cuisinesName);
		restaurantGetModel.setAvgPricePerPerson(res.getAvgPricePerPerson());
		List<Types> types = res.getTypes();
		List<String> typesName = types.stream().map(m -> m.getName()).collect(Collectors.toList());
		restaurantGetModel.setTypes(typesName);
		restaurantGetModels.add(restaurantGetModel);
	}

	private Restaurant parseUpdateRestaurantObject(Restaurant restaurant, RestaurantModel restaurantModel) {
		if (restaurantModel.getRestaurantName() != null)
			restaurant.setRestaurantName(restaurantModel.getRestaurantName());
		if (restaurantModel.getRestaurantPhoneNumber() != null)
			restaurant.setRestaurantPhoneNumber(restaurantModel.getRestaurantPhoneNumber());
		if (restaurantModel.getRestaurantEmailId() != null)
			restaurant.setRestaurantEmailId(restaurantModel.getRestaurantEmailId());
		if (restaurantModel.getFssaiLicenseNo() != null)
			restaurant.setFssaiLicenseNo(restaurantModel.getFssaiLicenseNo());
		if (restaurantModel.getWebsite() != null)
			restaurant.setWebsite(restaurantModel.getWebsite());
		if (restaurantModel.getSubmittedBy() != null)
			restaurant.setSubmittedBy(restaurantModel.getSubmittedBy());
		if (restaurantModel.getPhoneNumber() != null)
			restaurant.setPhoneNumber(restaurantModel.getPhoneNumber());
		if (restaurantModel.getEmailId() != null)
			restaurant.setEmailId(restaurantModel.getEmailId());
		if (restaurantModel.getRestaurantOpenDate() != null)
			restaurant.setRestaurantOpenDate(restaurantModel.getRestaurantOpenDate());
		if (restaurantModel.getAccountId() != 0) {
			Optional<Account> account = accountRepository.findById(restaurantModel.getAccountId());
			restaurant.setAccount(account.get());
		}
		if (restaurantModel.getStatus() != null)
			restaurant.setStatus(RestaurantStatusEnum.getValue(restaurantModel.getStatus()));
		if (restaurantModel.getState() != null)
			restaurant.setState(RestaurantStateEnum.getValue(restaurantModel.getState()));
		if (restaurantModel.getTierId() != 0) {
			Optional<Tiers> tiers = tiersRepository.findById((long) restaurantModel.getTierId());
			restaurant.setTier(tiers.get());
		}
		if (restaurantModel.getAvgPricePerPerson() != 0)
			restaurant.setAvgPricePerPerson(restaurantModel.getAvgPricePerPerson());
		if (restaurantModel.getDescription() != null)
			restaurant.setDescription(restaurantModel.getDescription());
		if (restaurantModel.getAddress() != null) {
			AddressModel addressModel = restaurantModel.getAddress();
			Address address = restaurant.getAddress();
			if (addressModel.getLine1() != null)
				address.setLine1(addressModel.getLine1());
			if (addressModel.getLine2() != null)
				address.setLine2(addressModel.getLine2());
			if (addressModel.getDistrict() != null)
				address.setDistrict(addressModel.getDistrict());
			if (addressModel.getCity() != null)
				address.setCity(addressModel.getCity());
			if (addressModel.getState() != null)
				address.setState(addressModel.getState());
			if (addressModel.getCountry() != null)
				address.setCountry(addressModel.getCountry());
			if (addressModel.getPincode() != null)
				address.setPincode(addressModel.getPincode());
			if (addressModel.getLattitude() != 0.0)
				address.setLattitude(addressModel.getLattitude());
			if (addressModel.getLongitude() != 0.0)
				address.setLongitude(addressModel.getLongitude());

			restaurant.setAddress(address);
		}
		if (restaurantModel.getType() != null) {
			List<Types> type = typesRepository.findAllById(restaurantModel.getType());
			restaurant.setTypes(type);
		}
		if (restaurantModel.getServices() != null) {
			List<Services> service = serviceRepository.findAllById(restaurantModel.getServices());
			restaurant.setServices(service);
		}
		if (restaurantModel.getSeating() != null) {
			List<Seatings> seating = seatingsRepository.findAllById(restaurantModel.getSeating());
			restaurant.setSeatings(seating);
		}
		if (restaurantModel.getPayment() != null) {
			List<Payments> payment = paymentsRepository.findAllById(restaurantModel.getPayment());
			restaurant.setPayments(payment);
		}
		if (restaurantModel.getCuisines() != null) {
			List<Cuisines> cuisines = cuisinesRepository.findAllById(restaurantModel.getCuisines());
			restaurant.setCuisines(cuisines);
		}
		if (restaurantModel.getSearchTags() != null) {
			List<SearchType> searchType = searchTypeRepository.findAllById(restaurantModel.getSearchTags());
			restaurant.setSearchType(searchType);
		}
		if (restaurantModel.getDietId() != null) {
			List<Diets> dietList = dietRepository.findAllById(restaurantModel.getDietId());
			restaurant.setDiets(dietList);
		}

		if (restaurantModel.getRestaurantIsOpened() != null) {
			restaurant.setRestaurantIsOpened(restaurantModel.getRestaurantIsOpened());
		}

		if (restaurantModel.getAlcoholServed() != null) {
			restaurant.setAlcoholServed(restaurantModel.getAlcoholServed());
		}

		if (restaurantModel.getTimings() != null) { 
			List<Timings> timings=restaurant.getTimings(); 
		    List<TimingModel> timingModels=restaurantModel.getTimings(); 
		    List<String> uniqueDay=timingModels.stream().map(e -> e.getDay()).collect(Collectors.toList());		    
		    timings.removeIf(p->(!uniqueDay.contains(p.getDay())));
		    for(TimingModel timModel:timingModels) {
		    	boolean check=true;		    	
		    	for(Timings tims:timings) {
		    		 if(timModel.getDay().equalsIgnoreCase(tims.getDay())) {
		    			tims.setOpeningTime(timModel.getFrom());
		    			tims.setClosingTime(timModel.getTo());
		    			check=false;
		    		}
		    	}
		    	if(check) {
		    		timings.add(Timings.builder().day(timModel.getDay()).openingTime(timModel.getFrom()).ClosingTime(timModel.getTo()).build());
		    	}
		    }
		    restaurant.setTimings(timings);
		}
		return restaurant;
	}

	private double calculateDistance(String latitude, String longitude, Double aLatitude, Double alongitude) {
		double dist = SloppyMath.haversinMeters(Double.valueOf(latitude), Double.valueOf(longitude), aLatitude,
				alongitude);
		return dist / 1000;
	}

	@Override
	public void updateStateAndStatus(int id, RestaurantUpdateRequest restaurantUpdateRequest) {
		Optional<Restaurant> restaurant = restaurantepository.findById(id);
		if (restaurant.isPresent()) {
			Restaurant res = restaurant.get();
			if (restaurantUpdateRequest.getState() != null && !restaurantUpdateRequest.getState().isEmpty()) {
				res.setState(RestaurantStateEnum.getValue(restaurantUpdateRequest.getState()));
                if (restaurantUpdateRequest.getState().equalsIgnoreCase(RestaurantStateEnum.CONFIRMED.toString())) {
                    userDetailsService.changeEmployeeUserStatus(res.getEmailId(), "active");
                }
			}
			if (restaurantUpdateRequest.getStatus() != null && !restaurantUpdateRequest.getStatus().isEmpty()) {
				res.setStatus(RestaurantStatusEnum.getValue(restaurantUpdateRequest.getStatus()));
			}
			Restaurant restaurantResponse = restaurantepository.save(res);
			if (restaurantUpdateRequest.getState() != null && !restaurantUpdateRequest.getState().isEmpty()) {
				try {
					emailService.triggerEmail(restaurantResponse, EmailModel.confirmRestaurant);
				} catch (Exception e) {
					log.error("unable to update the restaurant state " + e);
					throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
							ApplicationErrors.UPDATE_RESTAURANT_STATE_FAILED);
				}
			}
		} else {
			log.error("Update state is failed for restaurant : " + id);
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		}

	}

	@Override
	public List<TimingModel> getRestaurantTimings(int restaurantId) {
		Optional<Restaurant> restaurant = restaurantepository.findById(restaurantId);
		List<TimingModel> timingsModel = new ArrayList<TimingModel>();
		if (!restaurant.isPresent()) {
			log.error("Update restaurant is requested for invalid restaurant id : "+restaurantId);
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		} else {
			List<Timings> timings=timingRepository.findByRestaurantId(restaurantId);
			timingsModel=timings.parallelStream().map(data -> new TimingModel(data.getDay(), data.getOpeningTime(), data.getClosingTime())).collect(Collectors.toList());
		}
		return timingsModel;
	}

}
