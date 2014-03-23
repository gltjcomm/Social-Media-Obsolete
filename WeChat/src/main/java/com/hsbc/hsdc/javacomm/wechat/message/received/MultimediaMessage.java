package com.hsbc.hsdc.javacomm.wechat.message.received;

import com.hsbc.hsdc.javacomm.wechat.message.ReceivedMessage;

public class MultimediaMessage extends ReceivedMessage {

	private long mediaId;

	public long getMediaId() {
		return mediaId;
	}

	public void setMediaId(long mediaId) {
		this.mediaId = mediaId;
	}

}
