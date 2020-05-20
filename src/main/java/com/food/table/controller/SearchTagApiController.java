package com.food.table.controller;

import com.food.table.model.SearchTagModel;
import com.food.table.service.SearchTagApiService;
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
public class SearchTagApiController {

    final SearchTagApiService searchTagApiService;

    @ApiOperation(value = "Get a list of all search tag", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value="/searchtag",method=RequestMethod.GET)
    public ResponseEntity<List<SearchTagModel>> getAllSearchTag() {
        return ResponseEntity.ok(searchTagApiService.getAll());
    }

    @ApiOperation(value = "Get search tag by id", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping({"/searchtag/{id}"})
    public ResponseEntity<SearchTagModel> getSearchTagById(@NotNull @PathVariable("id") int id) {
        return ResponseEntity.ok(searchTagApiService.getById(id));
    }

    @ApiOperation(value = "Insert a new Search Tag", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value="/searchtag",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<SearchTagModel> insertSearchTag(@RequestBody SearchTagModel searchTagModel) {
        return ResponseEntity.ok(searchTagApiService.insertSearchTag(searchTagModel));
    }

    @ApiOperation(value = "Update search tag", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value="/searchtag/{id}",method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<SearchTagModel> updateSearchTagById(@NotNull @PathVariable("id") int id, @RequestBody SearchTagModel searchTagModel) {
        return ResponseEntity.ok(searchTagApiService.updateSearchTagById(id,searchTagModel));
    }
}
