package com.hsbc.hsdc.javacomm.wechat.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/Interface")
public class InterfaceController {

	private Logger logger = Logger.getLogger(this.getClass());

	private final static String TOKEN = "JavaCommunity";

	@RequestMapping(value = "/Process", method = RequestMethod.POST)
	@ResponseBody
	public Object processRequest(HttpServletRequest request, HttpServletResponse response) {

		Object result = null;
		try {

			String echostr = request.getParameter("echostr");
			if (echostr == null || echostr.isEmpty()) {

				// Reply Message
				String postStr = null;
				try {
					postStr = this.readStreamParameter(request.getInputStream());
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (null != postStr && !postStr.isEmpty()) {

					Document document = null;
					try {
						document = DocumentHelper.parseText(postStr);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (null == document) {
						result = "";
					}

					Element root = document.getRootElement();
					String fromUserName = root.elementText("FromUserName");
					String toUserName = root.elementText("ToUserName");
					String keyword = root.elementTextTrim("Content");
					String time = new Date().getTime() + "";

					System.out.println("keyword: " + keyword);
					if (keyword != null && "News".equals(keyword)) {
						String textTpl = "<xml>" + "<ToUserName><![CDATA[%1$s]]></ToUserName>"
						        + "<FromUserName><![CDATA[%2$s]]></FromUserName>" + "<CreateTime>%3$s</CreateTime>"
						        + "<MsgType><![CDATA[%4$s]]></MsgType>" + "<ArticleCount>%5$s</ArticleCount>"
						        + "<Articles>%6$s</Articles>" + "</xml> ";
						String articleTpl = "<item>" + "<Title><![CDATA[%1$s]]></Title> "
						        + "<Description><![CDATA[%2$s]]></Description>" + "<PicUrl><![CDATA[%3$s]]></PicUrl>"
						        + "<Url><![CDATA[%4$s]]></Url>" + "</item>";

						String articleStr1 = articleTpl
						        .format(articleTpl,
						                "阅读优秀代码是提高开发人员修为的一种捷径",
						                "",
						                "http://mmbiz.qpic.cn/mmbiz/NVvB3l3e9aFzprSKmrfB3sE1U48ofI43TQkKUicH7CkzsGOfBMDMj6AEe2ibOKUZ0ebib2YVXg0FxsuzcQPZRawEA/0",
						                "http://mp.weixin.qq.com/s?__biz=MjM5NzA1MTcyMA==&mid=200060408&idx=1&sn=4d5ad48ac832484ff043a66b7256e940#rd");
						String articleStr2 = articleTpl
						        .format(articleTpl,
						                "十个顶级的C语言资源助你成为优秀的程序员",
						                "",
						                "http://mmbiz.qpic.cn/mmbiz/NVvB3l3e9aFzprSKmrfB3sE1U48ofI434wVHUzRcatNhGLk20Tnl3JNUkNnhzGa0w1KKZnqWtwThepwGOibOYNQ/0",
						                "http://mp.weixin.qq.com/s?__biz=MjM5NzA1MTcyMA==&mid=200060408&idx=2&sn=2f257f482a6758a3b95d14a4891e2d8a#rd");
						String articleStr3 = articleTpl
						        .format(articleTpl,
						                "网络婚恋规模明年达30亿元移动与O2O成主流",
						                "",
						                "http://mmbiz.qpic.cn/mmbiz/NVvB3l3e9aFzprSKmrfB3sE1U48ofI43YsAPT0y03T3F0fRXuTWicmviaIVK1a5MpnzlO3KJGalOY8Lt7f096WyA/0",
						                "http://mp.weixin.qq.com/s?__biz=MjM5NzA1MTcyMA==&mid=200060408&idx=3&sn=170bc356db584d45b111800ea0c1a33a#rd");

						String msgType = "news";
						String resultStr = textTpl.format(textTpl, fromUserName, toUserName, time, msgType, 3,
						        articleStr1 + articleStr2 + articleStr3);
						result = resultStr;
					} else if (keyword != null && !keyword.equals("")) {
						String textTpl = "<xml>" + "<ToUserName><![CDATA[%1$s]]></ToUserName>"
						        + "<FromUserName><![CDATA[%2$s]]></FromUserName>" + "<CreateTime>%3$s</CreateTime>"
						        + "<MsgType><![CDATA[%4$s]]></MsgType>" + "<Content><![CDATA[%5$s]]></Content>"
						        + "<FuncFlag>0</FuncFlag>" + "</xml>";
						String msgType = "text";
						String contentStr = "欢迎加入Java Community!";
						String resultStr = textTpl.format(textTpl, fromUserName, toUserName, time, msgType, contentStr);
						System.out.println("Response: " + resultStr);
						result = resultStr;
					} else {
						result = "";
					}
				}

			} else {

				// Validate Interface
				String signature = request.getParameter("signature");
				String timestamp = request.getParameter("timestamp");
				String nonce = request.getParameter("nonce");

				boolean isValid = checkSignature(signature, timestamp, nonce);
				if (isValid) {
					result = echostr;
				} else {
					result = "error";
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			result = "error";
		}

		System.out.println("Result: " + result);
		return result;
	}

	private String readStreamParameter(ServletInputStream in) {

		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return buffer.toString();
	}

	private boolean checkSignature(String signature, String timestamp, String nonce) {

		String token = TOKEN;
		String[] tmpArr = { token, timestamp, nonce };
		Arrays.sort(tmpArr);
		String tmpStr = this.ArrayToString(tmpArr);
		tmpStr = this.SHA1Encode(tmpStr);
		if (tmpStr.equalsIgnoreCase(signature)) {
			return true;
		} else {
			return false;
		}
	}

	private String ArrayToString(String[] arr) {
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			bf.append(arr[i]);
		}
		return bf.toString();
	}

	private String SHA1Encode(String sourceString) {

		String resultString = null;
		try {
			resultString = new String(sourceString);
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			resultString = byte2HexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
		}

		return resultString;
	}

	private String byte2HexString(byte[] bytes) {

		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}

		return buf.toString().toUpperCase();
	}
}
