package com.food.table.serviceimpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.food.table.dto.Extras;
import com.food.table.dto.Foods;
import com.food.table.dto.Restaurant;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.repo.ExtrasRepository;
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

	@Autowired
	private ExtrasRepository extrasRepository;

	@Value("${s3.bucket.restaurantName}")
	private String parentBucketName;

	@Override
	public void uploadFile(MultipartFile uploadFile, Integer restaurantId, Integer foodId, Integer extrasId) {
		Optional<Restaurant> restaurantOp = restaurantRepository.findById(restaurantId);
		if (restaurantOp.isPresent()) {
			Restaurant restaurant = restaurantOp.get();
			String bucketName = parentBucketName + restaurant.getRestaurantName() + "_" + restaurantId;
			try {
				File file = convertMultiPartFileToFile(uploadFile);
				if (foodId == null && extrasId == null) {
					if (restaurant.getImageUrl() != null && !restaurant.getImageUrl().trim().isEmpty()) {
						deleteFileFromS3Bucket(restaurant.getImageUrl(), restaurantId);
					}
					String imageUrl = uploadFileToS3Bucket(bucketName, file);
					restaurant.setImageUrl(imageUrl);
					restaurantRepository.save(restaurant);
				} else if (foodId != null && extrasId == null) {
					Optional<Foods> foodsOP = foodRepository.findById(foodId);
					if (foodsOP.isPresent()) {
						Foods foods = foodsOP.get();
						if (foods.getImageUrl() != null && !foods.getImageUrl().trim().isEmpty()) {
							deleteFileFromS3Bucket(foods.getImageUrl(), restaurantId);
						}
						String imageUrl = uploadFileToS3Bucket(bucketName, file);
						foods.setImageUrl(imageUrl);
						foodRepository.save(foods);
					} else {
						throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_FOOD_ID);
					}
				} else if (extrasId != null) {
					Optional<Extras> extrasOp = extrasRepository.findById(Long.valueOf(extrasId));
					if (extrasOp.isPresent()) {
						Extras extra = extrasOp.get();
						if (extra.getImageUrl() != null && !extra.getImageUrl().trim().isEmpty()) {
							deleteFileFromS3Bucket(extra.getImageUrl(), restaurantId);
						}
						String imageUrl = uploadFileToS3Bucket(bucketName, file);
						extra.setImageUrl(imageUrl);
						extrasRepository.save(extra);
					} else {
						throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_EXTRAS_ID);
					}

				}
				file.delete();
			} catch (AmazonServiceException ex) {
				log.error("Error while uploading file for restaurant : " + restaurantId, ex.getMessage());
				throw new ApplicationException(ex, HttpStatus.INTERNAL_SERVER_ERROR,
						ApplicationErrors.UPLOAD_FILE_FAILED);
			}
		} else {
			throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_RESTAURANT_ID);
		}

	}

	private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
		String currentTime = String.valueOf(new Date().getTime());
		String fileName = FilenameUtils.getBaseName(multipartFile.getOriginalFilename()).concat(currentTime) + "."
				+ FilenameUtils.getExtension(multipartFile.getOriginalFilename());
		final File file = new File(fileName);
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

	private void deleteFileFromS3Bucket(String s3URIRequest, int restaurantId) {
		AmazonS3URI s3Uri = new AmazonS3URI(s3URIRequest);
		try {
			amazonS3.deleteObject(s3Uri.getBucket(), s3Uri.getKey());
		} catch (AmazonServiceException ex) {
			log.error("Error while uploading file for restaurant : " + restaurantId, ex.getMessage());
			throw new ApplicationException(ex, HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.UPLOAD_FILE_FAILED);
		}
	}

}
