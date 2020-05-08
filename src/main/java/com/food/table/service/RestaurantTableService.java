package com.food.table.service;

import com.food.table.model.FoodsModel;
import com.food.table.model.RestaurantTableModel;

import java.util.List;

public interface RestaurantTableService {

    RestaurantTableModel insertTable(RestaurantTableModel restaurantTableModel);

    RestaurantTableModel getById(int id);

    List<RestaurantTableModel> getAllByRestaurantId(int restaurantId);

    List<RestaurantTableModel> getAllTable();

    boolean deleteById(int id);

    RestaurantTableModel updateById(int id, RestaurantTableModel restaurantTableModel);

    byte[] generateQRCode(int id);

    List<FoodsModel> getFoodsByQRCode(String qrCode);


}
