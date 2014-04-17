package com.hsbc.hsdc.javacomm.wechat.client;

import java.nio.charset.Charset;
import java.util.Collections;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("weChatClient")
public class WeChatClient {
	
	public static final String grant_type = ""; //used to get access_token
	public static final String appid = ""; //used to get access_token
	public static final String secret = ""; //used to get access_token
	
	public static String ACCESS_TOKEN = "";//will change every 7200 seconds.
	
	private RestTemplate restTemplate;

	@Resource(name = "restTemplate")
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public <T> T call(Object request, String uri, Class<T> responseType) {
		// set http header.
		HttpHeaders entityHeaders = new HttpHeaders();
		// json传输
		entityHeaders.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
		entityHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		entityHeaders.setAcceptCharset(Collections.singletonList(Charset.forName("UTF-8")));

		HttpEntity requestEntity = new HttpEntity(request, entityHeaders);

		ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, responseType);

		T response = responseEntity.getBody();

		return response;

	}

}
