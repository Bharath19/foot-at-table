package com.food.table.controller;

import com.food.table.model.RestaurantTableDetailsModel;
import com.food.table.model.RestaurantTableModel;
import com.food.table.service.RestaurantTableService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestaurantTableController {

    final RestaurantTableService restaurantTableService;


    @ApiOperation(value = "Insert new restaurant table", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/table", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<RestaurantTableModel> insertRestaurantTable(@RequestBody RestaurantTableModel restaurantTableModel) {
        return ResponseEntity.ok(restaurantTableService.insertTable(restaurantTableModel));
    }

    @ApiOperation(value = "Get restaurant table by Id", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/table/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<RestaurantTableModel> getRestaurantTableById(@NotNull @PathVariable int id) {
        return ResponseEntity.ok(restaurantTableService.getById(id));
    }

    @ApiOperation(value = "View a list of restaurant table by restaurant id", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/table/restaurant/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<List<RestaurantTableModel>> getAllTableByRestaurantId(@NotNull @PathVariable int id , @RequestParam(defaultValue = StringUtils.EMPTY, required = false) String tableName) {
        return ResponseEntity.ok(restaurantTableService.getAllByRestaurantId(id,tableName));
    }

    @ApiOperation(value = "View a list of all restaurant table", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/table", method = RequestMethod.GET)
    public ResponseEntity<List<RestaurantTableModel>> getAllTable() {
        return ResponseEntity.ok(restaurantTableService.getAllTable());
    }

    @ApiOperation(value = "Delete a restaurant table", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/table/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public void deleteTableById(@NotNull @PathVariable int id) {
        restaurantTableService.deleteById(id);
    }

    @ApiOperation(value = "Update a restaurant table", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/table/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<RestaurantTableModel> updateTableById(@NotNull @PathVariable int id, @RequestBody RestaurantTableModel restaurantTableModel) {
        return ResponseEntity.ok(restaurantTableService.updateById(id, restaurantTableModel));
    }

    @ApiOperation(value = "Get restaurant table QR code by Id", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/table/qrcode/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
    public ResponseEntity<byte[]> getTableQrCode(@NotNull @PathVariable int id) {
        return ResponseEntity.ok().body(restaurantTableService.generateQRCode(id));
    }

    @ApiOperation(value = "Get foods by QR Code", authorizations = {@Authorization(value = "accessToken")})
    @RequestMapping(value = "/table/qrcode/details/{qrCode}", method = RequestMethod.GET)
    public ResponseEntity<RestaurantTableDetailsModel> getFoodsByQRCode(@NotNull @PathVariable String qrCode) {
        return ResponseEntity.ok().body(restaurantTableService.getTableDetailsByQRCode(qrCode));
    }


}
