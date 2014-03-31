package com.hsbc.hsdc.javacomm.wechat.controller;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hsbc.hsdc.javacomm.wechat.exception.NoResultException;
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
	public SentMessage receiveMessage(InputStream input) {
		
		//TODO - 解析 input - xml
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(input);
			Element xml = document.getRootElement();
			Iterator<Element> rootIter = xml.elementIterator();
			
			while(rootIter.hasNext()) {
				Element element = rootIter.next();
				System.out.println(element.getName() + ":" + element.getTextTrim());
			}
		} catch (DocumentException e) {
			logger.error("Reading inputStream error.", e);
		}
		
		TextSentMessage sentMessage = new TextSentMessage();

		sentMessage.setToUserName("<![CDATA[toUser]]>");
		sentMessage.setFromUserName("<![CDATA[fromUser]]>");
		sentMessage.setCreateTime(new Date().getTime());
		sentMessage.setMsgType("<![CDATA[text]]>");
		sentMessage.setContent("<![CDATA[你好!]]>");

		return sentMessage;
	}
	
	@RequestMapping(value = "/Internal")
	@ResponseStatus(HttpStatus.OK)
	public void empty() {
		//TODO
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({NoResultException.class})
	public void handle() {
		//TODO
	}
	
}
