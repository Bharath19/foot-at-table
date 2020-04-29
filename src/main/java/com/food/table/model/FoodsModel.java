package com.food.table.model;

import lombok.Data;

import java.util.List;

@Data
public class FoodsModel {

    private int id;
    private int restaurantId;
    private String description;
    private String name;
    private String imageUrl;
    private int dietId;
    private int cuisineId;
    private int foodCategoryId;
    private double price;
    private String endTime;
    private String startTime;
    private int sortNo;
    private String status;
    private List<FoodOptionMetaModel> extras;
    private List<Integer> tags;

}
