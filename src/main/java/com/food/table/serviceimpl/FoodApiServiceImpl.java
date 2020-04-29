package com.food.table.serviceimpl;

import com.food.table.dto.*;
import com.food.table.model.FoodsModel;
import com.food.table.repo.*;
import com.food.table.service.FoodApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodApiServiceImpl implements FoodApiService {

    final FoodRepository foodRepository;

    final DietRepository dietRepository;

    final CuisinesRepository cuisinesRepository;

    final RestaurantRepository restaurantRepository;

    final FoodCategoryRepository foodCategoryRepository;

    final FoodTagRepository foodTagRepository;

    @Override
    public void insertFood(FoodsModel foodsModel) {
        Diets diets = dietRepository.getOne(foodsModel.getDietId());
        Cuisines cuisines = cuisinesRepository.getOne(foodsModel.getCuisineId());
        FoodCategory foodCategory = foodCategoryRepository.getOne(foodsModel.getFoodCategoryId());
        Optional<Restaurant> restaurant = restaurantRepository.findById(foodsModel.getRestaurantId());
        List<FoodTag> foodTags = foodTagRepository.findAllById(foodsModel.getTags());
        List<FoodOptions> foodOptionsList = new ArrayList<>();
        foodsModel.getExtras().forEach(foodOptionMetaModel -> {
            foodOptionMetaModel.getFoodOptionsModels().stream().forEach(foodOptionsModel -> {
                foodOptionsList.add(FoodOptions.convertModelToDto(foodOptionsModel));
            });
        });
        List<FoodOptionMeta> foodOptionMetaList = foodsModel.getExtras().stream()
                .map(foodOptionMeta -> FoodOptionMeta.convertModelToDto(foodOptionMeta, foodOptionsList))
                .collect(Collectors.toList());
        foodRepository.save(
                Foods.convertModelToDto(foodsModel, diets, cuisines, foodCategory,
                        foodOptionMetaList, restaurant.get(), foodTags
                ));

    }
}
