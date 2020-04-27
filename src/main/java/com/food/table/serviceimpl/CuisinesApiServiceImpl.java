package com.food.table.serviceimpl;

import com.food.table.dto.Cuisines;
import com.food.table.model.CuisinesModel;
import com.food.table.repo.CuisinesRepository;
import com.food.table.service.CuisinesApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return CuisinesModel.convertDtoToModel(cusinesRepository.getOne(id));
    }

    @Override
    public CuisinesModel insertCuisine(CuisinesModel cuisinesModel) {
        return CuisinesModel.convertDtoToModel(cusinesRepository.save(Cuisines.convertModelToDto(cuisinesModel)));
    }

    @Override
    public CuisinesModel updateCuisineById(int id, CuisinesModel cuisinesModel) {
        Cuisines cuisines = cusinesRepository.getOne(id);
        cuisines.setName(cuisinesModel.getName());
        return CuisinesModel.convertDtoToModel(cusinesRepository.save(cuisines));
    }
}
