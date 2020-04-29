package com.food.table.controller;

import com.food.table.model.FoodsModel;
import com.food.table.service.FoodApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodApiController {

    final FoodApiService foodApiService;

    @RequestMapping(value = "/foods", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insertFood(@RequestBody FoodsModel foodsModel) {
        foodApiService.insertFood(foodsModel);
    }
}
