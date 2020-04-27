package com.food.table.model;

import com.food.table.dto.Cuisines;
import lombok.Data;

@Data
public class CuisinesModel {
    int id;

    String name;

    String description;

    public static CuisinesModel convertDtoToModel(Cuisines cusines){
        CuisinesModel cusinesModel = new CuisinesModel();
        cusinesModel.setId(cusines.getId());
        cusinesModel.setName(cusines.getName());
        cusinesModel.setDescription(cusines.getDescription());
        return cusinesModel;
    }
}
