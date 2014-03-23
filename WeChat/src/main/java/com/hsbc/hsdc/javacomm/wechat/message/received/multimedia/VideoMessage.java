package com.hsbc.hsdc.javacomm.wechat.message.received.multimedia;

import com.hsbc.hsdc.javacomm.wechat.message.received.MultimediaMessage;

public class VideoMessage extends MultimediaMessage {

	private String thumbMediaId;

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

}
