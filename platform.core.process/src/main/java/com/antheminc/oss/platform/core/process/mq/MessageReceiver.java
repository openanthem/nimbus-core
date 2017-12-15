package com.antheminc.oss.platform.core.process.mq;

import com.antheminc.oss.nimbus.core.util.JustLogit;

public class MessageReceiver {

	private JustLogit logit = new JustLogit(this.getClass());
	
    public void receiveMessage(byte[] message) {
    	logit.info(()->"Received message .. "+ new String(message));
    }
}
