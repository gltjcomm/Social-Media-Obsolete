package com.hsbc.hsdc.javacomm.wechat.message.received.multimedia;

import com.hsbc.hsdc.javacomm.wechat.message.received.MultimediaMessage;

public class ImageMessage extends MultimediaMessage {

	private String picUrl;

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

}
