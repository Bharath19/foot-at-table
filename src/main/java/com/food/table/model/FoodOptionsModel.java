package com.food.table.model;

import com.food.table.constant.FoodStatusEnum;
import com.food.table.dto.FoodOptions;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FoodOptionsModel {
    public int id;
    public String name;
    public String description;
    public String imageUrl;
    @ApiModelProperty(value = "Food Option stauts should be active or inactive")
    public String status = "active";
    public Integer sortNo;

    public static FoodOptionsModel convertDtoToModel(FoodOptions foodOptions) {
        FoodOptionsModel foodOptionsModel = new FoodOptionsModel();
        foodOptionsModel.setId(foodOptions.getId());
        foodOptionsModel.setName(foodOptions.getName());
        foodOptionsModel.setDescription(foodOptions.getDescription());
        foodOptionsModel.setImageUrl(foodOptions.getImageUrl());
        foodOptionsModel.setStatus(FoodStatusEnum.getName(foodOptions.getStatus()));
        foodOptionsModel.setSortNo(foodOptions.getSortNo());
        return foodOptionsModel;
    }
}
