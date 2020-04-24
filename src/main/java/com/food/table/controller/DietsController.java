package com.food.table.controller;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.table.model.DietsModel;
import com.food.table.service.DietsService;

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
	public ResponseEntity<DietsModel> updateDietById(@NotNull @PathVariable("dietId") int dietId,
			@RequestBody DietsModel dietPutRequest) {
		DietsModel dietsResponse = dietsService.updateDietById(dietId, dietPutRequest);
		return ResponseEntity.ok(dietsResponse);
	}
}
