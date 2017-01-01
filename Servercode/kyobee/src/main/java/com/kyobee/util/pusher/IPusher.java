package com.kyobee.util.pusher;

import com.kyobee.exception.RsntException;

public interface IPusher {

	//public void sendPushMessage(String deviceId, PushNotificationPayload payload) throws RsntException;
	public void sendPushMessage(String payloadType, String deviceId, String message, String optionID, String messageString) throws RsntException;
}
