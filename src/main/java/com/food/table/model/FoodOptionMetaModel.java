package com.food.table.model;

import com.food.table.constant.FoodOptionType;
import com.food.table.constant.FoodStatusEnum;
import com.food.table.dto.FoodOptionMeta;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FoodOptionMetaModel implements Serializable{
    @ApiModelProperty(value = "Food Option Meta type should be single or multiple")
    private String type = "multiple";
    private String name;
    private int id;
    private String description;
    @ApiModelProperty(value = "Food Option Meta status should be active or inactive")
    private String status = "active";
    private List<FoodOptionsModel> foodOptionsModels;

    public static FoodOptionMetaModel convertDtoToModel(FoodOptionMeta foodOptionMeta) {
        Comparator<FoodOptionsModel> compareBySortNo = (FoodOptionsModel foodsModel1, FoodOptionsModel foodsModel2) -> foodsModel1.getSortNo().compareTo(foodsModel2.getSortNo());
        List<FoodOptionsModel> foodOptionsModelList = foodOptionMeta.getFoodOptions().stream().map(FoodOptionsModel::convertDtoToModel).collect(Collectors.toList());
        Collections.sort(foodOptionsModelList, compareBySortNo);
        FoodOptionMetaModel foodOptionMetaModel = new FoodOptionMetaModel();
        foodOptionMetaModel.setType(FoodOptionType.getName(foodOptionMeta.getOptionType()));
        foodOptionMetaModel.setId(foodOptionMeta.getId());
        foodOptionMetaModel.setName(foodOptionMeta.getOptionName());
        foodOptionMetaModel.setDescription(foodOptionMeta.getOptionDescription());
        foodOptionMetaModel.setStatus(FoodStatusEnum.getName(foodOptionMeta.getStatus()));
        foodOptionMetaModel.setFoodOptionsModels(foodOptionsModelList);
        return foodOptionMetaModel;
    }
}
