
package com.food.table.serviceimpl;

import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.food.table.constant.ApplicationConstants;
import com.food.table.constant.CartOrderStatus;
import com.food.table.constant.CartStateEnum;
import com.food.table.constant.OrderStateEnum;
import com.food.table.constant.PaymentStatusEnum;
import com.food.table.dto.Address;
import com.food.table.dto.Cart;
import com.food.table.dto.CartFoodOptions;
import com.food.table.dto.FoodOptions;
import com.food.table.dto.Foods;
import com.food.table.dto.Order;
import com.food.table.dto.Restaurant;
import com.food.table.dto.RestaurantTable;
import com.food.table.dto.Types;
import com.food.table.dto.UserAccount;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.AddressModel;
import com.food.table.model.BasicRevenueModel;
import com.food.table.model.BasicUserResponseModel;
import com.food.table.model.CartFoodOptionModel;
import com.food.table.model.CartFoodOptionResponseModel;
import com.food.table.model.CartModel;
import com.food.table.model.CartResponseModel;
import com.food.table.model.FoodResponseModel;
import com.food.table.model.OrderModel;
import com.food.table.model.OrderResponseModel;
import com.food.table.model.OrderStateModel;
import com.food.table.model.RestaurantBasicGetModel;
import com.food.table.model.RevenueDetailsModel;
import com.food.table.repo.CartRepository;
import com.food.table.repo.FoodOptionsRepository;
import com.food.table.repo.FoodRepository;
import com.food.table.repo.OrderRepository;
import com.food.table.repo.RestaurantRepository;
import com.food.table.repo.RestaurantTableRepository;
import com.food.table.repo.TypesRepository;
import com.food.table.repo.UserRepository;
import com.food.table.service.OrderService;
import com.food.table.service.PaymentService;
import com.food.table.util.SimpleDateUtil;
import com.food.table.util.UserUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	TypesRepository typeRepository;
	@Autowired
	RestaurantRepository restaurantRepository;
	@Autowired
	FoodRepository foodRepository;
	@Autowired
	FoodOptionsRepository foodOptionsRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	RestaurantTableRepository restaurantTableRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
    PaymentService paymentService;
//	TODO get gst details
//	@Autowired
//	SetupService setupService;
	
	private static final Map<OrderStateEnum, CartStateEnum> orderCartStateMap = Map.ofEntries(
			entry(OrderStateEnum.COMPLETED, CartStateEnum.COMPLETED),
			entry(OrderStateEnum.SERVED, CartStateEnum.SERVED),
			entry(OrderStateEnum.CANCELLED, CartStateEnum.CANCELLED),
			entry(OrderStateEnum.REQUESTED, CartStateEnum.REQUESTED));

	private static final Map<OrderStateEnum, CartOrderStatus> cartOrderStatusMap = Map.ofEntries(
			entry(OrderStateEnum.COMPLETED, CartOrderStatus.SUCCESS),
			entry(OrderStateEnum.CANCELLED, CartOrderStatus.FAILED));
	
	/**
	 * If order have any of the state, can add more food Additionally, need to check
	 * whether order type is dineIn
	 */
	@Getter
	private static final OrderStateEnum[] addMoreCartOrderState = { OrderStateEnum.INPROGRESS, OrderStateEnum.SERVED };

	@Override
	public Order createOrder(OrderModel orderModel) {
		return orderRepository.save(convertToDto(orderModel));
	}

