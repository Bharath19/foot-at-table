package com.food.table.model;

import com.food.table.constant.FoodStatusEnum;
import com.food.table.dto.RestaurantTable;
import lombok.Data;

@Data
public class RestaurantTableModel {

    private int id;

    private int restaurantId;

    private String name;

    private Integer seats;

    private String status;

    public static RestaurantTableModel convertDtoToModel(RestaurantTable restaurantTable) {
        RestaurantTableModel restaurantTableModel = new RestaurantTableModel();
        restaurantTableModel.setId(restaurantTable.getId());
        restaurantTableModel.setRestaurantId(restaurantTable.getRestaurant().getId());
        restaurantTableModel.setName(restaurantTable.getName());
        restaurantTableModel.setSeats(restaurantTable.getSeats());
        restaurantTableModel.setStatus(FoodStatusEnum.getName(restaurantTable.getStatus()));
        return restaurantTableModel;
    }

}
