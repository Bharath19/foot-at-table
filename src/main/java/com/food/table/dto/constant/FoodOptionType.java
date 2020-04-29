package com.food.table.dto.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum FoodOptionType {
    SINGLE(1, "single"),
    MULTIPLE(2, "multiple");

    @Getter
    @Setter
    int id;

    @Getter
    @Setter
    String name;

    public static int getValue(String name) {
        int id = 0;
        Optional<FoodOptionType> foodOptionType = Arrays.stream(FoodOptionType.values()).filter(optionType -> optionType.getName().equalsIgnoreCase(name)).findFirst();
        if (foodOptionType.isPresent()) {
            id = foodOptionType.get().getId();
        }
        return id;
    }
}
