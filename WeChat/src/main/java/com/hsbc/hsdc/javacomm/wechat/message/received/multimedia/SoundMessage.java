package com.hsbc.hsdc.javacomm.wechat.message.received.multimedia;

import com.hsbc.hsdc.javacomm.wechat.message.received.MultimediaMessage;

public class SoundMessage extends MultimediaMessage {

	private String format;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
