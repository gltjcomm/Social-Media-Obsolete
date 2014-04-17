package com.hsbc.hsdc.javacomm.wechat.controller;

import java.util.Date;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hsbc.hsdc.javacomm.wechat.client.WeChatClient;
import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.received.TextMessage;
import com.hsbc.hsdc.javacomm.wechat.message.sent.TextSentMessage;

@Controller
@RequestMapping("test")
public class TestController {

	private WeChatClient weChatClient;

	@Resource(name = "weChatClient")
	public void setWeChatClient(WeChatClient weChatClient) {
		this.weChatClient = weChatClient;
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

	@RequestMapping(value = "/client")
	@ResponseBody
	public SentMessage wechatClientTest() {
		TextMessage message = new TextMessage();
		message.setFromUserName("fromUserName");
		message.setToUserName("toUserName");
		message.setCreateTime(new Date().getTime());
		message.setMsgId(new Random(new Date().getTime()).nextInt());
		message.setMsgType("text");
		message.setContent("content");

		String uri = "http://localhost:8080/WeChat/test/json.do";

		TextSentMessage response = weChatClient.call(message, uri, TextSentMessage.class);

		return response;
	}

}
