package com.food.table.controller;

import com.food.table.model.FoodsModel;
import com.food.table.model.FoodsRestaurantModel;
import com.food.table.service.FoodApiService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodApiController {

    final FoodApiService foodApiService;

    @ApiOperation(value = "Add new food", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/foods", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<FoodsModel> insertFood(@RequestBody FoodsModel foodsModel) {
        return ResponseEntity.ok(foodApiService.insertFood(foodsModel));
    }

    @ApiOperation(value = "View list of all foods", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/foods", method = RequestMethod.GET)
    public ResponseEntity<List<FoodsModel>> getAllFoods() {
        return ResponseEntity.ok(foodApiService.getAll());
    }

    @ApiOperation(value = "View food by id", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/foods/{id}", method = RequestMethod.GET)
    public ResponseEntity<FoodsModel> getFoodById(@NotNull @PathVariable("id") int id) {
        return ResponseEntity.ok(foodApiService.getById(id));
    }

    @ApiOperation(value = "Delete a food", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/foods/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public void deleteFoodById(@NotNull @PathVariable("id") int id) {
        foodApiService.deleteById(id);
    }

    @ApiOperation(value = "Update a food", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/foods/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<FoodsModel> updateFoodById(@NotNull @PathVariable("id") int id, @RequestBody FoodsModel foodsModel) {
        return ResponseEntity.ok(foodApiService.updateById(id, foodsModel));
    }

    @ApiOperation(value = "View list of foods by restaurant id", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/foods/restaurant/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<FoodsRestaurantModel>> getFoodByRestaurantId(@NotNull @PathVariable("id") int id) {
        return ResponseEntity.ok(foodApiService.getFoodsByRestaurantId(id));
    }

    @ApiOperation(value = "Update the food status. it should be active/inactive", authorizations = {@Authorization(value = "accessToken")})
	@PutMapping("/foods/updateStatus/{id}")
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
	public boolean updateStatus(@PathVariable(required = true) int id, @RequestParam(value = "status", required = true, defaultValue = "inactive") String status) {
		return foodApiService.updateStatus(id, status);
	}

}

