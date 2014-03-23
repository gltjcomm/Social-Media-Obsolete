package com.hsbc.hsdc.javacomm.wechat.message.sent;

import javax.xml.bind.annotation.XmlRootElement;

import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.sent.item.MusicItem;

@XmlRootElement(name = "xml")
public class MusicSentMessage extends SentMessage {

	private MusicItem music;

	public MusicItem getMusic() {
		return music;
	}

	public void setMusic(MusicItem music) {
		this.music = music;
	}

}
