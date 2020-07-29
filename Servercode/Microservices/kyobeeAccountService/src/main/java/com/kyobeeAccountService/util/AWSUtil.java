package com.kyobeeAccountService.util;

import java.io.File;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class AWSUtil {
	
	public String uploadProfileImage(File file) {
		AWSCredentials credentials = new BasicAWSCredentials(AccountServiceConstants.AWS_ACCESS_KEY_ID,
				AccountServiceConstants.AWS_SECRET_KEY);
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
		s3client.putObject(new PutObjectRequest(AccountServiceConstants.AWS_LOGOS_BUCKET_NAME, file.getName(), file)
						.withCannedAcl(CannedAccessControlList.PublicRead));
		return AccountServiceConstants.AWS_PATH + AccountServiceConstants.AWS_LOGOS_BUCKET_NAME
				+ "/" +file.getName();
	}

}
