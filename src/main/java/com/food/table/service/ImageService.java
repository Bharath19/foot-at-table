package com.food.table.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {

	public void uploadFile(MultipartFile uploadFile, int restaurantId, int foodId);
}
