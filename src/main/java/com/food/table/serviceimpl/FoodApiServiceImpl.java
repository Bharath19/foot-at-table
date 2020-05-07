package com.food.table.serviceimpl;

import com.food.table.dto.*;
import com.food.table.exceptions.RecordNotFoundException;
import com.food.table.model.FoodsModel;
import com.food.table.repo.*;
import com.food.table.service.FoodApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    private final String FOOD_SINGLE_RECORD_ERROR_MESSAGE = "No Record Found in Foods Table for id : ";

    private final String FOOD_MULTIPLE_RECORD_ERROR_MESSAGE = "No Records Found in Foods Table";

    @Override
    public FoodsModel insertFood(FoodsModel foodsModel) {
        return performSaveOrUpdate(foodsModel);

    }

    @Override
    public List<FoodsModel> getAll() {
        List<FoodsModel> foodList = foodRepository.findAllFood().stream().map(FoodsModel::convertDtoToModel).collect(Collectors.toList());
        checkRecordNotFoundException(foodList, 0, FOOD_MULTIPLE_RECORD_ERROR_MESSAGE);
        return foodList;
    }

    @Override
    public FoodsModel getById(int id) {
        Foods food = foodRepository.findFoodById(id);
        checkRecordNotFoundException(food, id, FOOD_SINGLE_RECORD_ERROR_MESSAGE);
        return FoodsModel.convertDtoToModel(food);
    }

    @Override
    public boolean deleteById(int id) {
        Foods foods = foodRepository.findFoodById(id);
        checkRecordNotFoundException(foods, id, FOOD_SINGLE_RECORD_ERROR_MESSAGE);
        foods.setDeleteFlag(1);
        foods.setDeletionDate(Timestamp.valueOf(LocalDateTime.now()));
        foodRepository.save(foods);
        return true;
    }

    @Override
    public FoodsModel updateById(int id, FoodsModel foodsModel) {
        Foods foods = foodRepository.findFoodById(id);
        checkRecordNotFoundException(foods, id, FOOD_SINGLE_RECORD_ERROR_MESSAGE);
        foodsModel.setId(id);
        return performSaveOrUpdate(foodsModel);
    }

    @Override
    public List<FoodsModel> getFoodsByRestaurantId(int restaurantId) {
        List<Foods> foodsList = foodRepository.findFoodsByRestaurantId(restaurantId);
        if (CollectionUtils.isEmpty(foodsList))
            throw new RecordNotFoundException("No Records found in food table for restaurant id: " + restaurantId);
        return foodsList.stream().map(FoodsModel::convertDtoToModel).collect(Collectors.toList());
    }

    private FoodsModel performSaveOrUpdate(FoodsModel foodsModel) {
        Optional<Diets> diets = dietRepository.findById(foodsModel.getDietId());
        if (!diets.isPresent())
            throw new RecordNotFoundException("No Record Found in Diets Table for id :" + foodsModel.getDietId());
        Optional<Cuisines> cuisines = cuisinesRepository.findById(foodsModel.getCuisineId());
        if (!cuisines.isPresent())
            throw new RecordNotFoundException("No Record Found in Cuisines Table for id :" + foodsModel.getCuisineId());
        Optional<FoodCategory> foodCategory = foodCategoryRepository.findById(foodsModel.getFoodCategoryId());
        if (!foodCategory.isPresent())
            throw new RecordNotFoundException("No Record Found in FoodCategory Table for id :" + foodsModel.getFoodCategoryId());
        Optional<Restaurant> restaurant = restaurantRepository.findById(foodsModel.getRestaurantId());
        if (!restaurant.isPresent())
            throw new RecordNotFoundException("No Record Found in Restaurant Table for id :" + foodsModel.getRestaurantId());
        List<FoodTag> foodTags = foodTagRepository.findAllById(foodsModel.getTags());
        if (CollectionUtils.isEmpty(foodTags))
            throw new RecordNotFoundException("No Records Found in FoodTag Table");
        List<FoodOptionMeta> foodOptionMetaList = foodsModel.getExtras().stream()
                .map(foodOptionMeta -> FoodOptionMeta.convertModelToDto(foodOptionMeta))
                .collect(Collectors.toList());
        return FoodsModel.convertDtoToModel(foodRepository.save(
                Foods.convertModelToDto(foodsModel, diets.get(), cuisines.get(), foodCategory.get(),
                        foodOptionMetaList, restaurant.get(), foodTags
                )));
    }

    private void checkRecordNotFoundException(Object table, int id, String message) {
        if (id != 0) {
            message = message + id;
            if (Objects.isNull(table)) {
                throw new RecordNotFoundException(message);
            }
        }
    }
}
