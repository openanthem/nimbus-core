package com.anthem.nimbus.platform.core.process.mq;

import com.anthem.oss.nimbus.core.util.JustLogit;

public class MessageReceiver {

	private JustLogit logit = new JustLogit(this.getClass());
	
    public void receiveMessage(byte[] message) {
    	logit.info(()->"Received message .. "+ new String(message));
    }
}
