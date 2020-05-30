package com.food.table.service;

import com.food.table.model.RestaurantEmployeeEditModel;
import com.food.table.model.RestaurantEmployeeRequestModel;
import com.food.table.model.RestaurantEmployeeResponseModel;

import java.util.List;

public interface RestaurantEmployeeService {

    RestaurantEmployeeResponseModel insertNewEmployee(RestaurantEmployeeRequestModel restaurantEmployeeRequestModel);

    RestaurantEmployeeResponseModel updateEmployee(int id, RestaurantEmployeeEditModel restaurantEmployeeRequestModel);

    void setEmployeeStatus(int id, String status);

    void deleteEmployeeStatus(int id);

    RestaurantEmployeeResponseModel getEmployeeById(int id);

    List<RestaurantEmployeeResponseModel> getEmployeesByRestaurantId(int restaurantId);
}
