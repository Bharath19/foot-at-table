package com.food.table.serviceimpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.food.table.dto.Foods;
import com.food.table.dto.Restaurant;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.repo.FoodRepository;
import com.food.table.repo.RestaurantRepository;
import com.food.table.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

	@Autowired
	private AmazonS3 amazonS3;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private FoodRepository foodRepository;

	@Value("${s3.bucket.restaurantName}")
	private String parentBucketName;

	@Override
	public void uploadFile(MultipartFile uploadFile, int restaurantId, int foodId) {
		Restaurant restaurant = restaurantRepository.getOne(restaurantId);
		String bucketName = parentBucketName + restaurant.getRestaurantName() + "_" + restaurantId;
		try {
			File file = convertMultiPartFileToFile(uploadFile);
			String imageUrl = uploadFileToS3Bucket(bucketName, file);
			if (foodId == 0) {
				restaurant.setImageUrl(imageUrl);
				restaurantRepository.save(restaurant);
			} else {
				Foods foods = foodRepository.getOne(foodId);
				foods.setImageUrl(imageUrl);
				foodRepository.save(foods);
			}
			file.delete();
		} catch (AmazonServiceException ex) {
			log.error("Error while uploading file for restaurant : " + restaurantId, ex.getMessage());
			throw new ApplicationException(ex, HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.UPLOAD_FILE_FAILED);
		}

	}

	private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
		final File file = new File(multipartFile.getOriginalFilename());
		try (final FileOutputStream outputStream = new FileOutputStream(file)) {
			outputStream.write(multipartFile.getBytes());
		} catch (final IOException ex) {
			log.error("Error converting the multi-part file to file :" + multipartFile.getName(), ex.getMessage());
			throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.FILE_CONVERSION_FAILED);
		}
		return file;
	}

	private String uploadFileToS3Bucket(final String bucketName, final File file) {
		final String uniqueFileName = file.getName();
		log.info("Uploading file with name : " + uniqueFileName);
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
		amazonS3.putObject(putObjectRequest);
		URL imageUrl = amazonS3.getUrl(bucketName, file.getName());
		log.info("Uploading file with name is completed : " + uniqueFileName);
		return imageUrl.toString();
	}

}
