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
package com.antheminc.oss.nimbus.domain.model.state.messagequeue;

import com.antheminc.oss.nimbus.domain.model.state.internal.AbstractEvent;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Tony Lopez
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageQueueEvent extends AbstractEvent<String, String> {

	public MessageQueueEvent() {}
	
	public MessageQueueEvent(String commandUrl, String payload) {
		super(null, commandUrl, payload);
	}

	public String getCommandUrl() {
		return this.id;
	}

	@Override
	@JsonSetter("commandUrl")
	public void setId(String commandUrl) {
		this.id = commandUrl;
	}
}