//	@Override
//	public Order updateOrder(OrderModel orderModel, int orderId) {
//		try {
//			return orderRepository.save(convertToDto(orderModel, orderRepository.getOne(orderId)));
//		} catch (EntityNotFoundException e) {
//			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_ID);
//		}
//	}

	@Override
	public Order addMoreFoods(ArrayList<CartModel> cartModels, int orderId) {
		Optional<Order> order = orderRepository.findById(orderId);
		if (!order.isPresent())
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_ID);

		validateOrderAlreadyClosed(order.get());
		
		Order inProgressOrder = order.get();
		
		List<Cart> carts = inProgressOrder.getCarts();
		cartModels.forEach(cartModel -> {
			if (canCreateMoreCart(order.get())) {
				carts.add(convertToDto(cartModel, inProgressOrder));
			} else {
				log.error("unable add more cart. Order ID: " + inProgressOrder.getId()+" Order State: " + inProgressOrder.getState());
				throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_STATE_OR_TYPE);
			}
		});
		inProgressOrder.setState(OrderStateEnum.INPROGRESS);
		inProgressOrder.setTotalPrice(getTotalOrderPrice(inProgressOrder.getCarts()));
		orderRepository.save(inProgressOrder);
		return order.get();
	}

	@Override
	public Object updateOrderState(OrderStateModel orderStateModel, int orderId) {
		try {
			Order order =  orderRepository.save(updateState(orderStateModel, orderRepository.getOne(orderId)));
//			TODO get offer code and test payment gateway
//			if(order.getState().equals(OrderStateEnum.BILL_REQUESTED)) {
//				UserAccount userAccount = order.getUserAccount();
//				PaymentDetail paymentDetail = PaymentDetail.builder().name(userAccount.getName()).email(userAccount.getEmail())
//						.phone(userAccount.getPhoneNo().toString()).productInfo(order.getId()+"").amount(order.getTotalPrice()+"").build();
//				return paymentService.proceedPayment(paymentDetail);
//			}
			return order;
		} catch (EntityNotFoundException e) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_ID);
		}
	}
	
	@Override
	public void updateOrderStateAfterPayment(int orderId, PaymentStatusEnum paymrntState) {
		Optional<Order> order = orderRepository.findById(orderId);
		if (!order.isPresent())
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_ID);
		String orderType = order.get().getType().getName();
		if(orderType.equals(ApplicationConstants.dineInTypeText) || orderType.equals(ApplicationConstants.selfServiceTypeText)) {
			OrderStateModel orderStateModel = new OrderStateModel();
			orderStateModel.setState(OrderStateEnum.COMPLETED);
			updateOrderState(orderStateModel, orderId);
		}else {
			OrderStateModel orderStateModel = new OrderStateModel();
			orderStateModel.setState(OrderStateEnum.REQUESTED);
			updateOrderState(orderStateModel, orderId);	
		}
	}
	
	@Override
	public OrderResponseModel getOrderById(int orderId) {
		Optional<Order> order = orderRepository.findById(orderId);
		if (!order.isPresent())
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_ID);
		return OrderServiceImpl.convertToOrderResponse(order.get());
	}

	@Override
	public ArrayList<OrderResponseModel> getOrderByUserId(int userId, List<String> orderState, Date orderDate, int from,
			int limit) {
		try {
			if (orderState != null) 
				orderState.forEach(orderEnumState -> { OrderStateEnum.valueOf(orderEnumState); });
		} catch (IllegalArgumentException e) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_STATE);
		}

		if(userId == 0)
			userId = userRepository.findUserByPhoneNo(UserUtil.getUserDetails().getPhoneNo()).getId();
		Pageable pageable = PageRequest.of(from, limit);
		Page<Order> orderlist = null;
		if (orderState == null && orderDate == null) {
			orderlist = orderRepository.findByUserId(userId, pageable);
		} else if (orderState == null) {
			orderlist = orderRepository.findByUserIdAndCreatedAt(userId, SimpleDateUtil.toDate(orderDate), pageable);
		} else if (orderDate == null) {
			orderlist = orderRepository.findByUserIdAndStateIn(userId, orderState, pageable);
		} else {
			orderlist = orderRepository.findByUserIdAndStateInAndCreatedAt(userId, orderState,
					SimpleDateUtil.toDate(orderDate), pageable);
		}

		ArrayList<OrderResponseModel> orderResponseModelList = new ArrayList<OrderResponseModel>();
		orderlist.forEach(order -> orderResponseModelList.add(OrderServiceImpl.convertToOrderResponse(order)));
		return orderResponseModelList;
	}

	@Override
	public BasicRevenueModel getBasicRevenue(int restaurantId, Date orderDate) {
		List<RevenueDetailsModel> revenueDetails = orderRepository.findRevenueDetais(restaurantId,
				SimpleDateUtil.toDate(orderDate), OrderStateEnum.COMPLETED.toString());
		double totalPrice = 0;
		for (RevenueDetailsModel revenueDetailsModel : revenueDetails) {
			totalPrice += revenueDetailsModel.getTotalPrice();
		}
		return new BasicRevenueModel(revenueDetails, totalPrice);
	}

	@Override
	public List<OrderResponseModel> getOrderByOrderTypeName(int restaurantId, List<String> orderTypes,
			List<String> orderState, Date orderDate, int from, int limit) {
		Page<Order> orderlist = null;
		
		try {
			if (orderState != null)
				orderState.forEach(orderEnumState -> { OrderStateEnum.valueOf(orderEnumState); });
		} catch (IllegalArgumentException e) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_STATE);
		}

		Pageable pageable = PageRequest.of(from, limit);

		if (orderState == null && orderDate == null) {
			orderlist = orderRepository.findByRestaurantAndTypeIn(restaurantId, orderTypes, pageable);
		} else if (orderState == null) {
			orderlist = orderRepository.findByRestaurantAndTypeAndCreatedAt(restaurantId, orderTypes,
					SimpleDateUtil.toDate(orderDate), pageable);
		} else if (orderDate == null) {
			orderlist = orderRepository.findByRestaurantAndTypeAndStateIn(restaurantId, orderTypes, orderState,
					pageable);
		} else {
			orderlist = orderRepository.findByRestaurantAndTypeAndStateInAndCreatedAt(restaurantId, orderTypes,
					orderState, SimpleDateUtil.toDate(orderDate), pageable);
		}

		ArrayList<OrderResponseModel> orderResponseModelList = new ArrayList<OrderResponseModel>();
		orderlist.getContent()
				.forEach(order -> orderResponseModelList.add(OrderServiceImpl.convertToOrderResponse(order)));
		return orderResponseModelList;
	}

	@Override
	public List<OrderResponseModel> getOrderByRestaurantTableIdAndType(int restaurantId, int restaurantTableId,
			String orderType, List<String> orderState, Date orderDate, int from, int limit) {
		Page<Order> orderlist = null;
		
		try {
			if (orderState != null) 
				orderState.forEach(orderEnumState -> { OrderStateEnum.valueOf(orderEnumState); });
		} catch (IllegalArgumentException e) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_STATE);
		}

		Pageable pageable = PageRequest.of(from, limit);

		if (orderState == null && orderDate == null) {
			orderlist = orderRepository.findByRestaurantAndrestaurantTableAndType(restaurantId, restaurantTableId,
					orderType, pageable);
		} else if (orderState == null) {
			orderlist = orderRepository.findByRestaurantAndrestaurantTableAndCreatedAtAndType(restaurantId,
					restaurantTableId, SimpleDateUtil.toDate(orderDate), orderType, pageable);
		} else if (orderDate == null) {
			orderlist = orderRepository.findByRestaurantAndrestaurantTableAndStateInAndType(restaurantId,
					restaurantTableId, orderState, orderType, pageable);
		} else {
			orderlist = orderRepository.findByRestaurantAndrestaurantTableAndStateInAndCreatedAtAndType(restaurantId,
					restaurantTableId, orderState, SimpleDateUtil.toDate(orderDate), orderType, pageable);
		}

		ArrayList<OrderResponseModel> orderResponseModelList = new ArrayList<OrderResponseModel>();
		orderlist.getContent()
				.forEach(order -> orderResponseModelList.add(OrderServiceImpl.convertToOrderResponse(order)));
		return orderResponseModelList;
	}

	@Override
	public List<OrderResponseModel> getOrderByRestaurantId(int restaurantId, List<String> orderState, Date orderDate,
			int from, int limit) {
		Page<Order> orderlist = null;
		
		try {
			if (orderState != null) 
				orderState.forEach(orderEnumState -> { OrderStateEnum.valueOf(orderEnumState); });
		} catch (IllegalArgumentException e) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_STATE);
		}
		
		Pageable pageable = PageRequest.of(from, limit);

		if (orderState == null && orderDate == null) {
			orderlist = orderRepository.findByRestaurantId(restaurantId, pageable);
		} else if (orderState == null) {
			orderlist = orderRepository.findByRestaurantAndCreatedAt(restaurantId, SimpleDateUtil.toDate(orderDate),
					pageable);
		} else if (orderDate == null) {
			orderlist = orderRepository.findByRestaurantAndStateIn(restaurantId, orderState, pageable);
		} else {
			orderlist = orderRepository.findByRestaurantAndStateInAndCreatedAt(restaurantId, orderState,
					SimpleDateUtil.toDate(orderDate), pageable);
		}

		ArrayList<OrderResponseModel> orderResponseModelList = new ArrayList<OrderResponseModel>();
		orderlist.getContent()
				.forEach(order -> orderResponseModelList.add(OrderServiceImpl.convertToOrderResponse(order)));
		return orderResponseModelList;
	}

	/**
	 * update the order state in order entity object
	 * 
	 * @param orderModel user given POJO object
	 * @param order      existing order entity
	 * @return order entity object
	 */
	private Order updateState(OrderStateModel orderStateModel, Order order) {
		
		validateOrderAlreadyClosed(order);
		
		if(orderStateModel.getState() != null)
			order.setState(orderStateModel.getState());

		if (orderCartStateMap.containsKey(orderStateModel.getState())) {
			HashMap<Integer, Cart> cartMap = new HashMap<Integer, Cart>();
			order.getCarts().forEach(cart -> cartMap.put(cart.getId(), cart));

			order.getCarts().forEach(cart -> {
				cart.setState(orderCartStateMap.get(orderStateModel.getState()));
				if (cartOrderStatusMap.containsKey(orderStateModel.getState())) {
					cart.setOrderStatus(cartOrderStatusMap.get(orderStateModel.getState()));
				}
				cartMap.put(cart.getId(), cart);
			});

			order.setCarts(new ArrayList<Cart>(cartMap.values()));

		} else if (orderStateModel.getCarts() != null) {
			HashMap<Integer, Cart> cartMap = new HashMap<Integer, Cart>();
			order.getCarts().forEach(cart -> cartMap.put(cart.getId(), cart));

			orderStateModel.getCarts().forEach(cartModel -> {
				if (cartModel.getState() != null && cartMap.containsKey(cartModel.getId())) {
					Cart cart = cartMap.get(cartModel.getId());
					cart.setState(cartModel.getState());
					cartMap.put(cartModel.getId(), cart);
				}
			});

			order.setCarts(new ArrayList<Cart>(cartMap.values()));
		}
		return order;
	}
	

	private void validateOrderAlreadyClosed(Order order) {
		if(order.getState().equals(OrderStateEnum.COMPLETED)) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.ALREADY_COMPLETED_ORDER_STATE);
		}
		
		if(order.getState().equals(OrderStateEnum.CANCELLED)) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.ALREADY_CANCELLED_ORDER_STATE);
		}
	}

	/**
	 * This method convert OrderModel(POJO) to Order(Entity) on update action
	 * 
	 * @param orderModel user given POJO object
	 * @param order      existing order Entity object
	 * @return order entity object
	 */
