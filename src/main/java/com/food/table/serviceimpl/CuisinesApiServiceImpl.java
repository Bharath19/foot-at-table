package com.food.table.serviceimpl;

import com.food.table.dto.Cuisines;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.CuisinesModel;
import com.food.table.repo.CuisinesRepository;
import com.food.table.service.CuisinesApiService;
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
public class CuisinesApiServiceImpl implements CuisinesApiService {

    final CuisinesRepository cusinesRepository;

    @Override
    public List<CuisinesModel> getAll() {
        return  cusinesRepository.findAll().stream().map(CuisinesModel::convertDtoToModel).collect(Collectors.toList());
    }

    @Override
    public CuisinesModel getById(int id) {
        Optional<Cuisines> cuisine = cusinesRepository.findById(id);
        if (!cuisine.isPresent())
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_CUISINES_ID);
        return CuisinesModel.convertDtoToModel(cuisine.get());
    }

    @Override
    public CuisinesModel insertCuisine(CuisinesModel cuisinesModel) {
        Cuisines cuisine = cusinesRepository.save(Cuisines.convertModelToDto(cuisinesModel));
        if (Objects.isNull(cuisine))
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.ADD_CUISINES_FAILED);
        return CuisinesModel.convertDtoToModel(cuisine);
    }

    @Override
    public CuisinesModel updateCuisineById(int id, CuisinesModel cuisinesModel) {
        Optional<Cuisines> cuisines = cusinesRepository.findById(id);
        if (!cuisines.isPresent())
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_CUISINES_ID);
        cuisines.get().setName(cuisinesModel.getName());
        Cuisines savedCuisines = cusinesRepository.save(cuisines.get());
        if (Objects.isNull(savedCuisines))
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.UPDATE_CUISINES_FAILED);
        return CuisinesModel.convertDtoToModel(savedCuisines);
    }
}
