package com.food.table.service;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface SetupService {

	Map<String, Integer> getGstValues();

}