//	private Order convertToDto(OrderModel orderModel, Order order) {
//		
//		Optional<RestaurantTable> restaurantTable = restaurantTableRepository.findById(orderModel.getRestaurantTableId());
//		if (!restaurantTable.isPresent())
//			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_TABLE_ID);
//		order.setRestaurantTable(restaurantTable.get());
//
//		HashMap<Integer, Cart> cartMap = new HashMap<Integer, Cart>();
//		order.getCarts().forEach(cart -> cartMap.put(cart.getId(), cart));
//
//		orderModel.getCarts().forEach(cartModel -> {
//			if (cartMap.containsKey(cartModel.getId())) {
//				Cart cart = cartMap.get(cartModel.getId());
//				if (cartModel.getQuantity() > 0) {
//					cart.setQuantity(cartModel.getQuantity());
//					cart.setPrice(cart.getFood().getPrice() * cart.getQuantity());
//				}
//				cart.setState(cartModel.getState());
//				cartMap.put(cartModel.getId(), cart);
//			} else {
//				log.error("can not add more cart " + order.getId());
//			}
//		});
//
//		order.setCarts(new ArrayList<Cart>(cartMap.values()));
//
//		order.setTotalPrice(getTotalOrderPrice(order.getCarts()));
//
//		return order;
//	}

	/**
	 * check whether we can add additional food on same order or not
	 * 
	 * @param order order existing order entity
	 * @return true if we can add more food, otherwise false
	 */
	private boolean canCreateMoreCart(Order order) {
		return (isDineInOrderType(order) && isValidOrderStateForMoreCart(order));
	}

	/**
	 * check the give order is dineIn type or not
	 * 
	 * @param order existing order entity
	 * @return true if give order is dineIn type, otherwise false
	 */
	private boolean isDineInOrderType(Order order) {
		return order.getType().getName().equalsIgnoreCase(ApplicationConstants.dineInTypeText);
	}

	/**
	 * check the order state to add more food on cart
	 * 
	 * @param order existing order entity
	 * @return true if the order state to can add more food in cart, otherwise false
	 */
	private boolean isValidOrderStateForMoreCart(Order order) {

		for (OrderStateEnum validOrderState : getAddMoreCartOrderState()) {
			if (order.getState().equals(validOrderState)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method update convert OrderModel(POJO) to Order(Entity) on create action
	 * 
	 * @param orderModel user given order POJO object
	 * @return Order entity object
	 */
	private Order convertToDto(OrderModel orderModel) {
		Order order = new Order();

		Types type = typeRepository.findByName(orderModel.getOrderType());
		if (type == null)
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_ORDER_TYPE_ID);
		order.setType(type);

		Optional<Restaurant> restaurant = restaurantRepository.findById(orderModel.getRestaurantId());
		if (!restaurant.isPresent())
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		order.setRestaurant(restaurant.get());
		
		if(orderModel.getUserId() == 0){
			order.setUserAccount(userRepository.findUserByPhoneNo(UserUtil.getUserDetails().getPhoneNo()));
		}else {
			Optional<UserAccount> user = userRepository.findById(orderModel.getUserId());
			if (!user.isPresent())
				throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_USER_ID);
			order.setUserAccount(user.get());
		}
		
		if(orderModel.isDineInType() || orderModel.isSelfServiceType()) {
			Optional<RestaurantTable> restaurantTable = restaurantTableRepository.findByIdAndRestaurantId(orderModel.getRestaurantTableId(), orderModel.getRestaurantId());
			if (!restaurantTable.isPresent())
				throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_TABLE_ID);
			order.setRestaurantTable(restaurantTable.get());
		}

		order.setState(orderModel.getState());

		ArrayList<Cart> cartList = new ArrayList<Cart>();
		orderModel.getCarts().forEach(cartModel -> {
			cartList.add(convertToDto(cartModel, orderModel));
		});
		order.setCarts(cartList);

		order.setTotalPrice(getTotalOrderPrice(cartList));
		return order;
	}
	
