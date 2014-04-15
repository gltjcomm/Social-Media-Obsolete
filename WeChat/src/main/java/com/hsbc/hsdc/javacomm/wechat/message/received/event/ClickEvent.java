package com.hsbc.hsdc.javacomm.wechat.message.received.event;

import com.hsbc.hsdc.javacomm.wechat.message.received.EventMessage;

public class ClickEvent extends EventMessage {

	private String enentKey;

	public String getEnentKey() {
		return enentKey;
	}

	public void setEnentKey(String enentKey) {
		this.enentKey = enentKey;
	}

}
