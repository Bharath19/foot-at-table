package com.food.table.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.food.table.model.DietsModel;

@Service
public interface DietsService {
	
	List<DietsModel> getDiets();
	
	DietsModel addNewDiets(DietsModel diet);
	
	DietsModel getDietById(int dietId);
	
	DietsModel updateDietById(int dietId,DietsModel dietRequest);

}
