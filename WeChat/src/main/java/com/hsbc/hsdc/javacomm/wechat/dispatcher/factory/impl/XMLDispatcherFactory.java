package com.hsbc.hsdc.javacomm.wechat.dispatcher.factory.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.hsbc.hsdc.javacomm.wechat.dispatcher.XMLDispatcher;
import com.hsbc.hsdc.javacomm.wechat.dispatcher.factory.AbstractDispatcherFactory;
import com.hsbc.hsdc.javacomm.wechat.message.ReceivedMessage;

public class XMLDispatcherFactory implements AbstractDispatcherFactory<XMLDispatcher<ReceivedMessage>> {
	
	private final static Logger logger = Logger.getLogger(XMLDispatcherFactory.class);

	private final static String METHOD_NAME = "match";
	
	private String[] clazzArray = null;
	
	public XMLDispatcherFactory(String[] clazzArray) {
		this.clazzArray = clazzArray;
	}

	@Override
	public XMLDispatcher<ReceivedMessage> createInstance(String data) {
		XMLDispatcher<ReceivedMessage> dispatcher = null;

		for (int i = 0; i < clazzArray.length; i++) {
			try {
				// get the Class object for the string.
				Class<?> clazz = Class.forName(clazzArray[i]);
				if(XMLDispatcher.class.isAssignableFrom(clazz)) {
					Class<XMLDispatcher<ReceivedMessage>> xmlDispatcherClazz = (Class<XMLDispatcher<ReceivedMessage>>) clazz;
					// get static method - "boolean match(InputStream)".
					Method method = xmlDispatcherClazz.getMethod(METHOD_NAME, String.class);
					if (method != null) {
						// get the return object through invoking the method.
						Object returnObject = method.invoke(xmlDispatcherClazz, data);
						// judge whether the type of return object is boolean.
						if (returnObject instanceof Boolean) {
							boolean match = (boolean) returnObject;
							// match - true : break the loop and return the
							// instance; false : continue the loop.
							if (match) {
								dispatcher = (XMLDispatcher<ReceivedMessage>) xmlDispatcherClazz.newInstance();
								logger.info("The dispatcher match : " + clazzArray[i]);
								break;
							}
						} else {
							logger.warn("The return type of the static method \"" + METHOD_NAME + "\" in the instance \"" + clazzArray[i] + "\" must be \"boolean\". This class is ignored.");
						}
					}
				} else {
					logger.warn("The class \"" + clazzArray[i] + "\" does not implement the XMLDispatcher interface. This class is ignored.");
				}
			} catch (ClassNotFoundException e) {
				logger.error("The class \"" + clazzArray[i] + "\" is not found : " + clazzArray[i], e);
			} catch (NoSuchMethodException e) {
				logger.error("The class \"" + clazzArray[i] + "\" must has the static method : " + METHOD_NAME, e);
			} catch (SecurityException e) {
				logger.error("The method \"" + METHOD_NAME + "\" of the class \"" + clazzArray[i] + "\" can not be accessed", e);
			} catch (IllegalAccessException e) {
				logger.error("The method \"" + METHOD_NAME + "\" of the class \"" + clazzArray[i] + "\" can not be accessed", e);
			} catch (IllegalArgumentException e) {
				logger.error("The argument of the method \"" + METHOD_NAME + "\" of the class \"" + clazzArray[i] + "\" is not right.", e);
			} catch (InvocationTargetException e) {
				logger.error("Faill to invocate the method \"" + METHOD_NAME + "\" of the class \"" + clazzArray[i] + "\"", e);
			} catch (InstantiationException e) {
				logger.error("Fail to create a Instance of the class \"" + clazzArray[i] + "\"", e);
			}
		}
		return dispatcher;
	}

}
