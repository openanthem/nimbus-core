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
package com.antheminc.oss.nimbus.domain.cmd.exec;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.cmd.exec.internal.ReservedKeywordRegistry;
import com.antheminc.oss.nimbus.domain.model.config.ExecutionConfig.Context;

import lombok.RequiredArgsConstructor;

/**
 * @author Tony Lopez
 * @since 1.3
 *
 */
@RequiredArgsConstructor
public class DefaultConfigPlaceholderResolver implements ConfigPlaceholderResolver {

	private final ReservedKeywordRegistry reservedKeywordRegistry;
	
	@Override
	public String resolve(Context context, String pathToResolve) {
		Map<Integer, String> entries = ParamPathExpressionParser.parse(pathToResolve);

		// remove reserved placeholders -- those will be handled by another
		// resolver
		entries = entries.entrySet().stream().filter(x -> !this.reservedKeywordRegistry.exists(x.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		if (MapUtils.isEmpty(entries)) {
			return pathToResolve;
		}

		String out = pathToResolve;
		for (Entry<Integer, String> entry : entries.entrySet()) {
			String placeholderName = ParamPathExpressionParser.stripPrefixSuffix(entry.getValue());
			String placeholderValue = context.getPlaceholderMap().get(placeholderName);
			out = StringUtils.replace(out, entry.getValue(), placeholderValue, 1);
		}

		return out;
	}
}
