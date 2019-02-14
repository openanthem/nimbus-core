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
import java.util.UUID;
import java.util.function.Supplier;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Chart;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TreeGrid;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListElemParam;
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
public class DefaultJsonParamSerializer extends JsonSerializer<Param<?>> {

	public static final String K_PATH = "path";
	public static final String K_ELEM_ID = "elemId";
	
	public static final String K_CONFIGID = "configId";
	public static final String K_CONFIG = "config";
	
	public static final String K_ACTIVE_VALS = "activeValidationGroups";
	public static final String K_LEAF_STATE = "leafState";
	public static final String K_MESSAGE = "message";
	
	public static final String K_TYPE = "type";
	public static final String K_VALUES = "values";
	public static final String K_LABELS = "labels";
	public static final String K_STYLE = "style";
	public static final String K_ELEM_LABELS = "elemLabels";
	
	public static final String K_PAGE = "page";
	
	public static final String K_IS_COL_ELEM = "collectionElem";
	public static final String K_IS_ENABLED = "enabled";
	public static final String K_IS_VISIBLE = "visible";
	
	private static final ThreadLocal<String> TH_PATH = new ThreadLocal<>();
	
	@Override
	public void serialize(Param<?> p, JsonGenerator gen, SerializerProvider provider) throws IOException {

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
			if(rootDomain != null && p == rootDomain.getAssociatedParam()) {
				gen.writeObjectField(K_CONFIG, p.getConfig());
			}
			
			if(p.isCollection()) {
				gen.writeObjectField(K_PAGE, p.findIfCollection().getPage());
				writer.writeObjectIfNotNull(K_ELEM_LABELS, p.findIfCollection()::getElemLabels);
			}
			
			writer.writeBooleanIfNotDefault(K_IS_COL_ELEM, p::isCollectionElem, false);
			writer.writeBooleanIfNotDefault(K_IS_ENABLED, p::isEnabled, true);
			writer.writeBooleanIfNotDefault(K_IS_VISIBLE, p::isVisible, true);

			Class<? extends ValidationGroup>[] activeValidationGroups = p.getActiveValidationGroups();
			if(ArrayUtils.isNotEmpty(activeValidationGroups))
				gen.writeObjectField(K_ACTIVE_VALS, p.getActiveValidationGroups());
			
			if(CollectionUtils.isNotEmpty(p.getMessages())) {
				writer.writeObjectIfNotNull(K_MESSAGE, p::getMessages);
				// Resetting the message in param, so that once the message is read through the http response, it is removed from param state.
				/* TODO Scenarios where further conditional processing is done based on the message text needs to be addressed,
				 since the message state has been reset. There will be a sync issue until a state is reloaded and message is set.*/
				p.setMessages(null);
			}
			writer.writeObjectIfNotNull(K_VALUES, p::getValues);
			writer.writeObjectIfNotNull(K_LABELS, p::getLabels);
			writer.writeObjectIfNotNull(K_STYLE, p::getStyle);
			
			if(!p.getType().getName().equals("string"))
				writer.writeObjectIfNotNull(K_TYPE, p::getType);
			
			writer.writeLeafStateConditionally(p);
			
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
		
		private void writeBooleanIfNotDefault(String fieldName, Supplier<Boolean> cb, boolean defaultValue) throws IOException {
			boolean o = cb.get();
			if(o==defaultValue)
				return;
			
			gen.writeBooleanField(fieldName, o);
		}
		
		private void writeLeafStateConditionally(Param<?> p) throws IOException {
			/* legacy code using leafState */
			if(p.isLeaf() || hasGrid(p)) {
				Object o = p.getLeafState();
				if(o==null)
					return;

				gen.writeObjectField(K_LEAF_STATE, o);
			}
		}
		
		private boolean hasGrid(Param<?> p) {
			if(!p.isCollectionElem())
				return false;
			
			ListElemParam<?> elemParam = p.findIfCollectionElem();
			Param<?> colParam = elemParam.getParentModel().getAssociatedParam();
			if(colParam.getConfig().getUiStyles()==null)
				return false;
			
			return ((colParam.getConfig().getUiStyles().getAnnotation().annotationType()==Grid.class) || (colParam.getConfig().getUiStyles().getAnnotation().annotationType()==Chart.class)
					|| (colParam.getConfig().getUiStyles().getAnnotation().annotationType() == TreeGrid.class));
			
		}
	}
}
