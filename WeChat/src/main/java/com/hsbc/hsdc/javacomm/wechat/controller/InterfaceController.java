package com.hsbc.hsdc.javacomm.wechat.controller;

import java.io.InputStream;
import java.util.Iterator;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hsbc.hsdc.javacomm.wechat.dispatcher.AbstractDispatcher;
import com.hsbc.hsdc.javacomm.wechat.dispatcher.factory.AbstractDispatcherFactory;
import com.hsbc.hsdc.javacomm.wechat.exception.NoResultException;
import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.received.LinkMessage;
import com.hsbc.hsdc.javacomm.wechat.message.received.LocationMessage;
import com.hsbc.hsdc.javacomm.wechat.message.received.TextMessage;
import com.hsbc.hsdc.javacomm.wechat.message.received.multimedia.ImageMessage;
import com.hsbc.hsdc.javacomm.wechat.message.received.multimedia.VideoMessage;
import com.hsbc.hsdc.javacomm.wechat.message.received.multimedia.VoiceMessage;

@Controller
@RequestMapping("/Interface")
public class InterfaceController {

	private final static Logger logger = Logger.getLogger(InterfaceController.class);
	
	private AbstractDispatcherFactory<AbstractDispatcher> dispatcherFactory = null;

	@Resource(name = "xmlDispatcherFactory")
	public void setDispatcherFactory(AbstractDispatcherFactory<AbstractDispatcher> dispatcherFactory) {
		this.dispatcherFactory = dispatcherFactory;
	}

	@RequestMapping(value = "/Process", params = { "echostr" }, method = RequestMethod.GET)
	@ResponseBody
	public String authenticate(@RequestParam("echostr") String echostr) {
		logger.info("Authenticate successfully - connect to the wechat server.");
		return echostr;
	}

	@RequestMapping(value = "/Process", params = { "!echostr" }, method = RequestMethod.POST)
	@ResponseBody
	public SentMessage receiveMessage(@RequestBody String data) {
		SentMessage message = null;
		
		//create specific dispatcher through the data and specific factory.
		AbstractDispatcher dispatcher = dispatcherFactory.createInstance(data);
		//construct the message object through specific dispatcher.
		Object receivedMessage = dispatcher.construct(data);
		
		if(receivedMessage != null) {
			//dispatch the message to specific dispatcher and return the response message.
			Object returnObject = dispatcher.dispatch(receivedMessage);
			if(returnObject != null && returnObject instanceof SentMessage) {
				message = (SentMessage)returnObject;
			} else {
				logger.warn("Return empty message to the client.");
			}
		}

		return message;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ NoResultException.class })
	public void handle() {
		// TODO
	}

	protected SentMessage explain(InputStream inputStream) {
		// TODO - 解析 input - xml
		SentMessage sentMessage = null;
		SAXReader reader = new SAXReader();

		try {
			Document document = reader.read(inputStream);
			Element xml = document.getRootElement();

			// print the tag and its value.
			Iterator<Element> rootIter = xml.elementIterator();
			while (rootIter.hasNext()) {
				Element element = rootIter.next();
				System.out.println(element.getName() + ":" + element.getTextTrim());
			}

			// depending on the message type to response.
			String msgType = xml.elementTextTrim("MsgType");

			switch (msgType) {
			default:
				sentMessage = null;
				break;

			case "text":
				sentMessage = receiveText(xml);
				break;

			case "image":
				sentMessage = receiveImage(xml);
				break;

			case "voice":
				sentMessage = receiveVoice(xml);
				break;

			case "video":
				sentMessage = receiveVideo(xml);
				break;

			case "location":
				sentMessage = receiveLocation(xml);
				break;

			case "link":
				sentMessage = receiveLink(xml);
				break;
			}
		} catch (DocumentException e) {
			logger.error("Reading inputStream error.", e);
		}

		return sentMessage;
	}

	private SentMessage receiveLink(Element element) {
		LinkMessage message = new LinkMessage();
		message.setMsgId(Long.parseLong(element.elementTextTrim("MsgId")));
		message.setToUserName(element.elementTextTrim("ToUserName"));
		message.setFromUserName(element.elementTextTrim("FromUserName"));
		message.setCreateTime(Long.parseLong(element.elementTextTrim("CreateTime")));
		message.setMsgType("link");

		message.setTitle(element.elementTextTrim("Title"));
		message.setDescription(element.elementTextTrim("Description"));
		message.setUrl(element.elementTextTrim("Url"));

		return null;
	}

	private SentMessage receiveLocation(Element element) {
		LocationMessage message = new LocationMessage();
		message.setMsgId(Long.parseLong(element.elementTextTrim("MsgId")));
		message.setToUserName(element.elementTextTrim("ToUserName"));
		message.setFromUserName(element.elementTextTrim("FromUserName"));
		message.setCreateTime(Long.parseLong(element.elementTextTrim("CreateTime")));
		message.setMsgType("location");

		message.setLocationX(Double.parseDouble(element.elementTextTrim("Location_X")));
		message.setLocationY(Double.parseDouble(element.elementTextTrim("Location_Y")));
		message.setScale(Double.parseDouble(element.elementTextTrim("Scale")));
		message.setLabel(element.elementTextTrim("Label"));

		return null;
	}

	private SentMessage receiveVideo(Element element) {
		VideoMessage message = new VideoMessage();
		message.setMsgId(Long.parseLong(element.elementTextTrim("MsgId")));
		message.setToUserName(element.elementTextTrim("ToUserName"));
		message.setFromUserName(element.elementTextTrim("FromUserName"));
		message.setCreateTime(Long.parseLong(element.elementTextTrim("CreateTime")));
		message.setMsgType("video");

		message.setMediaId(element.elementTextTrim("MediaId"));
		message.setThumbMediaId(element.elementTextTrim("ThumbMediaId"));

		return null;
	}

	private SentMessage receiveVoice(Element element) {
		VoiceMessage message = new VoiceMessage();
		message.setMsgId(Long.parseLong(element.elementTextTrim("MsgId")));
		message.setToUserName(element.elementTextTrim("ToUserName"));
		message.setFromUserName(element.elementTextTrim("FromUserName"));
		message.setCreateTime(Long.parseLong(element.elementTextTrim("CreateTime")));
		message.setMsgType("voice");

		message.setMediaId(element.elementTextTrim("MediaId"));
		message.setFormat(element.elementTextTrim("Format"));

		return null;
	}

	private SentMessage receiveImage(Element element) {
		ImageMessage message = new ImageMessage();
		message.setMsgId(Long.parseLong(element.elementTextTrim("MsgId")));
		message.setToUserName(element.elementTextTrim("ToUserName"));
		message.setFromUserName(element.elementTextTrim("FromUserName"));
		message.setCreateTime(Long.parseLong(element.elementTextTrim("CreateTime")));
		message.setMsgType("image");

		message.setMediaId(element.elementTextTrim("MediaId"));
		message.setPicUrl(element.elementTextTrim("PicUrl"));

		return null;
	}

	private SentMessage receiveText(Element element) {
		TextMessage message = new TextMessage();
		message.setMsgId(Long.parseLong(element.elementTextTrim("MsgId")));
		message.setToUserName(element.elementTextTrim("ToUserName"));
		message.setFromUserName(element.elementTextTrim("FromUserName"));
		message.setCreateTime(Long.parseLong(element.elementTextTrim("CreateTime")));
		message.setMsgType("text");

		message.setContent(element.elementTextTrim("Content"));

		return null;
	}

}
