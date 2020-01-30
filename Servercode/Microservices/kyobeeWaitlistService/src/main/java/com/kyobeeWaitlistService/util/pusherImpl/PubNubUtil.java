package com.kyobeeWaitlistService.util.pusherImpl;

import com.kyobeeWaitlistService.util.LoggerUtil;
import com.kyobeeWaitlistService.util.WaitListServiceConstants;
import com.kyobeeWaitlistService.util.pusher.Pusher;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

public class PubNubUtil implements Pusher {

	private static PubNub pubnub;

	public PubNubUtil() {

		PNConfiguration pnConfiguration = new PNConfiguration()
				.setSubscribeKey(WaitListServiceConstants.PUBNUB_SUBSCRIBE_KEY)
				.setPublishKey(WaitListServiceConstants.PUBNUB_PUBLISH_KEY)
				.setSecretKey(WaitListServiceConstants.PUBNUB_SECRET_KEY).setSecure(true);
		pubnub = new PubNub(pnConfiguration);
	}

	@Override
	public void publish(Object message, String channelName) {
		try {
			pubnub.publish().message(message).channel(channelName).async(new PNCallback<PNPublishResult>() {
				@Override
				public void onResponse(PNPublishResult result, PNStatus status) {
					// handle publish result, status always present, result if successful
					// status.isError to see if error happened
					LoggerUtil.logInfo("Message send");
				}
			});
		} catch (Exception ex) {
			LoggerUtil.logError(ex);
		}

	}

	@Override
	public void subscribe() {
//use this method for subscribe
	}

}
