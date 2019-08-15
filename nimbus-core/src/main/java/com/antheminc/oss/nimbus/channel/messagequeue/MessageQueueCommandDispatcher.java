/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.channel.messagequeue;

import com.antheminc.oss.nimbus.channel.CommandDispatcher;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.messagequeue.MessageQueueEvent;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.Getter;

/**
 * @author Tony Lopez
 *
 */
@Getter
@EnableLoggingInterceptor
public class MessageQueueCommandDispatcher extends CommandDispatcher {

	public MessageQueueCommandDispatcher(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	public Object handle(MessageQueueEvent event) {
		Command cmd = CommandBuilder.withUri(event.getCommandUrl()).getCommand();
		return handle(cmd, event.getPayload());
	}
}
