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
package com.antheminc.oss.nimbus.integration.mq;

import com.antheminc.oss.nimbus.domain.model.state.internal.AbstractEvent;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Tony Lopez
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MQEvent extends AbstractEvent<String, String> {

	public MQEvent(String commandUrl, String payload) {
		super("MQ-EVENT", commandUrl, payload);
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
