package com.food.table.controller;

import com.food.table.model.RestaurantTableDetailsModel;
import com.food.table.model.RestaurantTableModel;
import com.food.table.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestaurantTableController {

    final RestaurantTableService restaurantTableService;

    @RequestMapping(value = "/table", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTableModel> insertRestaurantTable(@RequestBody RestaurantTableModel restaurantTableModel) {
        return ResponseEntity.ok(restaurantTableService.insertTable(restaurantTableModel));
    }

    @RequestMapping(value = "/table/{id}", method = RequestMethod.GET)
    public ResponseEntity<RestaurantTableModel> getRestaurantTableById(@NotNull @PathVariable int id) {
        return ResponseEntity.ok(restaurantTableService.getById(id));
    }

    @RequestMapping(value = "/table/restaurant/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<RestaurantTableModel>> getAllTableByRestaurantId(@NotNull @PathVariable int id) {
        return ResponseEntity.ok(restaurantTableService.getAllByRestaurantId(id));
    }

    @RequestMapping(value = "/table", method = RequestMethod.GET)
    public ResponseEntity<List<RestaurantTableModel>> getAllTable() {
        return ResponseEntity.ok(restaurantTableService.getAllTable());
    }

    @RequestMapping(value = "/table/{id}", method = RequestMethod.DELETE)
    public void deleteTableById(@NotNull @PathVariable int id) {
        restaurantTableService.deleteById(id);
    }

    @RequestMapping(value = "/table/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTableModel> updateTableById(@NotNull @PathVariable int id, @RequestBody RestaurantTableModel restaurantTableModel) {
        return ResponseEntity.ok(restaurantTableService.updateById(id, restaurantTableModel));
    }

    @RequestMapping(value = "/table/qrcode/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getTableQrCode(@NotNull @PathVariable int id) {
        return ResponseEntity.ok().body(restaurantTableService.generateQRCode(id));
    }

    @RequestMapping(value = "/table/qrcode/details/{qrCode}", method = RequestMethod.GET)
    public ResponseEntity<RestaurantTableDetailsModel> getFoodsByQRCode(@NotNull @PathVariable String qrCode) {
        return ResponseEntity.ok().body(restaurantTableService.getTableDetailsByQRCode(qrCode));
    }


}
