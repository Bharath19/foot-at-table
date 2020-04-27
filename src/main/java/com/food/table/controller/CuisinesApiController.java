package com.food.table.controller;

import com.food.table.model.CuisinesModel;
import com.food.table.service.CuisinesApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CuisinesApiController {

    final CuisinesApiService cuisinesApiService;

    @RequestMapping(value="/cuisines",method=RequestMethod.GET)
    public ResponseEntity<List<CuisinesModel>> getAllTiers(){
        return ResponseEntity.ok(cuisinesApiService.getAll());
    }

    @RequestMapping({"/cuisines/{id}"})
    public ResponseEntity<CuisinesModel>getTierById(@NotNull @PathVariable("id") int id){
        return ResponseEntity.ok(cuisinesApiService.getById(id));
    }

    @RequestMapping(value="/cuisines",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CuisinesModel> insertTier(@RequestBody CuisinesModel cuisinesModel){
        return ResponseEntity.ok(cuisinesApiService.insertCuisine(cuisinesModel));
    }

    @RequestMapping(value="/cuisines/{id}",method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CuisinesModel> updateTierById(@NotNull @PathVariable("id") int id, @RequestBody CuisinesModel cusinesModel){
        return ResponseEntity.ok(cuisinesApiService.updateCuisineById(id,cusinesModel));
    }
}
