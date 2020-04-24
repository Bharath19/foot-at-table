package com.food.table.model;

import com.food.table.dto.Tiers;
import lombok.Data;

@Data
public class TiersModel {

    long id;

    String name;

    String description;

    public static TiersModel convertDtoToModel(Tiers tiers){
        TiersModel tiersModel = new TiersModel();
        tiersModel.setId(tiers.getId());
        tiersModel.setName(tiers.getName());
        tiersModel.setDescription(tiers.getDescription());
        return tiersModel;
    }

}
