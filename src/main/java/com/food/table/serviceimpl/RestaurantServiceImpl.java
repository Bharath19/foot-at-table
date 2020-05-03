package com.food.table.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.lucene.util.SloppyMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.food.table.dto.Account;
import com.food.table.dto.Address;
import com.food.table.dto.Cuisines;
import com.food.table.dto.Diets;
import com.food.table.dto.Payments;
import com.food.table.dto.Restaurant;
import com.food.table.dto.SearchType;
import com.food.table.dto.Seatings;
import com.food.table.dto.Services;
import com.food.table.dto.Tiers;
import com.food.table.dto.Timings;
import com.food.table.dto.Types;
import com.food.table.dto.constant.ApplicationConstants;
import com.food.table.model.AddressModel;
import com.food.table.model.RestaurantGetModel;
import com.food.table.model.RestaurantModel;
import com.food.table.model.TimingModel;
import com.food.table.repo.AccountRepository;
import com.food.table.repo.AddressRepository;
import com.food.table.repo.CuisinesRepository;
import com.food.table.repo.DietRepository;
import com.food.table.repo.PaymentsRepository;
import com.food.table.repo.RestaurantRepository;
import com.food.table.repo.SearchTypeRepository;
import com.food.table.repo.SeatingsRepository;
import com.food.table.repo.ServiceRepository;
import com.food.table.repo.TiersRepository;
import com.food.table.repo.TypesRepository;
import com.food.table.service.RestaurantService;

@Service
public class RestaurantServiceImpl implements RestaurantService {
	
	@Autowired
	RestaurantRepository restaurantepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	TiersRepository tiersRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	DietRepository dietRepository;
	
	@Autowired
	CuisinesRepository cuisinesRepository;	
	
	@Autowired
	SearchTypeRepository searchTypeRepository;
	
	@Autowired
	SeatingsRepository seatingsRepository;
	
	@Autowired
	ServiceRepository serviceRepository;
	
	@Autowired
	PaymentsRepository paymentsRepository;
	
	@Autowired
	TypesRepository typesRepository;

	@Override
	public void addRestaurant(RestaurantModel restaurantModel) {
		Restaurant restaurant =parseRestaurantValue(restaurantModel);
		restaurantepository.save(restaurant);
		}
	
	@Override
	public boolean deleteRestaurant(int id) {
		Optional<Restaurant> restaurant = restaurantepository.findById(id);
		restaurant.get().setState(ApplicationConstants.deleteState);
		Restaurant restaurantResponse = restaurantepository.save(restaurant.get());
		if (restaurantResponse.getState().equalsIgnoreCase(ApplicationConstants.deleteState))
			return true;
		else
			return false;
	}

	@Override
	public List<RestaurantGetModel> getAllRestaurant(int from,int limit,String latitude,String longitude) {
		Pageable pageable=PageRequest.of(from, limit);
		Page<Restaurant> restaurants=null;
		if(latitude!=null && longitude!=null) {
			restaurants = restaurantepository.findByStateWithDistance(ApplicationConstants.confirmedState, latitude, longitude, pageable);
		}
		else {
		 restaurants = restaurantepository.findByState(ApplicationConstants.confirmedState,pageable);
		}
		return parseGetAllRestaurant(restaurants.getContent(),latitude,longitude);
	}
	
	@Override
	public void updateRestaurant(RestaurantModel restaurantModel) {
		int id=restaurantModel.getId();
		Optional<Restaurant> restaurant=restaurantepository.findById(id);
		if(restaurant.get()!=null) {
			parseUpdateRestaurantObject(restaurant.get(), restaurantModel);
		}
		restaurantepository.save(restaurant.get());		
	}

	@Override
	public List<RestaurantGetModel> getRestaurantByDietType(List<String> dietType,int from,int limit,String latitude,String longitude) {
		Page<Restaurant> restaurants=null;
		if(latitude!=null && longitude!=null) {
			restaurants=restaurantepository.findByDietsWithDistance(ApplicationConstants.confirmedState, latitude, longitude, dietType, PageRequest.of(from, limit));	
		}
		else {
		restaurants=restaurantepository.findByDiets(ApplicationConstants.confirmedState, dietType,PageRequest.of(from, limit));
		}
		List<RestaurantGetModel> restauGetModels=parseGetAllRestaurant(restaurants.getContent(),latitude,longitude);
		return restauGetModels;
	}
	
