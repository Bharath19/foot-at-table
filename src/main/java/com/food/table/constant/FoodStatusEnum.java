package com.food.table.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static String getName(int id) {
        String name = StringUtils.EMPTY;
        AtomicInteger atomicId = new AtomicInteger();
        atomicId.set(id);
        Optional<FoodStatusEnum> foodStatusEnum = Arrays.stream(FoodStatusEnum.values()).filter(optionType -> optionType.getId() == atomicId.get()).findFirst();
        if (foodStatusEnum.isPresent()) {
            name = foodStatusEnum.get().getName();
        }
        return name;
    }
}
