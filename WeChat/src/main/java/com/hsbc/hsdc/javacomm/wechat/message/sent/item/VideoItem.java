package com.hsbc.hsdc.javacomm.wechat.message.sent.item;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "video")
public class VideoItem {

	private long mediaId;
	private String title;
	private String description;

	public long getMediaId() {
		return mediaId;
	}

	public void setMediaId(long mediaId) {
		this.mediaId = mediaId;
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

}
