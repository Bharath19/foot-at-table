package com.food.table.dto.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum FoodStatusEnum {
    ACTIVE(1, "active"),
    IN_ACTIVE(2, "inactive");

    @Getter
    @Setter
    int id;

    @Getter
    @Setter
    String name;

    public static int getValue(String name) {
        int id = 0;
        Optional<FoodStatusEnum> foodStatusEnum = Arrays.stream(FoodStatusEnum.values()).filter(foodStatus -> foodStatus.getName().equalsIgnoreCase(name)).findFirst();
        if (foodStatusEnum.isPresent()) {
            id = foodStatusEnum.get().getId();
        }
        return id;
    }
}
