package com.hsbc.hsdc.javacomm.wechat.message.received;

import com.hsbc.hsdc.javacomm.wechat.message.ReceivedMessage;

public class TextMessage extends ReceivedMessage {

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
