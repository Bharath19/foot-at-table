package com.food.table.model;

import java.io.Serializable;

import com.food.table.dto.FoodTag;
import lombok.Data;

@Data
public class FoodTagModel implements Serializable{

    int id;
    String name;
    String description;

    public static FoodTagModel convertDtoToModel(FoodTag foodTag) {
        FoodTagModel foodTagModel = new FoodTagModel();
        foodTagModel.setId(foodTag.getId());
        foodTagModel.setName(foodTag.getName());
        foodTagModel.setDescription(foodTag.getDescription());
        return foodTagModel;
    }
}
