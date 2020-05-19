package com.food.table.controller;

import com.food.table.model.TiersModel;
import com.food.table.service.TierApiService;
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
public class TierApiController {

    final TierApiService  tierApiService;

    @RequestMapping(value="/tiers",method=RequestMethod.GET)
    public ResponseEntity<List<TiersModel>> getAllTiers(){
        return ResponseEntity.ok(tierApiService.getAll());
    }

    @RequestMapping({"/tiers/{id}"})
    public ResponseEntity<TiersModel>getTierById(@NotNull @PathVariable("id") long id){
        return ResponseEntity.ok(tierApiService.getById(id));
    };

    @RequestMapping(value="/tiers",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<TiersModel> insertTier(@RequestBody TiersModel tiersModel){
        return ResponseEntity.ok(tierApiService.insertTier(tiersModel));
    }

    @RequestMapping(value="/tiers/{id}",method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<TiersModel> updateTierById(@NotNull @PathVariable("id") long id,@RequestBody TiersModel tiersModel){
        return ResponseEntity.ok(tierApiService.updateTierById(id,tiersModel));
    }

}