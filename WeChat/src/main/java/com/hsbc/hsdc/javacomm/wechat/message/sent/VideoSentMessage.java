package com.hsbc.hsdc.javacomm.wechat.message.sent;

import javax.xml.bind.annotation.XmlRootElement;

import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.sent.item.VideoItem;

@XmlRootElement(name = "xml")
public class VideoSentMessage extends SentMessage {

	private VideoItem video;

	public VideoItem getVideo() {
		return video;
	}

	public void setVideo(VideoItem video) {
		this.video = video;
	}

}
