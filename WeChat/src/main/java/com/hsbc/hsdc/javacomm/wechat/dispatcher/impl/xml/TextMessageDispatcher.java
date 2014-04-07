package com.hsbc.hsdc.javacomm.wechat.dispatcher.impl.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hsbc.hsdc.javacomm.wechat.dispatcher.XMLDispatcher;
import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.received.TextMessage;

public class TextMessageDispatcher implements XMLDispatcher<TextMessage> {
	
	public final static String MSG_TYPE = "text"; 

	public static boolean match(String data) {
		boolean match = false;
		
		Document document = null;
		try {
			document = DocumentHelper.parseText(data);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		if(document != null) {
			Element root = document.getRootElement();
			String msgType = root.elementTextTrim("MsgType");
			if(msgType != null && msgType.trim().equalsIgnoreCase(MSG_TYPE)) {
				match = true;
			}
		}
		
		return match;
	}

	@Override
	public TextMessage construct(String data) {
		TextMessage message = null;		
		Document document = null;
		
		try {
			document = DocumentHelper.parseText(data);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		if(document != null) {
			Element root = document.getRootElement();
			message = new TextMessage();
			message.setMsgId(Long.parseLong(root.elementTextTrim("MsgId")));
			message.setToUserName(root.elementTextTrim("ToUserName"));
			message.setFromUserName(root.elementTextTrim("FromUserName"));
			message.setCreateTime(Long.parseLong(root.elementTextTrim("CreateTime")));
			message.setMsgType(MSG_TYPE);
			
			message.setContent(root.elementTextTrim("Content"));
		}
		
		return message;
	}

	@Override
	public SentMessage dispatch(TextMessage requestMessage) {
		
		return null;
	}

}
