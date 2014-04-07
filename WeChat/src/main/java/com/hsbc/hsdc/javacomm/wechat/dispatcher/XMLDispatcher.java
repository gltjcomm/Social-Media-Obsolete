package com.hsbc.hsdc.javacomm.wechat.dispatcher;

import com.hsbc.hsdc.javacomm.wechat.message.ReceivedMessage;
import com.hsbc.hsdc.javacomm.wechat.message.SentMessage;

public interface XMLDispatcher<Request extends ReceivedMessage> extends AbstractDispatcher<Request, SentMessage>{

}
