package com.hsbc.hsdc.javacomm.wechat.dispatcher.factory;

import com.hsbc.hsdc.javacomm.wechat.dispatcher.AbstractDispatcher;

/**
 * Factory method pattern.
 * 
 * @author peihuadeng
 *
 * @param <T>
 */
public interface AbstractDispatcherFactory<T extends AbstractDispatcher> {

	public T createInstance(String data);

}
