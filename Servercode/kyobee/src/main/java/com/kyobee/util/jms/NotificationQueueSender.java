package com.kyobee.util.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.kyobee.entity.GuestNotificationBean;

@Component
public class NotificationQueueSender {

	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendMessage(final GuestNotificationBean guestNotificationBean) {

		jmsTemplate.send(new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage objectMessage = session.createObjectMessage(guestNotificationBean);
				return objectMessage;
			}
		});
	}

}
