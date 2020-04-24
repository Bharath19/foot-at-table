package com.food.table.service;

import com.food.table.model.TiersModel;

import java.util.List;

public interface TierApiService {
    
    List<TiersModel> getAll();

    TiersModel getById(long id);

    TiersModel insertTier(TiersModel tiersModel);

    TiersModel updateTierById(long id,TiersModel tiersModel);


}
