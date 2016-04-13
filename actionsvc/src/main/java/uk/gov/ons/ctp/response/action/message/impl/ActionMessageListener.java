package uk.gov.ons.ctp.response.action.message.impl;

import javax.jms.Message;
import javax.jms.MessageListener;

public class ActionMessageListener implements MessageListener{

	private String payload = "no payload";
	
	@Override
	public void onMessage(Message arg0) {
		payload  = "message found";
		System.out.println("Message processed by ActionMessageListener");
	}

	public String getPayload() {
		return payload;
	}
}
