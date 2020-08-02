package com.food.table.model;

import lombok.Data;

@Data
public class RestaurantTableRequestModel {

    private int restaurantId;

    private String name;

    private Integer seats;

    private String status;
}
