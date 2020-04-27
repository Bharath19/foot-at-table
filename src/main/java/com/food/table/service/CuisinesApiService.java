package com.food.table.service;

import com.food.table.model.CuisinesModel;

import java.util.List;

public interface CuisinesApiService {
    List<CuisinesModel> getAll();

    CuisinesModel getById(int id);

    CuisinesModel insertCuisine(CuisinesModel cuisinesModel);

    CuisinesModel updateCuisineById(int id, CuisinesModel cuisinesModel);
}
