package com.hsbc.hsdc.javacomm.wechat.dispatcher.impl.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hsbc.hsdc.javacomm.wechat.dispatcher.XMLDispatcher;
import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.received.TextMessage;
import com.hsbc.hsdc.javacomm.wechat.message.sent.NewsSentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.sent.TextSentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.sent.item.NewsItem;

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
		SentMessage message = null;
		
		String content = requestMessage.getContent();
		switch(content) {
		case "News" : //return the news message.
			List<NewsItem> articles = new ArrayList<NewsItem>();
			
			NewsItem item = new NewsItem();
			item.setPicUrl("http://mmbiz.qpic.cn/mmbiz/NVvB3l3e9aFzprSKmrfB3sE1U48ofI43TQkKUicH7CkzsGOfBMDMj6AEe2ibOKUZ0ebib2YVXg0FxsuzcQPZRawEA/0");
			item.setTitle("阅读优秀代码是提高开发人员修为的一种捷径");
			item.setDescription("");
			item.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5NzA1MTcyMA==&mid=200060408&idx=1&sn=4d5ad48ac832484ff043a66b7256e940#rd");
			articles.add(item);
			
			item = new NewsItem();
			item.setPicUrl("http://mmbiz.qpic.cn/mmbiz/NVvB3l3e9aFzprSKmrfB3sE1U48ofI434wVHUzRcatNhGLk20Tnl3JNUkNnhzGa0w1KKZnqWtwThepwGOibOYNQ/0");
			item.setTitle("十个顶级的C语言资源助你成为优秀的程序员");
			item.setDescription("");
			item.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5NzA1MTcyMA==&mid=200060408&idx=2&sn=2f257f482a6758a3b95d14a4891e2d8a#rd");
			articles.add(item);
			
			item = new NewsItem();
			item.setPicUrl("http://mmbiz.qpic.cn/mmbiz/NVvB3l3e9aFzprSKmrfB3sE1U48ofI43YsAPT0y03T3F0fRXuTWicmviaIVK1a5MpnzlO3KJGalOY8Lt7f096WyA/0");
			item.setTitle("网络婚恋规模明年达30亿元移动与O2O成主流");
			item.setDescription("");
			item.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5NzA1MTcyMA==&mid=200060408&idx=3&sn=170bc356db584d45b111800ea0c1a33a#rd");
			articles.add(item);
			
			message = new NewsSentMessage();
			message.setFromUserName(requestMessage.getToUserName());
			message.setToUserName(requestMessage.getFromUserName());
			message.setCreateTime(new Date().getTime());
			message.setMsgType("news");
			NewsSentMessage news = (NewsSentMessage)message;
			news.setArticles(articles);
			
			break;
		default : // return the same text message.
			message = new TextSentMessage();
			message.setFromUserName(requestMessage.getToUserName());
			message.setToUserName(requestMessage.getFromUserName());
			message.setCreateTime(new Date().getTime());
			message.setMsgType("text");
			TextSentMessage text = (TextSentMessage)message;
			text.setContent(requestMessage.getContent());
		}
		
		return message;
	}

}
