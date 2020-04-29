package com.food.table.model;

import lombok.Data;

@Data
public class FoodOptionsModel {
    public int id;
    public String name;
    public String description;
    public String imageUrl;
    public String status;
    public int sortNo;
}
