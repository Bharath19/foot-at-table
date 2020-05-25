package com.food.table.serviceimpl;

import com.food.table.dto.SearchType;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.SearchTagModel;
import com.food.table.repo.SearchTypeRepository;
import com.food.table.service.SearchTagApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Optional<SearchType> searchTag = searchTagRepository.findById(id);
        checkExistence(searchTag);
        return SearchTagModel.convertDtoToModel(searchTagRepository.getOne(id));
    }

    @Override
    public SearchTagModel insertSearchTag(SearchTagModel searchTagModel) {
        SearchType searchType = searchTagRepository.save(SearchType.convertModelToDto(searchTagModel));
        if (Objects.isNull(searchType))
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.ADD_SEARCH_TAG_FAILED);
        return SearchTagModel.convertDtoToModel(searchTagRepository.save(searchType));
    }

    @Override
    public SearchTagModel updateSearchTagById(int id, SearchTagModel searchTagModel) {
        Optional<SearchType> searchTag = searchTagRepository.findById(id);
        checkExistence(searchTag);
        searchTag.get().setName(searchTagModel.getName());
        SearchType savedSearchType = searchTagRepository.save(searchTag.get());
        if (Objects.isNull(savedSearchType))
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.UPDATE_SEARCH_TAG_FAILED);
        return SearchTagModel.convertDtoToModel(savedSearchType);
    }

    private void checkExistence(Optional<SearchType> searchTag) {
        if (!searchTag.isPresent()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_SEARCH_TAG_ID);
        }
    }
}
