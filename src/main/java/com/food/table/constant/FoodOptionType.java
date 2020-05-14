package com.food.table.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static String getName(int id) {
        String name = StringUtils.EMPTY;
        AtomicInteger atomicId = new AtomicInteger();
        atomicId.set(id);
        Optional<FoodOptionType> foodOptionType = Arrays.stream(FoodOptionType.values()).filter(optionType -> optionType.getId() == atomicId.get()).findFirst();
        if (foodOptionType.isPresent()) {
            name = foodOptionType.get().getName();
        }
        return name;
    }
}
