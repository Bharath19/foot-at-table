package com.food.table.model;

import com.food.table.constant.FoodStatusEnum;
import com.food.table.dto.FoodTag;
import com.food.table.dto.Foods;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
    private Integer sortNo;
    private String status;
    private List<FoodOptionMetaModel> extras;
    private List<Integer> tags;

    public static FoodsModel convertDtoToModel(Foods foods) {
        FoodsModel foodModel = new FoodsModel();
        try {
            foodModel.setId(foods.getId());
            foodModel.setName(foods.getName());
            foodModel.setImageUrl(foods.getImageUrl());
            foodModel.setPrice(foods.getPrice());
            foodModel.setDietId(foods.getDiets().getId());
            foodModel.setCuisineId(foods.getCuisines().getId());
            foodModel.setFoodCategoryId(foods.getFoodCategories().getId());
            foodModel.setExtras(foods.getExtras().stream().map(FoodOptionMetaModel::convertDtoToModel).collect(Collectors.toList()));
            foodModel.setStartTime(foods.getStartTime());
            foodModel.setEndTime(foods.getEndTime());
            foodModel.setSortNo(foods.getSortNo());
            foodModel.setDescription(foods.getDescription());
            foodModel.setRestaurantId(foods.getRestaurant().getId());
            foodModel.setTags(foods.getFoodTags().stream().map(FoodTag::getId).collect(Collectors.toList()));
            foodModel.setStatus(FoodStatusEnum.getName(foods.getStatus()));

        } catch (NullPointerException e) {
            log.error("No element found in food");
        }
        return foodModel;
    }

}