	@Override
	public List<RestaurantGetModel> getRestaurantByRestaurantType(List<String> restaurantType,int from,int limit,String latitude,String longitude) {
		Page<Restaurant> restaurants=null;
		if(latitude!=null && longitude!=null) {
			restaurants=restaurantepository.findByRestauratTypesWithDistance(ApplicationConstants.confirmedState, latitude, longitude, restaurantType, PageRequest.of(from, limit));
		}
		else {
		restaurants=restaurantepository.findByRestauratTypes(ApplicationConstants.confirmedState, restaurantType,PageRequest.of(from, limit));
		}
		List<RestaurantGetModel> restauGetModels=parseGetAllRestaurant(restaurants.getContent(),latitude,longitude);
		return restauGetModels;
	}

	@Override
	public List<RestaurantGetModel> getRestaurantByRestaurantName(String restaurantName, int from, int limit,
			String latitude, String longitude) {
		Page<Restaurant> restaurants = null;
		if (latitude != null && longitude != null) {
			restaurants = restaurantepository.findByStateWithDistance(ApplicationConstants.confirmedState, latitude,
					longitude, PageRequest.of(from, limit));
		} else {
			restaurants = restaurantepository.findByName(ApplicationConstants.confirmedState, restaurantName,
					PageRequest.of(from, limit));
		}
		List<RestaurantGetModel> restauGetModels = parseGetAllRestaurant(restaurants.getContent(), latitude, longitude);
		return restauGetModels;
	}

	@Override
	public List<RestaurantGetModel> getRestaurantByRestaurantSeating(List<String> restaurantSeating, int from,
			int limit, String latitude, String longitude) {
		Page<Restaurant> restaurants = null;
		if (latitude != null && longitude != null) {
			restaurants = restaurantepository.findByRestauratSeatingsWithDistance(ApplicationConstants.confirmedState,
					latitude, longitude, restaurantSeating, PageRequest.of(from, limit));
		} else {
			restaurants = restaurantepository.findByRestauratSeatings(ApplicationConstants.confirmedState,
					restaurantSeating, PageRequest.of(from, limit));
		}
		List<RestaurantGetModel> restauGetModels = parseGetAllRestaurant(restaurants.getContent(), latitude, longitude);
		return restauGetModels;
	}

	@Override
	public List<RestaurantGetModel> getRestaurantByRestaurantService(List<String> restaurantService,int from,int limit,String latitude,String longitude) {
		Page<Restaurant> restaurants=null;
		if(latitude!=null && longitude!=null) {
			restaurants=restaurantepository.findByRestauratServicesWithDistance(ApplicationConstants.confirmedState, latitude, longitude, restaurantService, PageRequest.of(from, limit));
		}
		else {
		 restaurants=restaurantepository.findByRestauratServices(ApplicationConstants.confirmedState, restaurantService,PageRequest.of(from, limit));
		}
		List<RestaurantGetModel> restauGetModels=parseGetAllRestaurant(restaurants.getContent(),latitude,longitude);
		return restauGetModels;
	}

	@Override
	public List<RestaurantGetModel> getRestaurantByRestaurantCuisine(List<String> restaurantCuisine, int from,
			int limit, String latitude, String longitude) {
		Page<Restaurant> restaurants = null;
		if (latitude != null && longitude != null) {
			restaurants = restaurantepository.findByRestauratCuisinesWithDistance(ApplicationConstants.confirmedState,
					latitude, longitude, restaurantCuisine, PageRequest.of(from, limit));
		} else {
			restaurants = restaurantepository.findByRestauratCuisines(ApplicationConstants.confirmedState,
					restaurantCuisine, PageRequest.of(from, limit));
		}
		List<RestaurantGetModel> restauGetModels = parseGetAllRestaurant(restaurants.getContent(), latitude, longitude);
		return restauGetModels;
	}
	
	@Override
	public List<RestaurantGetModel> getRestaurantByDistance(String latitude, String longitude, String km) {
		List<Restaurant> restaurants=restaurantepository.findByDistance(ApplicationConstants.confirmedState,latitude, longitude, km);
		List<RestaurantGetModel> restauGetModels=parseGetAllRestaurant(restaurants,latitude,longitude);
		return restauGetModels;
	}
	
