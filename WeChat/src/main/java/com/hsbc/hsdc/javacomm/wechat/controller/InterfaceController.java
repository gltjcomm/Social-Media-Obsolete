package com.hsbc.hsdc.javacomm.wechat.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hsbc.hsdc.javacomm.wechat.dispatcher.AbstractDispatcher;
import com.hsbc.hsdc.javacomm.wechat.dispatcher.factory.AbstractDispatcherFactory;
import com.hsbc.hsdc.javacomm.wechat.message.ReceivedMessage;
import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.sent.TextSentMessage;

@Controller
@RequestMapping("/Interface")
public class InterfaceController {

	private final static Logger logger = Logger.getLogger(InterfaceController.class);
	
	private AbstractDispatcherFactory<AbstractDispatcher<ReceivedMessage, SentMessage>> dispatcherFactory = null;

	@Resource(name = "xmlDispatcherFactory")
	public void setDispatcherFactory(AbstractDispatcherFactory<AbstractDispatcher<ReceivedMessage, SentMessage>> dispatcherFactory) {
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
		AbstractDispatcher<ReceivedMessage, SentMessage> dispatcher = dispatcherFactory.createInstance(data);
		
		if(dispatcher != null) {
			//construct the message object through specific dispatcher.
			ReceivedMessage receivedMessage = dispatcher.construct(data);
			if(receivedMessage != null) {
				//dispatch the message to specific dispatcher and return the response message.
				message = dispatcher.dispatch(receivedMessage);
			}
		} else {
			logger.warn("No suitable dispatcher found for the data : " + data);
		}
		
		return message;
	}
	
	@RequestMapping(value = "/json", method = RequestMethod.POST)
	@ResponseBody
	public SentMessage jsonMessage(@RequestBody String data) {
		System.out.println("data : " + data);
		
		TextSentMessage message = new TextSentMessage();
		
		message.setFromUserName("fromUserName");
		message.setToUserName("toUserName");
		message.setCreateTime(new Date().getTime());
		message.setMsgType("text");
		
		message.setContent("content");
		
		return message;
	}

}
