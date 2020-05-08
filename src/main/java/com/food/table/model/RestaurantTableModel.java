package com.food.table.model;

import com.food.table.dto.RestaurantTable;
import lombok.Data;

@Data
public class RestaurantTableModel {

    private int id;

    private int restaurantId;

    private String name;

    public static RestaurantTableModel convertDtoToModel(RestaurantTable restaurantTable) {
        RestaurantTableModel restaurantTableModel = new RestaurantTableModel();
        restaurantTableModel.setId(restaurantTable.getId());
        restaurantTableModel.setRestaurantId(restaurantTable.getRestaurant().getId());
        restaurantTableModel.setName(restaurantTable.getName());
        return restaurantTableModel;
    }

}