	private Restaurant parseRestaurantValue(RestaurantModel restaurantModel) {
		AddressModel addressModel = restaurantModel.getAddress();
		Address address = Address.builder().line1(addressModel.getLine1()).line2(addressModel.getLine2())
				.district(addressModel.getDistrict()).city(addressModel.getCity()).state(addressModel.getState())
				.country(addressModel.getCountry()).pincode(addressModel.getPincode())
				.lattitude(addressModel.getLattitude()).longitude(addressModel.getLongitude()).build();
		List<Diets> dietList=dietRepository.findAllById(restaurantModel.getDietId());
		List<SearchType> searchType=searchTypeRepository.findAllById(restaurantModel.getSearchTags());
		List<Cuisines> cuisines=cuisinesRepository.findAllById(restaurantModel.getCuisines());
		List<Payments> payment=paymentsRepository.findAllById(restaurantModel.getPayment());
		List<Seatings> seating=seatingsRepository.findAllById(restaurantModel.getSeating());
		List<Services> service=serviceRepository.findAllById(restaurantModel.getServices());
		List<Types> type=typesRepository.findAllById(restaurantModel.getType());
		List<Timings> timings=parseRestaurantTiming(restaurantModel.getTimings());
		Optional<Account> account = accountRepository.findById(restaurantModel.getAccountId());
		Optional<Tiers> tiers = tiersRepository.findById((long) restaurantModel.getTierId());
		Restaurant restaurant = Restaurant.builder().restaurantName(restaurantModel.getRestaurantName())
				.restaurantPhoneNumber(restaurantModel.getRestaurantPhoneNumber())
				.restaurantEmailId(restaurantModel.getRestaurantEmailId())
				.fssaiLicenseNo(restaurantModel.getFssaiLicenseNo()).website(restaurantModel.getWebsite())
				.submittedBy(restaurantModel.getSubmittedBy()).phoneNumber(restaurantModel.getPhoneNumber())
				.emailId(restaurantModel.getEmailId()).restaurantIsOpened(restaurantModel.getRestaurantIsOpened())
				.restaurantOpenDate(restaurantModel.getRestaurantOpenDate()).account(account.get())
				.status(restaurantModel.getStatus()).state(restaurantModel.getState()).tier(tiers.get())
				.avgPricePerPerson(restaurantModel.getAvgPricePerPerson()).imageUrl(restaurantModel.getImageUrl())
				.description(restaurantModel.getDescription()).alcoholServed(restaurantModel.getAlcoholServed())
				.address(address).services(service).seatings(seating).payments(payment).types(type)
				.diets(dietList).timings(timings).cuisines(cuisines).searchType(searchType).build();

		return restaurant;
	}	
	
	private List<Timings> parseRestaurantTiming(List<TimingModel> timingModel) {
		List<Timings> timings=new ArrayList<Timings>();
		timingModel.forEach(data->{			
			timings.add(Timings.builder().day(data.getDay()).openingTime(data.getFrom()).ClosingTime(data.getTo()).build());
		});
		return timings;
	}

	
	
	private List<RestaurantGetModel> parseGetAllRestaurant(List<Restaurant> restaurants,String latitude,String longitude) {
		List<RestaurantGetModel> restaurantGetModels = new ArrayList<RestaurantGetModel>();
		restaurants.forEach(res -> {
			String currentDay=LocalDate.now().getDayOfWeek().toString();
			List<Timings> timings=res.getTimings();
			List<Timings> timeForCurrentDay=timings.stream().filter(timefilter->timefilter.getDay().toUpperCase().matches(currentDay)).collect(Collectors.toList());
			if(!timeForCurrentDay.isEmpty() || timeForCurrentDay.size()!=0) {
			RestaurantGetModel restaurantGetModel = new RestaurantGetModel();
			restaurantGetModel.setTimingModel(TimingModel.builder().day(timeForCurrentDay.get(0).getDay()).from(timeForCurrentDay.get(0).getOpeningTime()).to(timeForCurrentDay.get(0).getClosingTime()).build());
			restaurantGetModel.setId(res.getId());
			restaurantGetModel.setName(res.getRestaurantName());
			restaurantGetModel.setDescription(res.getDescription());
			restaurantGetModel.setImageUrl(res.getImageUrl());
			Address address = res.getAddress();
			if(latitude==null&&longitude==null) {
			restaurantGetModel.setAddress(AddressModel.builder().line1(address.getLine1()).line2(address.getLine2())
					.district(address.getDistrict()).city(address.getCity()).state(address.getState())
					.country(address.getCountry()).pincode(address.getPincode()).build());
			}
			else {
				restaurantGetModel.setAddress(AddressModel.builder().line1(address.getLine1()).line2(address.getLine2())
						.district(address.getDistrict()).city(address.getCity()).state(address.getState())
						.country(address.getCountry()).pincode(address.getPincode()).distance(calculateDistance(latitude, longitude, address.getLattitude(), address.getLongitude())).build());
			}
			List<Cuisines> cuisines = res.getCuisines();
			List<String> cuisinesName = cuisines.stream().map(m -> m.getName()).collect(Collectors.toList());
			restaurantGetModel.setCuisines(cuisinesName);
			restaurantGetModel.setAvgPricePerPerson(res.getAvgPricePerPerson());			
			List<Types> types=res.getTypes();
			List<String> typesName = types.stream().map(m -> m.getName()).collect(Collectors.toList());
			restaurantGetModel.setTypes(typesName);
			restaurantGetModels.add(restaurantGetModel);
			}			
		});
		return restaurantGetModels;
	}
	
