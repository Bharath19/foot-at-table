package com.food.table.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.food.table.constant.FoodStatusEnum;
import com.food.table.dto.RestaurantTable;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import lombok.Data;

@Data
public class RestaurantTableModel  implements Serializable  {

	private static final long serialVersionUID = 1L;

	private int id;

    private int restaurantId;

    private String name;

    private Integer seats;

    private String status;

    private List<UserDetailsModel> asigneeDetails;

    public static RestaurantTableModel convertDtoToModel(RestaurantTable restaurantTable) {
        RestaurantTableModel restaurantTableModel = new RestaurantTableModel();
        restaurantTableModel.setId(restaurantTable.getId());
        restaurantTableModel.setRestaurantId(restaurantTable.getRestaurant().getId());
        restaurantTableModel.setName(restaurantTable.getName());
        restaurantTableModel.setSeats(restaurantTable.getSeats());
        restaurantTableModel.setStatus(FoodStatusEnum.getName(restaurantTable.getStatus()));
        if(CollectionUtils.isNotEmpty(restaurantTable .getRestaurantEmployees())) {
            restaurantTableModel.setAsigneeDetails(restaurantTable.getRestaurantEmployees().stream().map(UserDetailsModel::convertDtoToModel).collect(Collectors.toList()));
        }
        return restaurantTableModel;
    }

}
