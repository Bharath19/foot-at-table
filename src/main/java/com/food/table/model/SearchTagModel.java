package com.food.table.model;

import java.io.Serializable;

import com.food.table.dto.SearchType;

import lombok.Data;

@Data
public class SearchTagModel implements Serializable  {

	private static final long serialVersionUID = 1L;

	int id;

    String name;

    String description;

    public static SearchTagModel convertDtoToModel(SearchType searchTag){
        SearchTagModel searchTagModel = new SearchTagModel();
        searchTagModel.setId(searchTag.getId());
        searchTagModel.setName(searchTag.getName());
        searchTagModel.setDescription(searchTag.getDescription());
        return searchTagModel;
    }

}
