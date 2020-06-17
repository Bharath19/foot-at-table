package com.food.table.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.food.table.dto.Diets;
import com.food.table.model.DietsModel;
import com.food.table.repo.DietRepository;
import com.food.table.service.DietsService;

@Service
public class DietsServiceImpl implements DietsService {
	
	
	@Autowired
	DietRepository dietRepository;
	
	@Override
	@Cacheable(cacheNames = "getAllDiets")
	public List<DietsModel> getDiets() {
		List<DietsModel> dietsResponse = new ArrayList<DietsModel>();
		List<Diets> diets = dietRepository.findAll();
		diets.forEach(data -> {
			DietsModel diet = DietsModel.builder().id(data.getId()).name(data.getName()).build();
			dietsResponse.add(diet);
		});

		return dietsResponse;
	}
	
	@Override
	@Caching(evict = {
			@CacheEvict( cacheNames = "defaultTableValues" , allEntries = true),
			@CacheEvict( cacheNames = "getAllDiets" , allEntries = true)
	})	
	public DietsModel addNewDiets(DietsModel dietResponse) {
		Diets diet=Diets.builder().name(dietResponse.getName()).description(dietResponse.getDescription()).build();
		Diets dietInsert=dietRepository.save(diet);
		dietResponse.setId(dietInsert.getId());
		return dietResponse;
	}
	
	@Override
	@Cacheable(cacheNames = "getDietById" , key ="#dietId")
	public DietsModel getDietById(int dietId) {
		Optional<Diets> diet = dietRepository.findById(dietId);
		DietsModel dietResponse = DietsModel.builder().id(diet.get().getId()).name(diet.get().getName())
				.description(diet.get().getDescription()).build();
		return dietResponse;
	}
	
	@Override
	@Caching(evict = {
			@CacheEvict( cacheNames = "defaultTableValues" , allEntries = true),
			@CacheEvict( cacheNames = "getDietById" , allEntries = true),
			@CacheEvict( cacheNames = "getDiets" , allEntries = true)
	})
	public DietsModel updateDietById(int dietId,DietsModel dietRequest) {
		Optional<Diets> diet = dietRepository.findById(dietId);
		diet.get().setName(dietRequest.getName());
		Diets dietUpdate=dietRepository.save(diet.get());
		DietsModel dietResponse = DietsModel.builder().id(dietUpdate.getId()).name(dietUpdate.getName())
				.description(dietUpdate.getDescription()).build();
		return dietResponse;
	}
}
