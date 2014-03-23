package com.hsbc.hsdc.javacomm.wechat.message.sent;

import javax.xml.bind.annotation.XmlRootElement;

import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.sent.item.ImageItem;

@XmlRootElement(name = "xml")
public class ImageSentMessage extends SentMessage {

	private ImageItem image;

	public ImageItem getImage() {
		return image;
	}

	public void setImage(ImageItem image) {
		this.image = image;
	}

}
