package com.kyobeeUtilService.util.messageImpl;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.GetSMSAttributesRequest;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SetSMSAttributesRequest;
import com.kyobeeUtilService.util.message.MessageNotification;
import com.kyobeeUtilService.util.LoggerUtil;
import com.kyobeeUtilService.util.UtilServiceConstants;;

public class AWSUtil implements MessageNotification{
	
	@Override
	public void sendMessage(String contactNo, String message) {
		
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();
		smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
		        .withStringValue(UtilServiceConstants.SNS_DEFAULT_SENDER_ID) //The sender ID shown on the device.
		        .withDataType("String"));		
		smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
		        .withStringValue(UtilServiceConstants.SNS_DEFAULT_SMS_TYPE)
		        .withDataType("String"));
		
		AWSCredentials awsCredentials=new BasicAWSCredentials(UtilServiceConstants.AWS_ACCESS_KEY_ID, UtilServiceConstants.AWS_SECRET_KEY);
		try {
			
			AmazonSNS snsClient = AmazonSNSClient.builder().withRegion(UtilServiceConstants.AWS_REGION).withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
			
			LoggerUtil.logInfo("My Default SMS attributes:");
			
			SetSMSAttributesRequest setRequest = new SetSMSAttributesRequest()
					.addAttributesEntry("DefaultSenderID", UtilServiceConstants.SNS_DEFAULT_SENDER_ID)
					.addAttributesEntry("MonthlySpendLimit", UtilServiceConstants.SNS_MONTHLY_SPEND_LIMIT)
					.addAttributesEntry("DeliveryStatusIAMRole", UtilServiceConstants.SNS_DELIVERY_STATUS_IAM_ROLE)
					.addAttributesEntry("DeliveryStatusSuccessSamplingRate", UtilServiceConstants.SNS_DELIVERY_STATUS_SUCCESS_SAMPLING_RATE)
					.addAttributesEntry("DefaultSMSType", UtilServiceConstants.SNS_DEFAULT_SMS_TYPE)
					.addAttributesEntry("UsageReportS3Bucket", UtilServiceConstants.SNS_USAGE_REPORT_S3_BUCKET);
			snsClient.setSMSAttributes(setRequest);
			
			
			Map<String, String> myAttributes = snsClient.getSMSAttributes(new GetSMSAttributesRequest())
				.getAttributes();
			LoggerUtil.logInfo("My NEW  SMS attributes:");
			for (String key : myAttributes.keySet()) {
				System.out.println(key + " = " + myAttributes.get(key));
			}
					
			
			
		 PublishResult result = snsClient.publish(( new PublishRequest()
                 .withMessage(message))
                 .withPhoneNumber(UtilServiceConstants.PHONE_NO_PREFIX+contactNo.toString())
                 .withMessageAttributes(smsAttributes));
		 
		 LoggerUtil.logInfo("sms sent successfully to phone no: "+contactNo+" result:"+result);
		 
		}catch(Exception e) {
			LoggerUtil.logError(e.getMessage(),e);
		}
		
	}

}
