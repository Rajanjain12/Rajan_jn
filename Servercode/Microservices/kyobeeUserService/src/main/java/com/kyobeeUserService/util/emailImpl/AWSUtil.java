package com.kyobeeUserService.util.emailImpl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.kyobeeUserService.util.LoggerUtil;
import com.kyobeeUserService.util.UserServiceConstants;
import com.kyobeeUserService.util.email.Email;

public class AWSUtil implements Email {

	@Override
	public void sendEmail(String toEmail, String subject, String body) {
		try {

			AWSCredentials credentials = new BasicAWSCredentials(UserServiceConstants.AWS_ACCESS_KEY_ID,
					UserServiceConstants.AWS_SECRET_KEY);

			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
					.withRegion(Regions.US_WEST_2).withCredentials(new AWSStaticCredentialsProvider(credentials))
					.build();
			SendEmailRequest request = new SendEmailRequest()
					.withDestination(new Destination().withToAddresses(toEmail))
					.withMessage(new Message()
							.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(body)))
							.withSubject(new Content().withCharset("UTF-8").withData(subject)))
					.withSource(UserServiceConstants.EMAIL_FROM);
			client.sendEmail(request);
			LoggerUtil.logInfo("Email sent successfully!");

		} catch (Exception e) {
			LoggerUtil.logError(e);
		}

	}

}
