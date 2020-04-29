package com.food.table.model;

import lombok.Data;

import java.util.List;

@Data
public class FoodOptionMetaModel {
    public String type;
    public String name;
    public int id;
    public String description;
    public String status;
    public List<FoodOptionsModel> foodOptionsModels;
}
