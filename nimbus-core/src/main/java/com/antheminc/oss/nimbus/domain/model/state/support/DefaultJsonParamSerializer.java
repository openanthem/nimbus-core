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

	private static final String K_PATH = "path";
	private static final String K_ELEM_ID = "elemId";
	
	private static final String K_CONFIGID = "configId";
	private static final String K_CONFIG = "config";
	
	private static final String K_ACTIVE_VALS = "activeValidationGroups";
	private static final String K_LEAF_STATE = "leafState";
	private static final String K_MESSAGE = "message";
	
	private static final String K_TYPE = "type";
	private static final String K_VALUES = "values";
	private static final String K_LABELS = "labels";
	private static final String K_STYLE = "style";
	private static final String K_ELEM_LABELS = "elemLabels";
	
	private static final String K_PAGE = "page";
	
	//private static final String K_IS_COL = "collection";
	private static final String K_IS_COL_ELEM = "collectionElem";
	private static final String K_IS_ENABLED = "enabled";
	private static final String K_IS_VISIBLE = "visible";
	//private static final String K_IS_NESTED = "nested";
	
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
			
			//writer.writeBooleanIfNotDefault(K_IS_COL, p::isCollection, false);
			writer.writeBooleanIfNotDefault(K_IS_COL_ELEM, p::isCollectionElem, false);
			writer.writeBooleanIfNotDefault(K_IS_ENABLED, p::isEnabled, true);
			writer.writeBooleanIfNotDefault(K_IS_VISIBLE, p::isVisible, true);
			//writer.writeBooleanIfNotDefault(K_IS_NESTED, p::isNested, false);

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
			
			/*
			if(p.isLeaf() || hasGrid(p)) {
				writeSyntheticState(p);
			}
			*/
		}
		
		private boolean hasGrid(Param<?> p) {
			if(!p.isCollectionElem())
				return false;
			
			ListElemParam<?> elemParam = p.findIfCollectionElem();
			Param<?> colParam = elemParam.getParentModel().getAssociatedParam();
			if(colParam.getConfig().getUiStyles()==null)
				return false;
			
			return ((colParam.getConfig().getUiStyles().getAnnotation().annotationType()==Grid.class) || (colParam.getConfig().getUiStyles().getAnnotation().annotationType() == TreeGrid.class));
		}
		
		private void writeSyntheticState(Param<?> p) throws IOException {
			//gen.writeStartObject();
			writeParamState(p, true);
			//gen.writeEndObject();
		}
		
		private void writeParamState(Param<?> p, boolean isRoot) throws IOException {
			// unmapped
			if(!p.isMapped()) {
				writeWithin(p, isRoot, p::getState);
				return;
			}
			
			// mapped: leaf
			if(p.isLeafOrCollectionWithLeafElems()) {
				writeLeafOrCollectionWithLeafElements(p, isRoot);
				return;
			}
			
			// mapped: nested
			if(p.findIfNested()==null || p.findIfNested().templateParams().isNullOrEmpty())
				return;
			
			// mapped: collection
			if(p.isCollection()) {
				writeCollection(p, isRoot);
				return;
			}
			
			// mapped: nested
			gen.writeObjectFieldStart(getFieldNameForState(p, isRoot));
			for(Param<?> cp : p.findIfNested().getParams()) {
				if(cp.isLeafOrCollectionWithLeafElems()) 
					writeLeafOrCollectionWithLeafElements(cp, false);
				else 
					writeParamState(cp, false);
			}
			gen.writeEndObject();
		}

		private void writeCollection(Param<?> p, boolean isRoot) throws IOException {
			gen.writeObjectFieldStart(getFieldNameForState(p, isRoot));
			for(Param<?> ep : p.findIfNested().getParams()) {
				gen.writeStartArray();
				writeParamState(ep, isRoot);
				gen.writeEndArray();					
			}
			gen.writeEndObject();
		}
		
		private void writeLeafOrCollectionWithLeafElements(Param<?> p, boolean isRoot) throws IOException {
			writeWithin(p, isRoot, p::getState);
		}
	
		private void writeWithin(Param<?> p, boolean isRoot, Supplier<Object> cb) throws IOException {
			String fieldName = getFieldNameForState(p, isRoot); 
			writeObjectIfNotNull(fieldName, cb);
		}
		
		private String getFieldNameForState(Param<?> p, boolean isRoot) {
			return isRoot ? K_LEAF_STATE : p.getConfig().getBeanName(); 
		}
	}
}
