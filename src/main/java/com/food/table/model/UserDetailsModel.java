package com.food.table.model;

import com.food.table.dto.RestaurantEmployee;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDetailsModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String imageUrl;

    private long phoneNo;

    public static UserDetailsModel convertDtoToModel(RestaurantEmployee restaurantEmployee){
        UserDetailsModel userDetailsModel = new UserDetailsModel();
        userDetailsModel.setId(restaurantEmployee.getId());
        userDetailsModel.setName(restaurantEmployee.getName());
        userDetailsModel.setPhoneNo(restaurantEmployee.getPhoneNo());
        userDetailsModel.setImageUrl(restaurantEmployee.getUser().getImageUrl());
        return userDetailsModel;

    }


}
