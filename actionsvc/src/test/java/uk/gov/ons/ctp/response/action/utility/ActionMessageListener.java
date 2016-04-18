package uk.gov.ons.ctp.response.action.utility;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.jms.listener.DefaultMessageListenerContainer;

public class ActionMessageListener implements MessageListener{

	private String payload = "payload default: ";
	
	@Override
	public void onMessage(Message arg0) {
		this.payload  = "message found";
		System.out.println("Message processed by ActionMessageListener: " + this.payload);
	}

	public String getPayload() {
		return payload;
	}
	
	public void setPayload(String payload){
		this.payload = payload;
	}
	
}
