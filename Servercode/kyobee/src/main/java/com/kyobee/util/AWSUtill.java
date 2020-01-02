package com.kyobee.util;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.kyobee.util.common.Constants;

public class AWSUtill {
	private static AmazonSNS AMAZON_SNS_CLIENT;
	/*private static Map<String, MessageAttributeValue> SMS_ATTRIBUTE;*/
	
	@SuppressWarnings("deprecation")
	public static void AMAZONCLIENT() {
		AWSCredentials credentials = new BasicAWSCredentials(AppInitializer.AWS_ACCESS_KEY,
				AppInitializer.AWS_SECRET_KEY);
		
		//AmazonSNSClient snsClient = new AmazonSNSClient(credentials);
		//BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials(AccessKey,SecretAccessKey);
		AmazonSNS snsClient = AmazonSNSClient
							  .builder()	
							  .withRegion("us-west-2")
							  .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

		/*SMS_ATTRIBUTE = new HashMap<String, MessageAttributeValue>();

		SMS_ATTRIBUTE.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue("LOANDESK") // The senderID shown on the device.
				.withDataType("String"));
		SMS_ATTRIBUTE.put("AWS.SNS.SMS.SMSType",
				new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));*/

		AMAZON_SNS_CLIENT = snsClient;
	}
	
	public void sendSMSUsingSNS(){
		
		final String msg = "If you receive this message, publishing a message to an Amazon SNS topic works.";
		final PublishRequest publishRequest = new PublishRequest("topicarn", msg);
		final PublishResult publishResponse = AMAZON_SNS_CLIENT.publish(publishRequest);

		// Print the MessageId of the message.
		System.out.println("MessageId: " + publishResponse.getMessageId());
	}


}
