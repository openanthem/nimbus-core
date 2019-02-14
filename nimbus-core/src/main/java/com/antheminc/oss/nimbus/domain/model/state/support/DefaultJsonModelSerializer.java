/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.model.state.support;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultJsonModelSerializer extends JsonSerializer<Model<?>> {

	private static final String K_PARAMS = "params";
	
	@Override
	public void serialize(Model<?> m, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		
		List<Param<?>> rParams = getResolvedParams(m);
		if(!CollectionUtils.isEmpty(rParams))
			gen.writeObjectField(K_PARAMS, rParams);
		
		gen.writeEndObject();
	}
	
	private List<Param<?>> getResolvedParams(Model<?> m) {
		List<Param<?>> params = m.getParams();
		if(CollectionUtils.isEmpty(params)) 
			return null;
		
		return params.stream()
			.filter(this::include)
			.collect(Collectors.toList());
	}
	
	private boolean include(Param<?> p) {
		return !p.getConfig().containsIgnoreListener(ListenerType.websocket);
	}

}