//	TODO improve the price calculation
//	private Order updatePriceDetails(Order order) {
//		double totalPrice = getTotalOrderPrice(order.getCarts());
//		order.setTotalPrice(totalPrice);
//		double cgstPrice = getCgstPrice(totalPrice);
//		order.setCgst(cgstPrice);
//		double sgstPrice = getSgstPrice(totalPrice);
//		order.setSgst(sgstPrice);
//		order.setRequestedPrice(totalPrice+cgstPrice+sgstPrice);
//		return order;
//	}

	/**
	 * This method update convert CartModel(POJO) to Cart(Entity) on creation action
	 * 
	 * @param orderModel user given order POJO object
	 * @param cartModel  user given cart POJO object
	 * @return cart entity object
	 */
	private Cart convertToDto(CartModel cartModel, OrderModel orderModel) {
		Cart cart = new Cart();
		Foods food = getFoodWithValidation(cartModel.getFoodId(), orderModel.getRestaurantId());
		cart.setFood(food);
		cart.setQuantity(cartModel.getQuantity());
	
		Restaurant restaurant = restaurantRepository.getOne(orderModel.getRestaurantId());
		if (restaurant == null) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		}
		cart.setRestaurant(restaurant);
		cart.setPrice(food.getPrice() * cartModel.getQuantity());
		
		if(orderModel.getState().equals(OrderStateEnum.DRAFTED)) {
			cart.setState(CartStateEnum.DRAFTED);
		}else if(orderModel.getState().equals(OrderStateEnum.REQUESTED)) {
			cart.setState(CartStateEnum.REQUESTED);
		}
		
		if(cartModel.getCartFoodOptionModel() != null && cartModel.getCartFoodOptionModel().size() != 0)
		{
			List<CartFoodOptionModel> cartFoodOptionModels = cartModel.getCartFoodOptionModel();
			List<CartFoodOptions> cartFoodOptions = new ArrayList<CartFoodOptions>();
			cartFoodOptionModels.forEach(cartFoodOptionModel -> {
				cartFoodOptions.add(convertToFoodOptionDto(cartFoodOptionModel, cartModel));
			});
			cart.setCartFoodOptions(cartFoodOptions);
		}
		return cart;
	}

	private CartFoodOptions convertToFoodOptionDto(CartFoodOptionModel cartFoodOptionModel, CartModel cartModel) {
		CartFoodOptions  cartFoodOption = new CartFoodOptions();
		FoodOptions foodOption = getFoodOptionWithValidation(cartFoodOptionModel.getFoodOptionId(), cartModel.getFoodId());
		cartFoodOption.setFoodOption(foodOption);
		cartFoodOption.setQuantity(cartFoodOptionModel.getQuantity());
		cartFoodOption.setPrice(foodOption.getPrice() * cartFoodOptionModel.getQuantity());
		return cartFoodOption;
	}

	/**
	 * This method update convert CartModel(POJO) to Cart(Entity) on creation action for add more food
	 * 
	 * @param order     user given order POJO object
	 * @param cartModel user given cart POJO object
	 * @return cart entity object
	 */
	private Cart convertToDto(CartModel cartModel, Order order) {
		Cart cart = new Cart();
		Optional<Foods> food = foodRepository.findById(cartModel.getFoodId());
		if (!food.isPresent())
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_FOOD_ID);
		cart.setFood(food.get());
		cart.setQuantity(cartModel.getQuantity());
		Restaurant restaurant = restaurantRepository.getOne(order.getRestaurant().getId());
		cart.setOrderId(order.getId());
		if (restaurant == null) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_RESTAURANT_ID);
		}
		cart.setRestaurant(restaurant);
		cart.setPrice(food.get().getPrice() * cartModel.getQuantity());
