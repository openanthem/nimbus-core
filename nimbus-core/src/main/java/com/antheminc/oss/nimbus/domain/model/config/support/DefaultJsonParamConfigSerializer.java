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
package com.antheminc.oss.nimbus.domain.model.config.support;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultJsonParamConfigSerializer extends JsonSerializer<ParamConfig<?>> {

	private static final String K_ID = "id";
	private static final String K_POINTERID = "pointerId";
	private static final String K_CODE = "code";
	
	private static final String K_UISTYLES = "uiStyles";
	private static final String K_LABELCONFIGS = "labelConfigs";
	private static final String K_VALIDATIONS = "validations";
	private static final String K_UINATURES = "uiNatures";
	
	private static final String K_TYPE = "type";
	
	private static final ThreadLocal<Deque<String>> TH_STACK = new ThreadLocal<>();
	
	@Override
	public void serialize(ParamConfig<?> p, JsonGenerator gen, SerializerProvider serializers)
	throws IOException, JsonProcessingException {
		
		final String lockId;
		if(TH_STACK.get()==null) {
			lockId = UUID.randomUUID().toString();
			TH_STACK.set(new LinkedList<>());
			TH_STACK.get().push(lockId);
		} else {
			lockId = null;
		}
		
		
		try {
			if(p.containsIgnoreListener(ListenerType.websocket))
				return;
			
			gen.writeStartObject();
			if(TH_STACK.get().contains(p.getId())) {
				gen.writeStringField(K_POINTERID, p.getId());
				
			} else {
				TH_STACK.get().push(p.getId());
				
				gen.writeStringField(K_ID, p.getId());
				gen.writeStringField(K_CODE, p.getCode());
				
				gen.writeObjectField(K_UISTYLES, p.getUiStyles());
				gen.writeObjectField(K_VALIDATIONS, p.getValidations());
				gen.writeObjectField(K_UINATURES, p.getUiNatures());

				gen.writeObjectField(K_TYPE, p.getType());

				
				TH_STACK.get().pop();
			}
			gen.writeEndObject();
			
		} finally {
			if(lockId!=null) {
				TH_STACK.set(null);
			}
		}
		
	}

}
