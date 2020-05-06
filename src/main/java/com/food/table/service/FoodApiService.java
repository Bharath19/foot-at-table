package com.food.table.service;

import com.food.table.model.FoodsModel;

import java.util.List;

public interface FoodApiService {

    FoodsModel insertFood(FoodsModel foodsModel);

    List<FoodsModel> getAll();

    FoodsModel getById(int id);

    boolean deleteById(int id);

    FoodsModel updateById(int id, FoodsModel foodsModel);

    List<FoodsModel> getFoodsByRestaurantId(int restaurantId);

}
