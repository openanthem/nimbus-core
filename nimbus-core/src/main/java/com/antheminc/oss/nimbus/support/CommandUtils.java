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
package com.antheminc.oss.nimbus.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.google.common.base.Enums;

/**
 * <p>Utility class for common operations perfomed using the command.
 * @author Tony Lopez
 *
 */
public final class CommandUtils {

	/**
	 * <p>Copy the values of any request params that also exist as fields within
	 * the provided object.
	 * @param obj the source object to copy values to
	 * @param command the command providing the request params to copy
	 */
	public static void copyRequestParams(Object obj, Command command) {
		if (null == command || null == command.getRequestParams()) {
			return;
		}
		Map<String, String> properties = new HashMap<>();
		for (Entry<String, String[]> entry : command.getRequestParams().entrySet()) {
			if (entry.getValue().length > 0) {
				properties.put(entry.getKey(), entry.getValue()[0]);
			}
		}
		BeanUtils.copyProperties(obj, properties);
	}

	/**
	 * <p>Get the enumerated type converted from a request param value given in
	 * a {@link Command} that match a given name. If the request param does not
	 * exist, the default value will be returned.
	 * @param command the command object to retrieve request parameters from
	 * @param parameterName the name of the parameter to retrieve
	 * @param defaultValue the default enumerated value to provide
	 * @return the provided enumerated type
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<?>> T getEnumFromRequestParam(Command command, String parameterName, T defaultValue) {
		String sRequestParamValue = command.getFirstParameterValue(parameterName);
		if (null == sRequestParamValue) {
			return defaultValue;
		}
		return (T) Enums.getIfPresent(defaultValue.getClass(), sRequestParamValue.toUpperCase()).or(defaultValue);
	}

	public static Command prepareCommand(String uri, Behavior... behaviors) {
		final Command command = CommandBuilder.withUri(uri).getCommand();
		if (behaviors != null) {
			Arrays.asList(behaviors).forEach((b) -> command.templateBehaviors().add(b));
		}
		return command;
	}

	private CommandUtils() {
		throw new UnsupportedOperationException();
	}
}
