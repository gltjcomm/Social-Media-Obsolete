package com.hsbc.hsdc.javacomm.wechat.message.received;

import com.hsbc.hsdc.javacomm.wechat.message.ReceivedMessage;

public class EventMessage extends ReceivedMessage {
	
	private String event;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

}
