package com.food.table.controller;

import com.food.table.model.RestaurantEmployeeEditModel;
import com.food.table.model.RestaurantEmployeeRequestModel;
import com.food.table.model.RestaurantEmployeeResponseModel;
import com.food.table.service.RestaurantEmployeeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestaurantEmployeeController {

    private final RestaurantEmployeeService restaurantEmployeeService;

    @ApiOperation(value = "Add new restaurant employee", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/restaurant/employee", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<RestaurantEmployeeResponseModel> insertRestaurantEmployee(@RequestBody RestaurantEmployeeRequestModel restaurantEmployeeRequestModel) {
        return ResponseEntity.ok(restaurantEmployeeService.insertNewEmployee(restaurantEmployeeRequestModel));
    }

    @ApiOperation(value = "Update restaurant employee", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/restaurant/employee/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<RestaurantEmployeeResponseModel> updateRestaurantEmployee(@PathVariable @NonNull int id, @RequestBody RestaurantEmployeeEditModel restaurantEmployeeEditModel) {
        return ResponseEntity.ok(restaurantEmployeeService.updateEmployee(id, restaurantEmployeeEditModel));
    }

    @ApiOperation(value = "Delete restaurant employee", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/restaurant/employee/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<Void> deleteRestaurantEmployee(@PathVariable @NonNull int id) {
        restaurantEmployeeService.deleteEmployeeStatus(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "View restaurant employee by id", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/restaurant/employee/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<RestaurantEmployeeResponseModel> getEmployeeById(@PathVariable @NonNull int id) {
        return ResponseEntity.ok(restaurantEmployeeService.getEmployeeById(id));
    }

    @ApiOperation(value = "View restaurant employees by restaurant id", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/restaurant/employee/restaurant/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<List<RestaurantEmployeeResponseModel>> getEmployeesByRestaurantId(@PathVariable @NonNull int id) {
        return ResponseEntity.ok(restaurantEmployeeService.getEmployeesByRestaurantId(id));
    }

    @ApiOperation(value = "Change Status of employee", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/restaurant/employee/status/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<Void> changeEmployeeStatus(@PathVariable @NonNull int id, @RequestParam(value = "status", required = true) String status) {
        restaurantEmployeeService.setEmployeeStatus(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
