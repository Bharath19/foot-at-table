package com.food.table.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.table.constant.ApplicationConstants;
import com.food.table.constant.CartStateEnum;
import com.food.table.constant.OrderStateEnum;
import com.food.table.dto.Cart;
import com.food.table.dto.Foods;
import com.food.table.dto.Order;
import com.food.table.dto.RestaurantTable;
import com.food.table.exceptions.RecordNotFoundException;
import com.food.table.model.CartModel;
import com.food.table.model.OrderModel;
import com.food.table.model.OrderResponseModel;
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
	 * If order have any of the state, can add more food
	 * Additionally, need to check whether order type is dineIn
	 */
	@Getter
	private static final int[] addMoreCartOrderState = { OrderStateEnum.INPROGRESS.getId(),
			OrderStateEnum.BILL_REQUESTED.getId() };


	@Override
	public OrderResponseModel getOrderById(int orderId) {
		Optional<Order> order = orderRepository.findById(orderId);
		if(!order.isPresent())
			triggerExceptionIfRecordNotExist(null, "Order Table", orderId);
		return OrderResponseModel.convertToOrderResponse(order.get());
	}
	
	@Override
	public ArrayList<OrderResponseModel> getOrderByUserId(int userId) {
		ArrayList<Order> orders = orderRepository.findByUserId(userId);
//		check list
		triggerExceptionIfRecordNotExist(orders, "Order Table", 0);
		ArrayList<OrderResponseModel> orderResponseModelList = new ArrayList<OrderResponseModel>();
		orders.forEach(order -> orderResponseModelList.add(OrderResponseModel.convertToOrderResponse(order)));
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
		}catch (EntityNotFoundException e) {
			triggerExceptionIfRecordNotExist(null, "Order Table", orderId);
		}
		return null;
	}

	@Override
	public Order updateOrderState(OrderModel orderModel, int orderId) {
		try {
			return orderRepository.save(updateState(orderModel, orderRepository.getOne(orderId)));
		}catch (EntityNotFoundException e) {
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
	private Order updateState(OrderModel orderModel, Order order) {
//		TODO check the existence of order state in Enum 
		if (orderModel.getState() != null)
			order.setState(OrderStateEnum.getValue(orderModel.getState()));

		if (orderModel.getCarts() != null) {
			HashMap<Integer, Cart> cartMap = new HashMap<Integer, Cart>();
			order.getCarts().forEach(cart -> cartMap.put(cart.getId(), cart));

			orderModel.getCarts().forEach(cartModel -> {
				if (cartModel.getState() != null && cartMap.containsKey(cartModel.getId())) {
					Cart cart = cartMap.get(cartModel.getId());
					cart.setState(CartStateEnum.getValue(cartModel.getState()));
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
		if (order.isRequestedState()) {
			if (orderModel.getOrderType() != 0) {
				try {
					order.setType(typeRepository.getOne(orderModel.getOrderType()));
				}catch (EntityNotFoundException e) {
					triggerExceptionIfRecordNotExist(null, "Types Table", orderModel.getOrderType());
				}	
			}

			if (orderModel.getState() != null) {
				order.setState(OrderStateEnum.getValue(orderModel.getState()));
			}
		}

		if (orderModel.getPaidPrice() != 0) {
			order.setPaidPrice(orderModel.getPaidPrice());
			if (orderModel.getState() != null) {
				order.setState(OrderStateEnum.getValue(orderModel.getState()));
			}
		}

		if (orderModel.getRestaurantTableId() != 0) {
			Optional<RestaurantTable> restaurantTable = restaurantTableRepository.findById(orderModel.getRestaurantTableId());
			if(!restaurantTable.isPresent())
				triggerExceptionIfRecordNotExist(null, "RestaurantTableDetails Table", orderModel.getRestaurantTableId());
			order.setRestaurantTable(restaurantTable.get());
		}

		HashMap<Integer, Cart> cartMap = new HashMap<Integer, Cart>();
		order.getCarts().forEach(cart -> cartMap.put(cart.getId(), cart));

		orderModel.getCarts().forEach(cartModel -> {
			if (cartMap.containsKey(cartModel.getId())) {
				Cart cart = cartMap.get(cartModel.getId());
				if (order.isRequestedState()) {
					if (cartModel.getQuantity() != 0) {
						cart.setQuantity(cartModel.getQuantity());
						cart.setPrice(cart.getFood().getPrice() * cart.getQuantity());
					}
					cart.setState(CartStateEnum.getValue(cartModel.getState()));
					cartMap.put(cartModel.getId(), cart);
				}
			} else {
				if (canCreateMoreCart(order)) {
					Cart cart = convertToDto(cartModel, orderModel);
					cartMap.put(cart.hashCode(), cart);
				} else {
					log.error("unable add more cart "+order.getId());
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
		return IntStream.of(getAddMoreCartOrderState()).anyMatch(x -> x == order.getState());
	}

	/**
	 * This method update convert OrderModel(POJO) to Order(Entity) on create action
	 * 
	 * @param orderModel user given order POJO object
	 * @return Order entity object
	 */
	private Order convertToDto(OrderModel orderModel) {
		Order order = new Order();
		try {
			order.setType(typeRepository.getOne(orderModel.getOrderType()));
		}catch (EntityNotFoundException e) {
			triggerExceptionIfRecordNotExist(null, "Types Table", orderModel.getOrderType());
		}
		
		try {
			order.setRestaurant(restaurantRepository.getOne(orderModel.getRestaurantId()));
		}catch (EntityNotFoundException e) {
			triggerExceptionIfRecordNotExist(null, "Restaurant Table", orderModel.getRestaurantId());
		}
		
		order.setUserId(orderModel.getUserId());
		
		Optional<RestaurantTable> restaurantTable = restaurantTableRepository.findById(orderModel.getRestaurantTableId());
		if(!restaurantTable.isPresent())
			triggerExceptionIfRecordNotExist(null, "RestaurantTableDetails Table", orderModel.getRestaurantTableId());
		order.setRestaurantTable(restaurantTable.get());
		
		order.setPaidPrice(orderModel.getPaidPrice());
		order.setState(OrderStateEnum.getValue(orderModel.getState()));
		
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
		if(!food.isPresent())
			triggerExceptionIfRecordNotExist(null, "Food Table", cartModel.getFoodId());
		cart.setFood(food.get());
		cart.setQuantity(cartModel.getQuantity());
		cart.setRestaurant(restaurantRepository.getOne(orderModel.getRestaurantId()));
		cart.setPrice(food.get().getPrice() * cartModel.getQuantity());
		cart.setState(CartStateEnum.getValue(cartModel.getState()));
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
			if (cart.canCalculateTotalPrice())
				totalPrice += cart.getPrice();
		}
		return totalPrice;
	}
	
	
	private void triggerExceptionIfRecordNotExist(Object object, String keyWord, int id) {
		if (Objects.isNull(object)) {
            throw new RecordNotFoundException(String.format(ORDER_RECORD_NOT_FOUND_MESSAGE, keyWord, id));
        }
	}

}
