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
package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.internal.AbstractEvent;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelPersistenceHandler;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;

/**
 * @author Tony Lopez
 *
 */
public class ParamStateBulkPersistenceEventListenerTest {

	private ModelRepositoryFactory repoFactory;
	
	private ParamStateBulkPersistenceEventListener testee;
	
	public static class NoDomain {}
	
	@Domain(value = "noListener")
	public static class NoListener {}
	
	@Domain(value = "persistedEntity1", includeListeners = { ListenerType.persistence })
	public static class PersistedEntity1 {}
	
	@Before
	public void init() {
		this.repoFactory = Mockito.mock(ModelRepositoryFactory.class);
		this.testee = new ParamStateBulkPersistenceEventListener(this.repoFactory);
	}
	
	@Test
	public void testListenNoEvents() {
		Assert.assertTrue(this.testee.listen(null));
		Assert.assertTrue(this.testee.listen(new ArrayList<AbstractEvent<String, Param<?>>>()));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testListenNonAllowedEventsNoDomain() {
		AbstractEvent<String, Param<?>> event1 = Mockito.mock(AbstractEvent.class);
		Param e1Param = Mockito.mock(Param.class);
		Model e1Model = Mockito.mock(Model.class);
		ModelConfig e1ModelConfig = Mockito.mock(ModelConfig.class);
		
		Mockito.when(event1.getPayload()).thenReturn(e1Param);
		Mockito.when(e1Param.getRootDomain()).thenReturn(e1Model);
		Mockito.when(e1Model.getConfig()).thenReturn(e1ModelConfig);
		Mockito.when(e1ModelConfig.getReferredClass()).thenReturn(NoDomain.class);
		
		List<AbstractEvent<String, Param<?>>> events = new ArrayList<>();
		events.add(event1);
		
		Assert.assertFalse(this.testee.listen(events));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testListenNonAllowedEventsNoListener() {
		AbstractEvent<String, Param<?>> event1 = Mockito.mock(AbstractEvent.class);
		Param e1Param = Mockito.mock(Param.class);
		Model e1Model = Mockito.mock(Model.class);
		ModelConfig e1ModelConfig = Mockito.mock(ModelConfig.class);
		
		Mockito.when(event1.getPayload()).thenReturn(e1Param);
		Mockito.when(e1Param.getRootDomain()).thenReturn(e1Model);
		Mockito.when(e1Model.getConfig()).thenReturn(e1ModelConfig);
		Mockito.when(e1ModelConfig.getReferredClass()).thenReturn(NoListener.class);
		
		List<AbstractEvent<String, Param<?>>> events = new ArrayList<>();
		events.add(event1);
		
		Assert.assertFalse(this.testee.listen(events));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testListenNoRepoDefined() {
		AbstractEvent<String, Param<?>> event1 = Mockito.mock(AbstractEvent.class);
		Param e1Param = Mockito.mock(Param.class);
		Model e1Model = Mockito.mock(Model.class);
		ModelConfig e1ModelConfig = Mockito.mock(ModelConfig.class);
		
		Mockito.when(event1.getPayload()).thenReturn(e1Param);
		Mockito.when(e1Param.getRootDomain()).thenReturn(e1Model);
		Mockito.when(e1Model.getConfig()).thenReturn(e1ModelConfig);
		Mockito.when(e1ModelConfig.getReferredClass()).thenReturn(PersistedEntity1.class);
		Mockito.when(e1ModelConfig.getAlias()).thenReturn("persistedEntity1");
		Mockito.when(e1ModelConfig.getRepo()).thenReturn(null);
		
		List<AbstractEvent<String, Param<?>>> events = new ArrayList<>();
		events.add(event1);
		
		Assert.assertFalse(this.testee.listen(events));
		Mockito.verifyZeroInteractions(this.repoFactory);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testListen() {
		AbstractEvent<String, Param<?>> event1 = Mockito.mock(AbstractEvent.class);
		Param e1Param = Mockito.mock(Param.class);
		Model e1Model = Mockito.mock(Model.class);
		ModelConfig e1ModelConfig = Mockito.mock(ModelConfig.class);
		Repo e1Repo = Mockito.mock(Repo.class);
		ModelPersistenceHandler handler = Mockito.mock(ModelPersistenceHandler.class);
		
		Mockito.when(event1.getPayload()).thenReturn(e1Param);
		Mockito.when(e1Param.getRootDomain()).thenReturn(e1Model);
		Mockito.when(e1Model.getConfig()).thenReturn(e1ModelConfig);
		Mockito.when(e1ModelConfig.getReferredClass()).thenReturn(PersistedEntity1.class);
		Mockito.when(e1ModelConfig.getAlias()).thenReturn("persistedEntity1");
		Mockito.when(e1ModelConfig.getRepo()).thenReturn(e1Repo);
		Mockito.when(this.repoFactory.getHandler(e1Repo)).thenReturn(handler);
		
		Mockito.when(event1.getType()).thenReturn(Action._get.toString());
		Mockito.when(e1Model.getPath()).thenReturn("/persistedEntity1");
		Mockito.when(e1Model.getAssociatedParam()).thenReturn(e1Param);
		
		List<AbstractEvent<String, Param<?>>> events = new ArrayList<>();
		events.add(event1);
		
		Assert.assertFalse(this.testee.listen(events));
		Mockito.verify(this.repoFactory).getHandler(e1Repo);
		
		// Validate expected events were handled 
		ArgumentCaptor<List> tempEventsCaptor = ArgumentCaptor.forClass(List.class);
		Mockito.verify(handler, Mockito.times(1)).handle(tempEventsCaptor.capture());
		List<ModelEvent<Param<?>>> tempEvents = tempEventsCaptor.getValue();
		Assert.assertEquals(1, tempEvents.size());
		ModelEvent<Param<?>> e1 = tempEvents.get(0);
		Assert.assertEquals(Action._get.toString(), e1.getType());
		Assert.assertEquals("/persistedEntity1", e1.getPath());
		Assert.assertEquals(e1Param, e1.getPayload());	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testListenOneEventPerDomainAlias() {
		AbstractEvent<String, Param<?>> event1 = Mockito.mock(AbstractEvent.class);
		Param e1Param = Mockito.mock(Param.class);
		Model e1Model = Mockito.mock(Model.class);
		ModelConfig e1ModelConfig = Mockito.mock(ModelConfig.class);
		Repo e1Repo = Mockito.mock(Repo.class);
		ModelPersistenceHandler handler = Mockito.mock(ModelPersistenceHandler.class);
		
		Mockito.when(event1.getPayload()).thenReturn(e1Param);
		Mockito.when(e1Param.getRootDomain()).thenReturn(e1Model);
		Mockito.when(e1Model.getConfig()).thenReturn(e1ModelConfig);
		Mockito.when(e1ModelConfig.getReferredClass()).thenReturn(PersistedEntity1.class);
		Mockito.when(e1ModelConfig.getAlias()).thenReturn("persistedEntity1");
		Mockito.when(e1ModelConfig.getRepo()).thenReturn(e1Repo);
		Mockito.when(this.repoFactory.getHandler(e1Repo)).thenReturn(handler);
		
		Mockito.when(event1.getType()).thenReturn(Action._get.toString());
		Mockito.when(e1Model.getPath()).thenReturn("/persistedEntity1");
		Mockito.when(e1Model.getAssociatedParam()).thenReturn(e1Param);
		
		List<AbstractEvent<String, Param<?>>> events = new ArrayList<>();
		events.add(event1);
		events.add(event1);
		
		Assert.assertFalse(this.testee.listen(events));
		Mockito.verify(this.repoFactory).getHandler(e1Repo);
		
		// Validate expected events were handled 
		ArgumentCaptor<List> tempEventsCaptor = ArgumentCaptor.forClass(List.class);
		Mockito.verify(handler, Mockito.times(1)).handle(tempEventsCaptor.capture());
		List<ModelEvent<Param<?>>> tempEvents = tempEventsCaptor.getValue();
		Assert.assertEquals(1, tempEvents.size());
		ModelEvent<Param<?>> e1 = tempEvents.get(0);
		Assert.assertEquals(Action._get.toString(), e1.getType());
		Assert.assertEquals("/persistedEntity1", e1.getPath());
		Assert.assertEquals(e1Param, e1.getPayload());
		
	}
}