//		default sate when add more food
		cart.setState(CartStateEnum.REQUESTED);
		if(cartModel.getCartFoodOptionModel() != null && cartModel.getCartFoodOptionModel().size() != 0)
		{
			List<CartFoodOptionModel> cartFoodOptionModels = cartModel.getCartFoodOptionModel();
			List<CartFoodOptions> cartFoodOptions = new ArrayList<CartFoodOptions>();
			cartFoodOptionModels.forEach(cartFoodOptionModel -> {
				cartFoodOptions.add(convertToFoodOptionDto(cartFoodOptionModel, cartModel));
			});
			cart.setCartFoodOptions(cartFoodOptions);
		}
		return cart;
	}

	/**
	 * Calculate the total price of the order
	 * 
	 * @param carts cart entity object
	 * @return total price as double
	 */
	private double getTotalOrderPrice(List<Cart> carts) {
		double totalPrice = 0;
		for (Cart cart : carts) {
			if (!(cart.getState().equals(CartStateEnum.CANCELLED))) {
				totalPrice += cart.getPrice();
				if(cart.getCartFoodOptions() != null)
					for(CartFoodOptions cartFoodOption: cart.getCartFoodOptions()) {
						totalPrice += cartFoodOption.getPrice();
					}
			}
		}
		return totalPrice;
	}
	
