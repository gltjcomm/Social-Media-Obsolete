package com.hsbc.hsdc.javacomm.wechat.interceptors;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthenticateInterceptor extends HandlerInterceptorAdapter {

	private final static String TOKEN = "JavaCommunity";
	private final static Logger logger = Logger.getLogger(AuthenticateInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");

		logger.info("Request from " + request.getRemoteHost() + "; Parameters - signature:"
				+ signature + "; timestamp:" + timestamp + "; nonce:" + nonce);

		if (signature == null || timestamp == null || nonce == null) {
			logger.error("Authenticate fail - parameters validate fail.");
			return false;
		}

		return authenticate(signature, timestamp, nonce)
				&& super.preHandle(request, response, handler);
	}

	private boolean authenticate(String signature, String timestamp, String nonce) {
		String[] tempArray = { TOKEN, timestamp, nonce };
		Arrays.sort(tempArray);
		String plainText = tempArray[0] + tempArray[1] + tempArray[2];

		String cipherText = encode(plainText);

		if (cipherText != null && cipherText.compareToIgnoreCase(signature) == 0) {
			logger.info("Authenticate successfully - authenticate access.");
			return true;
		} else {
			logger.error("Authenticate fail - non-authenticate access.");
			return false;
		}

	}

	private String encode(String plainText) {

		String cipherText = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			cipherText = byte2HexString(md.digest(plainText.getBytes()));
		} catch (Exception ex) {
		}

		return cipherText;
	}

	private String byte2HexString(byte[] bytes) {

		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Integer.toHexString((int) bytes[i] & 0xff));
		}

		return buf.toString().toUpperCase();
	}

}
