package com.hsbc.hsdc.javacomm.wechat.message.received;

import com.hsbc.hsdc.javacomm.wechat.message.ReceivedMessage;

public class LinkMessage extends ReceivedMessage {

	private long msgId;
	private String title;
	private String description;
	private String url;

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