//	TODO get gst details
//	private double getSgstPrice(double totalPrice) {
//		double sgstPercentage = setupService.getGstValues().get(ApplicationConstants.sgstKey).intValue();
//		return (totalPrice * sgstPercentage) / 100;
//	}
//
//	private double getCgstPrice(double totalPrice) {
//		double cgstPercentage = setupService.getGstValues().get(ApplicationConstants.cgstKey).intValue();
//		return (totalPrice * cgstPercentage) / 100;
//	}
	
	private FoodOptions getFoodOptionWithValidation(int id, int foodId) {
		if(foodId < 1) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_FOOD_OPTION_ID);
		}
		FoodOptions foodOption = foodOptionsRepository.findIdByIdAndFoodId(id, foodId);
		if(Objects.isNull(foodOption)){
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_FOOD_OPTION_ID);
		}else if(foodOption.getStatus() != 1) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_FOOD_OPTION_STATUS);
		}
		return foodOption;
	}
	
	private Foods getFoodWithValidation(int foodId, int restaurantId) {
		if(foodId < 1) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_FOOD_ID);
		}
		Foods food = foodRepository.findByIdAndRestaurantId(foodId, restaurantId);
		if(Objects.isNull(food)) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_FOOD_ID);
		}
		if(food.getStatus() != 1) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_FOOD_STATE);
		}
