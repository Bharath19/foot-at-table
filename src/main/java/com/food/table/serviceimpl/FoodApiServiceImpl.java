package com.food.table.serviceimpl;

import com.food.table.constant.FoodOptionType;
import com.food.table.constant.FoodStatusEnum;
import com.food.table.dto.*;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.FoodsModel;
import com.food.table.model.FoodsRestaurantModel;
import com.food.table.repo.*;
import com.food.table.service.FoodApiService;
import com.food.table.util.AuthorityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodApiServiceImpl implements FoodApiService {

    final FoodRepository foodRepository;

    final DietRepository dietRepository;

    final CuisinesRepository cuisinesRepository;

    final RestaurantRepository restaurantRepository;

    final FoodCategoryRepository foodCategoryRepository;

    final FoodTagRepository foodTagRepository;

    final AuthorityUtil authorityUtil;

    @Override
    public FoodsModel insertFood(FoodsModel foodsModel) {
        return performSaveOrUpdate(foodsModel);

    }

    @Override
    @Cacheable( cacheNames = "getAllFoods")
    public List<FoodsModel> getAll() {
        List<FoodsModel> foodList = foodRepository.findAllFood().stream().map(FoodsModel::convertDtoToModel).collect(Collectors.toList());
        return foodList;
    }

    @Override
    @Cacheable( cacheNames = "getFoodById" , key ="#id")
    public FoodsModel getById(int id) {
        Foods food = foodRepository.findFoodById(id);
        checkRecordNotFoundException(food);
        return FoodsModel.convertDtoToModel(food);
    }

    @Override
    public boolean deleteById(int id) {
        Foods foods = foodRepository.findFoodById(id);
        checkRecordNotFoundException(foods);
        checkAuthority(foods.getRestaurant().getId());
        foods.setDeleteFlag(1);
        foods.setDeletionDate(Timestamp.valueOf(LocalDateTime.now()));
        foodRepository.save(foods);
        return true;
    }

    @Override    
    @CachePut(cacheNames = "getFoodById" , key = "#id")
    @CacheEvict(cacheNames = "getAllFoods" ,allEntries = true)    
    public FoodsModel updateById(int id, FoodsModel foodsModel) {
        Foods foods = foodRepository.findFoodById(id);
        checkRecordNotFoundException(foods);
        foodsModel.setId(id);
        return performSaveOrUpdate(foodsModel);
    }

    @Override
    @Cacheable(cacheNames = "getFoodsByRestaurantId" , key = "#restaurantId")
    public List<FoodsRestaurantModel> getFoodsByRestaurantId(int restaurantId) {
        List<Foods> foodsList = foodRepository.findFoodsByRestaurantId(restaurantId);
        if (CollectionUtils.isEmpty(foodsList))
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_RESTAURANT_ID);
        List<FoodsModel> foodsModelList = foodsList.stream().map(FoodsModel::convertDtoToModel).collect(Collectors.toList());
        Map<Integer, List<FoodsModel>> foodsMap = foodsModelList.stream().collect(Collectors.groupingBy(e -> e.getFoodCategoryId()));
        Comparator<FoodsModel> compareBySortNo = (FoodsModel foodsModel1, FoodsModel foodsModel2) -> foodsModel1.getSortNo().compareTo(foodsModel2.getSortNo());
        List<FoodCategory> foodsCategoryList = foodCategoryRepository.findFoodCategoriesByRestaurantId(restaurantId);
        Comparator<FoodCategory> compareCategoryBySortNo = (FoodCategory foodsCategoryModel1, FoodCategory foodsCategoryModel2) -> foodsCategoryModel1.getSortOrder().compareTo(foodsCategoryModel2.getSortOrder());
        Collections.sort(foodsCategoryList, compareCategoryBySortNo);
        List<FoodsRestaurantModel> foodResponseModels = new ArrayList<>();
        foodsCategoryList.stream()
                .filter(foodCategory ->
                        foodsMap.containsKey(foodCategory.getId()))
                .forEach(foodCategory -> {
            List<FoodsModel> foodsStreamList = foodsMap.get(foodCategory.getId());
            Collections.sort(foodsStreamList, compareBySortNo);
            FoodsRestaurantModel foodsRestaurantModel = new FoodsRestaurantModel();
            foodsRestaurantModel.setFoodCategoryId(foodCategory.getId());
            foodsRestaurantModel.setFoodCategoryName(foodCategory.getName());
            foodsRestaurantModel.setFoods(foodsStreamList);
            foodResponseModels.add(foodsRestaurantModel);
        });
        return foodResponseModels;
    }

	@Override
	@CachePut(cacheNames = "getFoodById" , key = "#id")
	@Caching( evict = {
			 @CacheEvict(cacheNames = "getAllFoods" ,allEntries = true) ,
			 @CacheEvict(cacheNames = "getFoodsByRestaurantId" ,allEntries = true) 
	})   
	public boolean updateStatus(int id, String status) {
		try {
	        Foods food = foodRepository.findFoodById(id);
            checkRecordNotFoundException(food);
	        food.setStatus(FoodStatusEnum.getValue(status));
	        foodRepository.save(food);
	        return true;
		}catch (Exception e) {
			log.error("unable to update food status "+ e);
		}
		return false;
	}
	
    private FoodsModel performSaveOrUpdate(FoodsModel foodsModel) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(foodsModel.getRestaurantId());
        if (!restaurant.isPresent())
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_RESTAURANT_ID);
        checkAuthority(foodsModel.getRestaurantId());
        Optional<Diets> diets = dietRepository.findById(foodsModel.getDietId());
        if (!diets.isPresent())
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_DIET_ID);
        Optional<Cuisines> cuisines = cuisinesRepository.findById(foodsModel.getCuisineId());
        if (!cuisines.isPresent())
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_CUISINES_ID);
        Optional<FoodCategory> foodCategory = foodCategoryRepository.findById(foodsModel.getFoodCategoryId());
        if (!foodCategory.isPresent())
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_FOOD_CATEGORY_ID);
        List<FoodTag> foodTags = foodTagRepository.findAllById(foodsModel.getTags());
        if (CollectionUtils.isEmpty(foodTags))
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_FOOD_TAGS_ID);
        if (FoodStatusEnum.getValue(foodsModel.getStatus()) == 0)
        	throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_FOOD_STATUS);
        
        if (Objects.nonNull(foodsModel.getExtras())) {
            foodsModel.getExtras().forEach(foodOptionMetaModel -> {
            	if (FoodOptionType.getValue(foodOptionMetaModel.getType()) == 0) {
            		throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_FOOD_OPTION_META_TYPE);
            	}
            	if (FoodStatusEnum.getValue(foodOptionMetaModel.getStatus()) == 0) {
            		throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_FOOD_OPTION_META_STATUS);
            	}
            	foodOptionMetaModel.getFoodOptionsModels().forEach(foodoption ->{
            		if (FoodStatusEnum.getValue(foodoption.getStatus()) == 0) {
                		throw new ApplicationException(HttpStatus.BAD_REQUEST, ApplicationErrors.INVALID_FOOD_OPTIONS_STATUS);
                	}
            	});
            });
        }

        List<FoodOptionMeta> foodOptionMetaList = foodsModel.getExtras().stream()
                .map(foodOptionMeta -> FoodOptionMeta.convertModelToDto(foodOptionMeta))
                .collect(Collectors.toList());
        Foods savedFoods = foodRepository.save(
                Foods.convertModelToDto(foodsModel, diets.get(), cuisines.get(), foodCategory.get(),
                        foodOptionMetaList, restaurant.get(), foodTags
                ));
        if (Objects.isNull(savedFoods))
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.ADD_UPDATE_FOOD_FAILED);
        return FoodsModel.convertDtoToModel(savedFoods);
    }

    private void checkRecordNotFoundException(Foods foods) {
        if (Objects.isNull(foods)) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_FOOD_ID);
        }
    }

    private void checkAuthority(int restaurantId) {
        UserAccount userDetails = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authorityUtil.checkAuthority(userDetails, restaurantId);
    }

}
