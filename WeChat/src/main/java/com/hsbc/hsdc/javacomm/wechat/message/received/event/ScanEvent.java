package com.hsbc.hsdc.javacomm.wechat.message.received.event;

import com.hsbc.hsdc.javacomm.wechat.message.received.EventMessage;

public class ScanEvent extends EventMessage {

	private String eventKey;
	private String ticket;

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

}
