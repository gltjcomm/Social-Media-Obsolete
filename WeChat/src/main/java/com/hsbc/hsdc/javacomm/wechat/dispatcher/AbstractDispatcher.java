package com.hsbc.hsdc.javacomm.wechat.dispatcher;


public interface AbstractDispatcher<Request, Response> {

	public Request construct(String data);

	public Response dispatch(Request requestMessage);

}
