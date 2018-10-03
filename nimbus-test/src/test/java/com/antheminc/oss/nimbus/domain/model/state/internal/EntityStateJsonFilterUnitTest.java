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

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.GROUP_1;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.test.domain.mock.MockParam;
import com.antheminc.oss.nimbus.test.domain.mock.MockStateType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;

/**
 * @author Tony Lopez
 *
 */
public class EntityStateJsonFilterUnitTest {

	private EntityStateJsonFilter testee;
	
	@Before
	public void init() {
		this.testee = new EntityStateJsonFilter();
	}
	
	@Test
	public void testOmitConditionalParam_enabled() {
		MockParam param = new MockParam();
		param.setEnabled(false);
		Assert.assertFalse(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_ENABLED));
		
		param.setEnabled(true);
		Assert.assertTrue(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_ENABLED));
	}
	
	@Test
	public void testOmitConditionalParam_visible() {
		MockParam param = new MockParam();
		param.setVisible(false);
		Assert.assertFalse(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_VISIBLE));
		
		param.setVisible(true);
		Assert.assertTrue(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_VISIBLE));
	}
	
	@Test
	public void testOmitConditionalParam_collection() {
		MockParam param = new MockParam();
		param.setCollection(true);
		Assert.assertFalse(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_COLLECTION));
		
		param.setCollection(false);
		Assert.assertTrue(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_COLLECTION));
	}
	
	@Test
	public void testOmitConditionalParam_collectionElem() {
		MockParam param = new MockParam();
		param.setCollectionElem(true);
		Assert.assertFalse(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_COLLECTION_ELEM));
		
		param.setCollectionElem(false);
		Assert.assertTrue(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_COLLECTION_ELEM));
	}
	
	@Test
	public void testOmitConditionalParam_nested() {
		MockParam param = new MockParam();
		param.setNested(true);
		Assert.assertFalse(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_NESTED));
		
		param.setNested(false);
		Assert.assertTrue(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_NESTED));
	}
	
	@Test
	public void testOmitConditionalParam_activeValidationGroups() {
		MockParam param = new MockParam();
		Set<Class<? extends ValidationGroup>> validationGroups = new HashSet<>();
		validationGroups.add(GROUP_1.class);
		param.setActiveValidationGroups(validationGroups.toArray(new Class[validationGroups.size()]));
		Assert.assertFalse(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_ACTIVE_VALIDATION_GROUPS));
		
		param.setActiveValidationGroups(null);
		Assert.assertTrue(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_ACTIVE_VALIDATION_GROUPS));
	}
	
	@Test
	public void testOmitConditionalParam_type() {
		MockParam param = new MockParam();
		MockStateType stateType = new MockStateType(null);
		stateType.setNested(true);
		param.setType(stateType);
		Assert.assertFalse(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_TYPE));
		
		stateType.setName(EntityStateJsonFilter.FIELD_NAME_STRING);
		Assert.assertFalse(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_TYPE));
		
		stateType.setNested(false);
		Assert.assertTrue(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_TYPE));
	}
	
	@Test
	public void testOmitConditionalParam_leafState() {
		MockParam param = new MockParam();
		param.setLeaf(true);
		Assert.assertFalse(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_LEAF_STATE));
		
		param.setLeaf(false);
		Assert.assertTrue(this.testee.omitConditional(param, EntityStateJsonFilter.FIELD_NAME_LEAF_STATE));
	}
	
	@Test
	public void testOmitConditionalModel() {
		Assert.assertFalse(this.testee.omitConditional(null, null));
	}
	
	@Test
	public void testSerializeAsField_omitParamConfig() throws Exception {
		JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
		SerializerProvider provider = Mockito.mock(SerializerProvider.class);
		PropertyWriter writer = Mockito.mock(PropertyWriter.class);
		
		Param pojo = Mockito.mock(Param.class);
		Model rootDomain = Mockito.mock(Model.class);
		Param associatedParam = Mockito.mock(Param.class);
		
		Mockito.when(pojo.getRootDomain()).thenReturn(rootDomain);
		Mockito.when(rootDomain.getAssociatedParam()).thenReturn(associatedParam);
		Mockito.when(writer.getName()).thenReturn(AbstractEntityStateJsonFilter.FIELD_NAME_CONFIG);
		this.testee.serializeAsField(pojo, jgen, provider, writer);
		Mockito.verify(writer, Mockito.times(0)).serializeAsField(pojo, jgen, provider);
	}
	
	@Test
	public void testSerializeAsField_seralizeParamConfig_missingRootDomain() throws Exception {
		JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
		SerializerProvider provider = Mockito.mock(SerializerProvider.class);
		PropertyWriter writer = Mockito.mock(PropertyWriter.class);
		
		Param pojo = Mockito.mock(Param.class);
		
		Mockito.when(pojo.getRootDomain()).thenReturn(null);
		Mockito.when(writer.getName()).thenReturn(AbstractEntityStateJsonFilter.FIELD_NAME_CONFIG);
		this.testee.serializeAsField(pojo, jgen, provider, writer);
		Mockito.verify(writer, Mockito.times(1)).serializeAsField(pojo, jgen, provider);
	}
	
	@Test
	public void testSerializeAsField_seralizeParamConfig_sameAssociatedParam() throws Exception {
		JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
		SerializerProvider provider = Mockito.mock(SerializerProvider.class);
		PropertyWriter writer = Mockito.mock(PropertyWriter.class);
		
		Param pojo = Mockito.mock(Param.class);
		Model rootDomain = Mockito.mock(Model.class);
		
		Mockito.when(pojo.getRootDomain()).thenReturn(rootDomain);
		Mockito.when(rootDomain.getAssociatedParam()).thenReturn(pojo);
		Mockito.when(writer.getName()).thenReturn(AbstractEntityStateJsonFilter.FIELD_NAME_CONFIG);
		this.testee.serializeAsField(pojo, jgen, provider, writer);
		Mockito.verify(writer, Mockito.times(1)).serializeAsField(pojo, jgen, provider);
	}
	
	@Test
	public void testSerializeAsField_omitModelConfig() throws Exception {
		JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
		SerializerProvider provider = Mockito.mock(SerializerProvider.class);
		PropertyWriter writer = Mockito.mock(PropertyWriter.class);
		
		Model pojo = Mockito.mock(Model.class);
		
		Mockito.when(writer.getName()).thenReturn(AbstractEntityStateJsonFilter.FIELD_NAME_CONFIG);
		this.testee.serializeAsField(pojo, jgen, provider, writer);
		Mockito.verify(writer, Mockito.times(0)).serializeAsField(pojo, jgen, provider);
	}
	
	@Test
	public void testSerializeAsField_seralizeModelField() throws Exception {
		JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
		SerializerProvider provider = Mockito.mock(SerializerProvider.class);
		PropertyWriter writer = Mockito.mock(PropertyWriter.class);
		
		Model pojo = Mockito.mock(Model.class);
		
		Mockito.when(writer.getName()).thenReturn("foo");
		this.testee.serializeAsField(pojo, jgen, provider, writer);
		Mockito.verify(writer, Mockito.times(1)).serializeAsField(pojo, jgen, provider);
	}
}
