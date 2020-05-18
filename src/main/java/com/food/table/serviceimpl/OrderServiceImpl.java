
package com.food.table.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;

import static java.util.Map.entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.table.dto.Address;
import com.food.table.constant.ApplicationConstants;
import com.food.table.constant.CartOrderStatus;
import com.food.table.constant.CartStateEnum;
import com.food.table.constant.OrderStateEnum;
import com.food.table.dto.Cart;
import com.food.table.dto.Foods;
import com.food.table.dto.Order;
import com.food.table.dto.Restaurant;
import com.food.table.dto.RestaurantTable;
import com.food.table.dto.Types;
import com.food.table.exceptions.RecordNotFoundException;
import com.food.table.model.AddressModel;
import com.food.table.model.CartModel;
import com.food.table.model.CartResponseModel;
import com.food.table.model.FoodResponseModel;
import com.food.table.model.OrderModel;
import com.food.table.model.OrderResponseModel;
import com.food.table.model.OrderStateModel;
import com.food.table.model.RestaurantBasicGetModel;
import com.food.table.repo.CartRepository;
import com.food.table.repo.FoodRepository;
import com.food.table.repo.OrderRepository;
import com.food.table.repo.RestaurantRepository;
import com.food.table.repo.RestaurantTableRepository;
import com.food.table.repo.TypesRepository;
import com.food.table.service.OrderService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

	private static final String ORDER_RECORD_NOT_FOUND_MESSAGE = "No Record Found in %s for id : %d";

	private static final Map<OrderStateEnum, CartStateEnum> orderCartStateMap = Map.ofEntries(
			entry(OrderStateEnum.COMPLETED, CartStateEnum.COMPLETED),
			entry(OrderStateEnum.SERVED, CartStateEnum.SERVED),
			entry(OrderStateEnum.CANCELLED, CartStateEnum.CANCELLED));

	private static final Map<OrderStateEnum, CartOrderStatus> cartOrderStatusMap = Map.ofEntries(
			entry(OrderStateEnum.COMPLETED, CartOrderStatus.SUCCESS),
			entry(OrderStateEnum.CANCELLED, CartOrderStatus.FAILED));

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	TypesRepository typeRepository;
	@Autowired
	RestaurantRepository restaurantRepository;
	@Autowired
	FoodRepository foodRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	RestaurantTableRepository restaurantTableRepository;

	/**
	 * If order have any of the state, can add more food Additionally, need to check
	 * whether order type is dineIn
	 */
	@Getter
	private static final OrderStateEnum[] addMoreCartOrderState = { OrderStateEnum.INPROGRESS };

	@Override
	public OrderResponseModel getOrderById(int orderId) {
		Optional<Order> order = orderRepository.findById(orderId);
		if (!order.isPresent())
			triggerExceptionIfRecordNotExist(null, "Order Table", orderId);
		return OrderServiceImpl.convertToOrderResponse(order.get());
	}

	@Override
	public ArrayList<OrderResponseModel> getOrderByUserId(int userId) {
		ArrayList<Order> orders = orderRepository.findByUserId(userId);
		triggerExceptionIfRecordNotExist(orders, "Order Table", 0);
		ArrayList<OrderResponseModel> orderResponseModelList = new ArrayList<OrderResponseModel>();
		orders.forEach(order -> orderResponseModelList.add(OrderServiceImpl.convertToOrderResponse(order)));
		return orderResponseModelList;
	}

	@Override
	public Order createOrder(OrderModel orderModel) {
		return orderRepository.save(convertToDto(orderModel));
	}

	@Override
	public Order updateOrder(OrderModel orderModel, int orderId) {
		try {
			return orderRepository.save(convertToDto(orderModel, orderRepository.getOne(orderId)));
		} catch (EntityNotFoundException e) {
			triggerExceptionIfRecordNotExist(null, "Order Table", orderId);
		}
		return null;
	}

	@Override
	public Order updateOrderState(OrderStateModel orderStateModel, int orderId) {
		try {
			return orderRepository.save(updateState(orderStateModel, orderRepository.getOne(orderId)));
		} catch (EntityNotFoundException e) {
			triggerExceptionIfRecordNotExist(null, "Order Table", orderId);
		}
		return null;
	}

	/**
	 * update the order state in order entity object
	 * 
	 * @param orderModel user given POJO object
	 * @param order      existing order entity
	 * @return order entity object
	 */
	private Order updateState(OrderStateModel orderStateModel, Order order) {

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

	/**
	 * This method convert OrderModel(POJO) to Order(Entity) on update action
	 * 
	 * @param orderModel user given POJO object
	 * @param order      existing order Entity object
	 * @return order entity object
	 */
	private Order convertToDto(OrderModel orderModel, Order order) {

		Optional<RestaurantTable> restaurantTable = restaurantTableRepository
				.findById(orderModel.getRestaurantTableId());
		if (!restaurantTable.isPresent())
			triggerExceptionIfRecordNotExist(null, "RestaurantTableDetails Table", orderModel.getRestaurantTableId());
		order.setRestaurantTable(restaurantTable.get());

		HashMap<Integer, Cart> cartMap = new HashMap<Integer, Cart>();
		order.getCarts().forEach(cart -> cartMap.put(cart.getId(), cart));

		orderModel.getCarts().forEach(cartModel -> {
			if (cartMap.containsKey(cartModel.getId())) {
				Cart cart = cartMap.get(cartModel.getId());
				if (cartModel.getQuantity() > 0) {
					cart.setQuantity(cartModel.getQuantity());
					cart.setPrice(cart.getFood().getPrice() * cart.getQuantity());
				}
				cart.setState(cartModel.getState());
				cartMap.put(cartModel.getId(), cart);
			} else {
				if (canCreateMoreCart(order)) {
					Cart cart = convertToDto(cartModel, orderModel);
					cartMap.put(cart.hashCode(), cart);
				} else {
					log.error("unable add more cart " + order.getId());
				}
			}
		});

		order.setCarts(new ArrayList<Cart>(cartMap.values()));

		order.setTotalPrice(getTotalOrderPrice((ArrayList<Cart>) order.getCarts()));

		return order;
	}

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

		Optional<Types> type = typeRepository.findById(orderModel.getOrderType());
		if (!type.isPresent())
			triggerExceptionIfRecordNotExist(null, "OrderType Table", orderModel.getOrderType());
		order.setType(type.get());

		Optional<Restaurant> restaurant = restaurantRepository.findById(orderModel.getRestaurantId());
		if (!restaurant.isPresent())
			triggerExceptionIfRecordNotExist(null, "Restaurant Table", orderModel.getRestaurantId());
		order.setRestaurant(restaurant.get());

		order.setUserId(orderModel.getUserId());

		Optional<RestaurantTable> restaurantTable = restaurantTableRepository
				.findByIdAndRestaurantId(orderModel.getRestaurantTableId(), orderModel.getRestaurantId());
		if (!restaurantTable.isPresent())
			triggerExceptionIfRecordNotExist(null, "RestaurantTableDetails Table", orderModel.getRestaurantTableId());
		order.setRestaurantTable(restaurantTable.get());

		order.setPaidPrice(orderModel.getPaidPrice());
		order.setState(orderModel.getState());

		ArrayList<Cart> cartList = new ArrayList<Cart>();
		orderModel.getCarts().forEach(cart -> {
			cartList.add(convertToDto(cart, orderModel));
		});
		order.setCarts(cartList);

		order.setTotalPrice(getTotalOrderPrice(cartList));
		return order;
	}

	/**
	 * This method update convert CartModel(POJO) to Cart(Entity) on creation action
	 * 
	 * @param orderModel user given order POJO object
	 * @param cartModel  user given cart POJO object
	 * @return cart entity object
	 */
	private Cart convertToDto(CartModel cartModel, OrderModel orderModel) {
		Cart cart = new Cart();
		Optional<Foods> food = foodRepository.findById(cartModel.getFoodId());
		if (!food.isPresent())
			triggerExceptionIfRecordNotExist(null, "Food Table", cartModel.getFoodId());
		cart.setFood(food.get());
		cart.setQuantity(cartModel.getQuantity());
		cart.setRestaurant(restaurantRepository.getOne(orderModel.getRestaurantId()));
		cart.setPrice(food.get().getPrice() * cartModel.getQuantity());
		cart.setState(cartModel.getState());
		return cart;
	}

	/**
	 * Calculate the total price of the order
	 * 
	 * @param carts cart entity object
	 * @return total price as double
	 */
	private double getTotalOrderPrice(ArrayList<Cart> carts) {
		double totalPrice = 0;
		for (Cart cart : carts) {
			if (!(cart.getState().equals(CartStateEnum.CANCELLED))) {
				totalPrice += cart.getPrice();
			}
		}
		return totalPrice;
	}

	private void triggerExceptionIfRecordNotExist(Object object, String keyWord, int id) {
		if (Objects.isNull(object)) {
			throw new RecordNotFoundException(String.format(ORDER_RECORD_NOT_FOUND_MESSAGE, keyWord, id));
		}
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

		orderResponseModel.setUserId(order.getUserId());
		orderResponseModel.setRestaurantTableId(order.getRestaurantTable().getId());
		orderResponseModel.setOrderType(order.getType().getId());
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
			cartResponseList.add(cartResponseModel);
		});

		orderResponseModel.setCarts(cartResponseList);
		return orderResponseModel;
	}

}
