package com.food.table.controller;

import com.food.table.model.SearchTagModel;
import com.food.table.service.SearchTagApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SearchTagApiController {

    final SearchTagApiService searchTagApiService;

    @RequestMapping(value="/searchtag",method=RequestMethod.GET)
    public ResponseEntity<List<SearchTagModel>> getAllTiers(){
        return ResponseEntity.ok(searchTagApiService.getAll());
    }

    @RequestMapping({"/searchtag/{id}"})
    public ResponseEntity<SearchTagModel>getTierById(@NotNull @PathVariable("id") int id){
        return ResponseEntity.ok(searchTagApiService.getById(id));
    }

    @RequestMapping(value="/searchtag",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchTagModel> insertTier(@RequestBody SearchTagModel searchTagModel){
        return ResponseEntity.ok(searchTagApiService.insertSearchTag(searchTagModel));
    }

    @RequestMapping(value="/searchtag/{id}",method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchTagModel> updateTierById(@NotNull @PathVariable("id") int id, @RequestBody SearchTagModel searchTagModel){
        return ResponseEntity.ok(searchTagApiService.updateSearchTagById(id,searchTagModel));
    }
}
