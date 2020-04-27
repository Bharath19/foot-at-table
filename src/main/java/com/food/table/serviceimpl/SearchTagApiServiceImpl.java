package com.food.table.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.table.dto.SearchType;
import com.food.table.model.SearchTagModel;
import com.food.table.repo.SearchTypeRepository;
import com.food.table.service.SearchTagApiService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SearchTagApiServiceImpl implements SearchTagApiService {


    final SearchTypeRepository searchTagRepository;

    @Override
    public List<SearchTagModel> getAll() {
       return  searchTagRepository.findAll().stream().map(SearchTagModel::convertDtoToModel).collect(Collectors.toList());
    }

    @Override
    public SearchTagModel getById(int id) {
        return SearchTagModel.convertDtoToModel(searchTagRepository.getOne(id));
    }

    @Override
    public SearchTagModel insertSearchTag(SearchTagModel searchTagModel) {
        return SearchTagModel.convertDtoToModel(searchTagRepository.save(SearchType.convertModelToDto(searchTagModel)));
    }

    @Override
    public SearchTagModel updateSearchTagById(int id, SearchTagModel searchTagModel) {
        SearchType searchTag = searchTagRepository.getOne(id);
        searchTag.setName(searchTagModel.getName());
        return SearchTagModel.convertDtoToModel(searchTagRepository.save(searchTag));
    }
}
