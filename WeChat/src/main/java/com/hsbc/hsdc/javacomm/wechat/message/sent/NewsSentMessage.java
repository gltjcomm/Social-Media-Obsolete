package com.hsbc.hsdc.javacomm.wechat.message.sent;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;
import com.hsbc.hsdc.javacomm.wechat.message.sent.item.NewsItem;

@XmlRootElement(name = "xml")
public class NewsSentMessage extends SentMessage {

	private List<NewsItem> articles;

	@XmlElement(name = "artidesCount")
	public int getArtidesCount() {
		int size = 0;

		if (articles != null) {
			size = articles.size();
		}
		return size;
	}

	@XmlElementWrapper(name = "articles")
	@XmlElement(name = "item")
	public List<NewsItem> getArticles() {
		return articles;
	}

	public void setArticles(List<NewsItem> articles) {
		this.articles = articles;
	}

}
