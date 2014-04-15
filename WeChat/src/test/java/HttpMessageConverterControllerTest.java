import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.hsbc.hsdc.javacomm.wechat.message.received.TextMessage;
import com.hsbc.hsdc.javacomm.wechat.message.sent.TextSentMessage;

public class HttpMessageConverterControllerTest {

	@Test
	public void testJSONUrl() throws IOException {
		try {
			RestTemplate restTemplate = buildRestTemplate();

			TextMessage message = new TextMessage();
			message.setFromUserName("fromUserName");
			message.setToUserName("toUserName");
			message.setCreateTime(new Date().getTime());
			message.setMsgId(new Random(new Date().getTime()).nextInt());
			message.setMsgType("text");

			message.setContent("content");

			HttpHeaders entityHeaders = new HttpHeaders();
			// json传输
			entityHeaders.setContentType(MediaType.valueOf("application/json;UTF-8"));
			entityHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			entityHeaders.setAcceptCharset(Collections.singletonList(Charset.forName("UTF-8")));
			HttpEntity<TextMessage> requestEntity = new HttpEntity<TextMessage>(message, entityHeaders);

			ResponseEntity<TextSentMessage> responseEntity = restTemplate.exchange("http://localhost:8080/WeChat/Interface/json.do", HttpMethod.POST, requestEntity,
					TextSentMessage.class);

			TextSentMessage response = responseEntity.getBody();
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private RestTemplate buildRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		restTemplate.getMessageConverters().add(jsonConverter);

		return restTemplate;
	}
}