//		TODO - finalize food start and end time format, add food availability at the time condition
		return food;
	}

	private static OrderResponseModel convertToOrderResponse(Order order) {
		if (order == null)
			return null;

		OrderResponseModel orderResponseModel = new OrderResponseModel();
		orderResponseModel.setId(order.getId());

		Restaurant restaurant = order.getRestaurant();
		RestaurantBasicGetModel restaurantBasicGetModel = RestaurantBasicGetModel.builder().id(restaurant.getId())
				.name(restaurant.getRestaurantName()).imageUrl(restaurant.getImageUrl()).build();

		Address address = restaurant.getAddress();
		restaurantBasicGetModel.setAddress(AddressModel.builder().line1(address.getLine1()).line2(address.getLine2())
				.district(address.getDistrict()).city(address.getCity()).state(address.getState())
				.country(address.getCountry()).pincode(address.getPincode()).longitude(address.getLattitude())
				.lattitude(address.getLattitude()).build());

		orderResponseModel.setRestaurant(restaurantBasicGetModel);
		UserAccount userAccount = order.getUserAccount();
		orderResponseModel.setUserAccount(
				BasicUserResponseModel.builder().id(userAccount.getId()).name(userAccount.getName()).build());
		if(Objects.nonNull(order.getRestaurantTable())) {
			orderResponseModel.setRestaurantTableId(order.getRestaurantTable().getId());
		}
		orderResponseModel.setOrderTypeName(order.getType().getName());
		orderResponseModel.setTotalPrice(order.getTotalPrice());
		orderResponseModel.setPaidPrice(order.getPaidPrice());
		orderResponseModel.setState(order.getState());
		orderResponseModel.setOrderDate(order.getCreatedAt());

		List<CartResponseModel> cartResponseList = new ArrayList<CartResponseModel>();
		order.getCarts().forEach(cart -> {
			CartResponseModel cartResponseModel = new CartResponseModel();
			Foods food = cart.getFood();
			FoodResponseModel foodResponseModel = FoodResponseModel.builder().id(food.getId()).name(food.getName())
					.description(food.getDescription()).imageUrl(food.getImageUrl()).price(food.getPrice()).build();
			cartResponseModel.setFood(foodResponseModel);
			cartResponseModel.setId(cart.getId());
			cartResponseModel.setPrice(cart.getPrice());
			cartResponseModel.setQuantity(cart.getQuantity());
			cartResponseModel.setState(cart.getState());
			
			List<CartFoodOptions> cartFoodOptions = cart.getCartFoodOptions();
			
			if(cartFoodOptions != null && cartFoodOptions.size() != 0) {
				
				List<CartFoodOptionResponseModel> cartFoodOptionResponseModels = new ArrayList<CartFoodOptionResponseModel>();
				cartFoodOptions.forEach(cartFoodOption -> {
					FoodOptions foodOption = cartFoodOption.getFoodOption();
					cartFoodOptionResponseModels.add(CartFoodOptionResponseModel.builder().name(foodOption.getName())
							.imageUrl(foodOption.getImageUrl()).description(foodOption.getDescription())
							.price(cartFoodOption.getPrice()).quantity(cartFoodOption.getQuantity()).build());
				});
				cartResponseModel.setCartFoodOptionResponseModels(cartFoodOptionResponseModels);
			}
			
			cartResponseList.add(cartResponseModel);
		});

		orderResponseModel.setCarts(cartResponseList);
		return orderResponseModel;
	}

}
