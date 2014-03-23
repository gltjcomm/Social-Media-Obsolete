package com.hsbc.hsdc.javacomm.wechat.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.sent.TextSentMessage;

@Controller
@RequestMapping("/Interface")
public class InterfaceController {

	private final static Logger logger = Logger.getLogger(InterfaceController.class);

	@RequestMapping(value = "/Process", params = { "echostr" }, method = RequestMethod.GET)
	@ResponseBody
	public String authenticate(@RequestParam("echostr") String echostr) {
		logger.info("Authenticate successfully - connect to the wechat server.");
		return echostr;
	}

	@RequestMapping(value = "/Process", params = { "!echostr" }, method = RequestMethod.POST)
	@ResponseBody
	public SentMessage receiveMessage(@RequestBody String message, HttpServletRequest request) {
		//TODO
		System.out.println("*********" + message);
		
		TextSentMessage sentMessage = new TextSentMessage();

		sentMessage.setToUserName("<![CDATA[toUser]]>");
		sentMessage.setFromUserName("<![CDATA[fromUser]]>");
		sentMessage.setCreateTime(new Date().getTime());
		sentMessage.setMsgType("<![CDATA[text]]>");
		sentMessage.setContent("<![CDATA[你好]]>");

		return sentMessage;
	}
	
}
