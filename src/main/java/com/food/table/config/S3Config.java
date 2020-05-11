package com.food.table.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

@Configuration
public class S3Config {

	@Value("${s3.access.name}")
	String accessKey;

	@Value("${s3.access.secret}")
	String accessSecret;

	@Value("${s3.bucket.region}")
	private String region;

	@Bean
	public AmazonS3 getAmazonS3Cient() {
		final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, accessSecret);
		return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).withPathStyleAccessEnabled(true)
				.build();
	}
	
	@Bean
	public AmazonSNS getAmazonSNSClient() {
		final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, accessSecret);
		return AmazonSNSClientBuilder.standard().withRegion(Regions.fromName(region))
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
				.build();
	}
}
