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
package com.antheminc.oss.nimbus.converter.writer;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.converter.RowProcessable.RowProcessingHandler;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * <p>Bean Writer that executes a given command with the provided bean as a
 * payload.
 * 
 * @author Sandeep Mantha
 * @author Tony Lopez
 *
 */
@RequiredArgsConstructor
public class CommandHandlingBeanWriter implements RowProcessingHandler<Object> {

	private final ObjectMapper om;
	private final CommandExecutorGateway commandGateway;
	private final Command command;

	@Override
	public void write(Object bean) {
		String payload;
		try {
			payload = om.writeValueAsString(bean);
		} catch (JsonProcessingException e) {
			throw new FrameworkRuntimeException("Failed to write bean data. Failed to convert bean data to JSON.", e);
		}
		commandGateway.execute(command, payload);
	}

}
