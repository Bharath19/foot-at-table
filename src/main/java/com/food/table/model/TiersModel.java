package com.food.table.model;

import java.io.Serializable;

import com.food.table.dto.Tiers;
import lombok.Data;

@Data
public class TiersModel implements Serializable {

	private static final long serialVersionUID = 1L;

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
