package com.food.table.service;

import com.food.table.model.RestaurantTableDetailsModel;
import com.food.table.model.RestaurantTableModel;
import com.food.table.model.RestaurantTableRequestModel;

import java.util.List;

public interface RestaurantTableService {

    RestaurantTableModel insertTable(RestaurantTableRequestModel restaurantTableModel);

    RestaurantTableModel getById(int id);

    List<RestaurantTableModel> getAllByRestaurantId(int restaurantId ,String tableName);

    List<RestaurantTableModel> getAllTable();

    boolean deleteById(int id);

    RestaurantTableModel updateById(int id, RestaurantTableRequestModel restaurantTableModel);

    byte[] generateQRCode(int id);

    RestaurantTableDetailsModel getTableDetailsByQRCode(String qrCode);

    void assignEmployeeForTable(int tableId,int employeeId);

    void unAssignEmployeeForTable(int tableId,int employeeId);


}
