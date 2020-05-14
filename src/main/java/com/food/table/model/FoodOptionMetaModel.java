package com.food.table.model;

import com.food.table.constant.FoodOptionType;
import com.food.table.constant.FoodStatusEnum;
import com.food.table.dto.FoodOptionMeta;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class FoodOptionMetaModel {
    public String type;
    public String name;
    public int id;
    public String description;
    public String status;
    public List<FoodOptionsModel> foodOptionsModels;

    public static FoodOptionMetaModel convertDtoToModel(FoodOptionMeta foodOptionMeta) {
        FoodOptionMetaModel foodOptionMetaModel = new FoodOptionMetaModel();
        foodOptionMetaModel.setType(FoodOptionType.getName(foodOptionMeta.getOptionType()));
        foodOptionMetaModel.setId(foodOptionMeta.getId());
        foodOptionMetaModel.setName(foodOptionMeta.getOptionName());
        foodOptionMetaModel.setDescription(foodOptionMeta.getOptionDescription());
        foodOptionMetaModel.setStatus(FoodStatusEnum.getName(foodOptionMeta.getStatus()));
        foodOptionMetaModel.setFoodOptionsModels(foodOptionMeta.getFoodOptions().stream().map(FoodOptionsModel::convertDtoToModel).collect(Collectors.toList()));
        return foodOptionMetaModel;
    }
}
