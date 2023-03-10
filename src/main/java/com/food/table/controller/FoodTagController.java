package com.food.table.controller;

import com.food.table.model.FoodTagModel;
import com.food.table.service.FoodTagService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodTagController {

    final FoodTagService foodTagService;

    @ApiOperation(value = "Get list of Food Tag", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/foodtag", method = RequestMethod.GET)
    public ResponseEntity<List<FoodTagModel>> getAllFoodTags() {
        return ResponseEntity.ok(foodTagService.getAll());
    }
}
