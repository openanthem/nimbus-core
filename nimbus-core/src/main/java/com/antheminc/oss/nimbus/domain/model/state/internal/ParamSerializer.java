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
package com.antheminc.oss.nimbus.domain.model.state.internal;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;

import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
public class ParamSerializer extends JsonSerializer<Param<?>> {

	private static final String K_PATH = "path";
	private static final String K_ELEM_ID = "elemId";
	
	private static final String K_CONFIGID = "configId";
	private static final String K_CONFIG = "config";
	
	private static final String K_ACTIVE_VALS = "activeValidationGroups";
	private static final String K_LEAF_STATE = "leafState";
	private static final String K_MESSAGE = "message";
	
	private static final String K_TYPE = "type";
	private static final String K_VALUES = "values";
	
	private static final String K_IS_COL = "collection";
	private static final String K_IS_COL_ELEM = "collectionElem";
	private static final String K_IS_ENABLED = "enabled";
	private static final String K_IS_VISIBLE = "visible";
	private static final String K_IS_NESTED = "nested";
	
	private static final ThreadLocal<String> TH_PATH = new ThreadLocal<>();
	
	@Override
	public void serialize(Param<?> p, JsonGenerator gen, SerializerProvider provider) throws IOException {
		System.out.println("@@@ "+ p.getPath());
		
		final String lockId;
		if(TH_PATH.get()==null) {
			lockId = UUID.randomUUID().toString();
			TH_PATH.set(lockId);
		} else {
			lockId = null;
		}
		
		try {
			ParamWriter writer = new ParamWriter(gen);
			gen.writeStartObject();
			
			if(lockId!=null)
				gen.writeStringField(K_PATH, p.getPath());
			
			if (p.isCollectionElem())
				gen.writeStringField(K_ELEM_ID, p.findIfCollectionElem().getElemId());
			
			gen.writeStringField(K_CONFIGID, p.getConfigId());
			
			Model<?> rootDomain = p.getRootDomain();
			if (rootDomain != null && p == rootDomain.getAssociatedParam()) {
				gen.writeObjectField(K_CONFIG, p.getConfig());
			}
			
			Class<? extends ValidationGroup>[] activeValidationGroups = p.getActiveValidationGroups();
			if (ArrayUtils.isNotEmpty(activeValidationGroups))
				gen.writeObjectField(K_ACTIVE_VALS, p.getActiveValidationGroups());
			
			writer.writeObjectIfNotNull(K_LEAF_STATE, p::getLeafState);
			writer.writeObjectIfNotNull(K_MESSAGE, p::getMessage);
			writer.writeObjectIfNotNull(K_TYPE, p::getType);
			writer.writeObjectIfNotNull(K_VALUES, p::getValues);
			
			gen.writeBooleanField(K_IS_COL, p.isCollection());
			gen.writeBooleanField(K_IS_COL_ELEM, p.isCollectionElem());
			gen.writeBooleanField(K_IS_ENABLED, p.isEnabled());
			gen.writeBooleanField(K_IS_VISIBLE, p.isVisible());
			gen.writeBooleanField(K_IS_NESTED, p.isNested());
			
			gen.writeEndObject();
			
		} finally {
			if(lockId!=null) {
				TH_PATH.set(null);
			}
		}
	}

	@RequiredArgsConstructor
	private static class ParamWriter {
		private final JsonGenerator gen;
		
		private void writeObjectIfNotNull(String fieldName, Supplier<Object> cb) throws IOException {
			Object o = cb.get();
			if(o==null)
				return;
			
			gen.writeObjectField(fieldName, o);
		}
	}
}
