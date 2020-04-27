package com.food.table.service;

import com.food.table.model.SearchTagModel;

import java.util.List;

public interface SearchTagApiService {

    List<SearchTagModel> getAll();

    SearchTagModel getById(int id);

    SearchTagModel insertSearchTag(SearchTagModel searchTagModel);

    SearchTagModel updateSearchTagById(int id, SearchTagModel searchTagModel);
}
