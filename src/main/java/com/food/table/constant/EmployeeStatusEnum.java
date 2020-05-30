package com.food.table.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public enum EmployeeStatusEnum {
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
        Optional<EmployeeStatusEnum> employeeStatusEnum = Arrays.stream(EmployeeStatusEnum.values())
                .filter(employeeStatus -> employeeStatus.getName()
                        .equalsIgnoreCase(name)).findFirst();
        if (employeeStatusEnum.isPresent()) {
            id = employeeStatusEnum.get().getId();
        }
        return id;
    }

    public static String getName(int id) {
        String name = StringUtils.EMPTY;
        AtomicInteger atomicId = new AtomicInteger();
        atomicId.set(id);
        Optional<EmployeeStatusEnum> employeeStatusEnum = Arrays.stream(EmployeeStatusEnum.values()).filter(optionType -> optionType.getId() == atomicId.get()).findFirst();
        if (employeeStatusEnum.isPresent()) {
            name = employeeStatusEnum.get().getName();
        }
        return name;
    }
}
