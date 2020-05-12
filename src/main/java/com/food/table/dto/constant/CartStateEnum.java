package com.food.table.dto.constant;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum CartStateEnum {

	REQUESTED(1, "requested"),
	CONFIRMED(2, "confirmed"),
	REJECTED(3, "rejected"),
	CANCEL_REQUESTED(4, "cancel requested"),
	CANCELLED(5, "cancelled"),
	PREPARING(6, "preparing"),
	DELIVERED(7, "delivered");

	@Getter
	@Setter
	int id;
	
	@Getter
	@Setter
	String name;
	
    public static int getValue(String name) {
        int id = 0;
        Optional<CartStateEnum> cartStateEnum = Arrays.stream(CartStateEnum.values()).filter(optionType -> optionType.getName().equalsIgnoreCase(name)).findFirst();
        if (cartStateEnum.isPresent()) {
            id = cartStateEnum.get().getId();
        }
        return id;
    }
    
    public static String getValue(int _id) {
    	String value = "";
        Optional<CartStateEnum> cartStateEnum = Arrays.stream(CartStateEnum.values()).filter(optionType -> (optionType.getId() == _id)).findFirst();
        if (cartStateEnum.isPresent()) {
        	value = cartStateEnum.get().getName();
        }
    	return value;
    }

}
