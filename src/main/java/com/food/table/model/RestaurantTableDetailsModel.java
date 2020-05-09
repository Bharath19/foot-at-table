package com.food.table.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantTableDetailsModel {

    int restaurantId;

    int tableId;
}