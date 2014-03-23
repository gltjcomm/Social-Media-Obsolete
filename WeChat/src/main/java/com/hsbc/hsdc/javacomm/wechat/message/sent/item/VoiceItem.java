package com.hsbc.hsdc.javacomm.wechat.message.sent.item;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "voice")
public class VoiceItem {

	private long mediaId;

	public long getMediaId() {
		return mediaId;
	}

	public void setMediaId(long mediaId) {
		this.mediaId = mediaId;
	}

}
