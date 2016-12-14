package com.rsnt.util.pusher;

import javapns.notification.PushNotificationPayload;

import com.rsnt.common.exception.RsntException;

public interface IPusher {

	//public void sendPushMessage(String deviceId, PushNotificationPayload payload) throws RsntException;
	public void sendPushMessage(String payloadType, String deviceId, String message, String optionID, String messageString) throws RsntException;
}