	private Restaurant parseUpdateRestaurantObject(Restaurant restaurant,RestaurantModel restaurantModel) {
		if(restaurantModel.getRestaurantName()!=null)
			restaurant.setRestaurantName(restaurantModel.getRestaurantName());
		if(restaurantModel.getRestaurantPhoneNumber()!=null)
			restaurant.setRestaurantPhoneNumber(restaurantModel.getRestaurantPhoneNumber());
		if(restaurantModel.getRestaurantEmailId()!=null)
			restaurant.setRestaurantEmailId(restaurantModel.getRestaurantEmailId());
		if(restaurantModel.getFssaiLicenseNo()!=null)
			restaurant.setFssaiLicenseNo(restaurantModel.getFssaiLicenseNo());
		if(restaurantModel.getWebsite()!=null)
			restaurant.setWebsite(restaurantModel.getWebsite());
		if(restaurantModel.getSubmittedBy()!=null)
			restaurant.setSubmittedBy(restaurantModel.getSubmittedBy());
		if(restaurantModel.getPhoneNumber()!=null)
			restaurant.setPhoneNumber(restaurantModel.getPhoneNumber());
		if(restaurantModel.getEmailId()!=null)
			restaurant.setEmailId(restaurantModel.getEmailId());		
		if(restaurantModel.getRestaurantOpenDate()!=null)
			restaurant.setRestaurantOpenDate(restaurantModel.getRestaurantOpenDate());
		if(restaurantModel.getAccountId()!=0) {
			Optional<Account> account = accountRepository.findById(restaurantModel.getAccountId());
			restaurant.setAccount(account.get());
		}
		if(restaurantModel.getStatus()!=null)
			restaurant.setStatus(restaurantModel.getStatus());
		if(restaurantModel.getState()!=null)
			restaurant.setState(restaurantModel.getState());
		if(restaurantModel.getTierId()!=0) {
			Optional<Tiers> tiers = tiersRepository.findById((long) restaurantModel.getTierId());
			restaurant.setTier(tiers.get());
		}
		if(restaurantModel.getAvgPricePerPerson()!=0)
			restaurant.setAvgPricePerPerson(restaurantModel.getAvgPricePerPerson());
		if(restaurantModel.getImageUrl()!=null)
			restaurant.setAvgPricePerPerson(restaurantModel.getAvgPricePerPerson());
		if(restaurantModel.getDescription()!=null)
			restaurant.setAvgPricePerPerson(restaurantModel.getAvgPricePerPerson());
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
			if (addressModel.getLattitude() !=0.0)
			    address.setLattitude(addressModel.getLattitude()); 
			if (addressModel.getLongitude() != 0.0)
				address.setLongitude(addressModel.getLongitude());			 

			restaurant.setAddress(address);
		}
		if(restaurantModel.getType()!=null) {
			List<Types> type=typesRepository.findAllById(restaurantModel.getType());
			restaurant.setTypes(type);
		}
		if(restaurantModel.getServices()!=null) {
			List<Services> service=serviceRepository.findAllById(restaurantModel.getServices());
			restaurant.setServices(service);
		}
		if(restaurantModel.getSeating()!=null) {
			List<Seatings> seating=seatingsRepository.findAllById(restaurantModel.getSeating());
			restaurant.setSeatings(seating);
		}
		if(restaurantModel.getPayment()!=null) {
			List<Payments> payment=paymentsRepository.findAllById(restaurantModel.getPayment());
			restaurant.setPayments(payment);
		}
		if(restaurantModel.getCuisines()!=null) {
			List<Cuisines> cuisines=cuisinesRepository.findAllById(restaurantModel.getCuisines());
			restaurant.setCuisines(cuisines);
		}
		if(restaurantModel.getSearchTags()!=null) {
			List<SearchType> searchType=searchTypeRepository.findAllById(restaurantModel.getSearchTags());
			restaurant.setSearchType(searchType);
		}
		if(restaurantModel.getDietId()!=null) {
			List<Diets> dietList=dietRepository.findAllById(restaurantModel.getDietId());
			restaurant.setDiets(dietList);
		}
		
		if(restaurantModel.getRestaurantIsOpened()!=null) {
			restaurant.setRestaurantIsOpened(restaurantModel.getRestaurantIsOpened());
		}
		
		if(restaurantModel.getAlcoholServed()!=null) {
			restaurant.setAlcoholServed(restaurantModel.getAlcoholServed());
		}
		
		if(restaurantModel.getTimings()!=null) { 
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
		double dist = SloppyMath.haversinMeters(Double.valueOf(latitude),
				Double.valueOf(longitude), aLatitude, alongitude);
		return dist/1000;
	}
		
}
