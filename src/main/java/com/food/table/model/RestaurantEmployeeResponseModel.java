package com.food.table.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RestaurantEmployeeResponseModel {

    private int id;

    private String employeeId;

    private String name;

    private String password;

    private String deviceId;

    private String deviceName;

    private int age;

    private String gender;

    private String bloodGroup;

    private long phoneNo;

    private String emailId;

    private String role;

    private String status;

    private int restaurantId;
}
