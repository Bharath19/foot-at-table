package com.food.table.controller;

import com.food.table.model.CuisinesModel;
import com.food.table.service.CuisinesApiService;
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
public class CuisinesApiController {

    final CuisinesApiService cuisinesApiService;

    @ApiOperation(value = "View list of all cuisines", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value="/cuisines",method=RequestMethod.GET)
    public ResponseEntity<List<CuisinesModel>> getAllTiers(){
        return ResponseEntity.ok(cuisinesApiService.getAll());
    }

    @ApiOperation(value = "Get Cuisines by Id", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping({"/cuisines/{id}"})
    public ResponseEntity<CuisinesModel>getTierById(@NotNull @PathVariable("id") int id){
        return ResponseEntity.ok(cuisinesApiService.getById(id));
    }

    @ApiOperation(value = "Insert a new cuisine", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value="/cuisines",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<CuisinesModel> insertTier(@RequestBody CuisinesModel cuisinesModel){
        return ResponseEntity.ok(cuisinesApiService.insertCuisine(cuisinesModel));
    }

    @ApiOperation(value = "Update a cuisine", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value="/cuisines/{id}",method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<CuisinesModel> updateTierById(@NotNull @PathVariable("id") int id, @RequestBody CuisinesModel cusinesModel){
        return ResponseEntity.ok(cuisinesApiService.updateCuisineById(id,cusinesModel));
    }
}
