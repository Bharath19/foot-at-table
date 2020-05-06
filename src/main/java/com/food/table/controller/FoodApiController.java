package com.food.table.controller;

import com.food.table.model.FoodsModel;
import com.food.table.service.FoodApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodApiController {

    final FoodApiService foodApiService;

    @RequestMapping(value = "/foods", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodsModel> insertFood(@RequestBody FoodsModel foodsModel) {
        return ResponseEntity.ok(foodApiService.insertFood(foodsModel));
    }

    @RequestMapping(value = "/foods", method = RequestMethod.GET)
    public ResponseEntity<List<FoodsModel>> getAllFoods() {
        return ResponseEntity.ok(foodApiService.getAll());
    }

    @RequestMapping(value = "/foods/{id}", method = RequestMethod.GET)
    public ResponseEntity<FoodsModel> getFoodById(@NotNull @PathVariable("id") int id) {
        return ResponseEntity.ok(foodApiService.getById(id));
    }

    @RequestMapping(value = "/foods/{id}", method = RequestMethod.DELETE)
    public void deleteFoodById(@NotNull @PathVariable("id") int id) {
        foodApiService.deleteById(id);
    }

    @RequestMapping(value = "/foods/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodsModel> updateFoodById(@NotNull @PathVariable("id") int id, @RequestBody FoodsModel foodsModel) {
        return ResponseEntity.ok(foodApiService.updateById(id, foodsModel));
    }

    @RequestMapping(value = "/foods/restaurant/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<FoodsModel>> getFoodByRestaurantId(@NotNull @PathVariable("id") int id) {
        return ResponseEntity.ok(foodApiService.getFoodsByRestaurantId(id));
    }

}

