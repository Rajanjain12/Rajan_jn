package com.kyobeeUserService.util;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class AWSUtil {

	public String uploadInvoice(InputStream is ,String  fileName) throws IOException {
		
		AWSCredentials credentials = new BasicAWSCredentials(UserServiceConstants.AWS_ACCESS_KEY_ID,
				UserServiceConstants.AWS_SECRET_KEY);
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(is.available());
		s3client.putObject(new PutObjectRequest(UserServiceConstants.AWS_BUCKET_NAME, fileName , is, meta)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		return UserServiceConstants.AWS_PATH + UserServiceConstants.AWS_BUCKET_NAME + "/" + fileName;

	}

}
