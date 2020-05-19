package com.food.table.controller;

import com.food.table.model.DietsModel;
import com.food.table.service.DietsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class DietsController {
	
	@Autowired
	DietsService dietsService;
	
	@GetMapping("/diets")
	public ResponseEntity<List<DietsModel>> getDiets() {
		List<DietsModel> dietsResponse=dietsService.getDiets();
		return ResponseEntity.ok(dietsResponse);
	}
	
	@PostMapping("/diets")
	@PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
	public ResponseEntity<DietsModel> addNewDiets(@RequestBody DietsModel dietPutRequest) {
		DietsModel dietsResponse=dietsService.addNewDiets(dietPutRequest);
		return ResponseEntity.ok(dietsResponse);
	}
	
	@GetMapping("/diets/{dietId}")
	public ResponseEntity<DietsModel> getDietById(@NotNull @PathVariable("dietId") int dietId) {
		DietsModel dietsResponse=dietsService.getDietById(dietId);
		return ResponseEntity.ok(dietsResponse);
	}
	
	@PutMapping("/diets/{dietId}")
	@PreAuthorize("hasAnyAuthority('RESTAURANT_OWNER','RESTAURANT_MANAGER','ADMIN')")
	public ResponseEntity<DietsModel> updateDietById(@NotNull @PathVariable("dietId") int dietId,
			@RequestBody DietsModel dietPutRequest) {
		DietsModel dietsResponse = dietsService.updateDietById(dietId, dietPutRequest);
		return ResponseEntity.ok(dietsResponse);
	}
}
