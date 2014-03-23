package com.hsbc.hsdc.javacomm.wechat.message.sent;

import javax.xml.bind.annotation.XmlRootElement;

import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;

@XmlRootElement(name = "xml")
public class TextSentMessage extends SentMessage {

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
