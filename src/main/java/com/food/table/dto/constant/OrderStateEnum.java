package com.food.table.dto.constant;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum OrderStateEnum {

	REQUESTED(1, "requested"),
	ORDER_CONFIRMED(2, "order confirmed"),
	ORDER_REJECTED(3, "order rejected"),
	CANCEL_REQUESTED(4, "cancel requested"),
	CANCELLED(5, "cancelled"),
	INPROGRESS(6, "inprogress"),
	BILL_REQUESTED(7, "bill requested"),
	PAYMENT_FAILED(8, "bill failed"),
	PAYMENT_COMPLETED(9, "bill completed");
	
	@Getter
	@Setter
	int id;
	
	@Getter
	@Setter
	String name;
	
    public static int getValue(String name) {
        int id = 0;
        Optional<OrderStateEnum> orderStateEnum = Arrays.stream(OrderStateEnum.values()).filter(optionType -> optionType.getName().equalsIgnoreCase(name)).findFirst();
        if (orderStateEnum.isPresent()) {
            id = orderStateEnum.get().getId();
        }
        return id;
    }

    public static String getValue(int _id) {
    	String value = "";
        Optional<OrderStateEnum> orderStateEnum = Arrays.stream(OrderStateEnum.values()).filter(optionType -> (optionType.getId() == _id)).findFirst();
        if (orderStateEnum.isPresent()) {
        	value = orderStateEnum.get().getName();
        }
    	return value;
    }

}
