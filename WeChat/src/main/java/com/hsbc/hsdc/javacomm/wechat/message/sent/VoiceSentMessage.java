package com.hsbc.hsdc.javacomm.wechat.message.sent;

import javax.xml.bind.annotation.XmlRootElement;

import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.sent.item.VoiceItem;

@XmlRootElement(name = "xml")
public class VoiceSentMessage extends SentMessage {

	private VoiceItem voice;

	public VoiceItem getVoice() {
		return voice;
	}

	public void setVoice(VoiceItem voice) {
		this.voice = voice;
	}

}
