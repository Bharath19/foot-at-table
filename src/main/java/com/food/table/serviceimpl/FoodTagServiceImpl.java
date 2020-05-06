package com.food.table.serviceimpl;

import com.food.table.model.FoodTagModel;
import com.food.table.repo.FoodTagRepository;
import com.food.table.service.FoodTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodTagServiceImpl implements FoodTagService {

    final FoodTagRepository foodTagRepository;

    @Override
    public List<FoodTagModel> getAll() {
        return foodTagRepository.findAll().stream().map(FoodTagModel::convertDtoToModel).collect(Collectors.toList());
    }
}
