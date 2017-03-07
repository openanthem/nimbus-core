/**
 * 
 */
package com.anthem.nimbus.platform.core.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.anthem.nimbus.platform.core.process.api.AbstractPlatformIntegrationTests;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadScopedEventListener;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;
import com.anthem.oss.nimbus.test.sample.um.model.ServiceLine;
import com.anthem.oss.nimbus.test.sample.um.model.UMCase;
import com.anthem.oss.nimbus.test.sample.um.model.view.UMCaseFlow;

import test.com.anthem.nimbus.platform.spec.contract.event.MockEventListener;
import test.com.anthem.nimbus.platform.spec.model.comamnd.TestCommandFactory;

/**
 * Test class to verify the event emitting functionality on setState
 * 
 * @author Swetha Vemuri
 *
 */
@EnableAutoConfiguration
@ContextConfiguration(classes = { MockEventListener.class })
public class EmitStateAndConfigEventListenerTest extends AbstractPlatformIntegrationTests {

	@Autowired
	QuadModelBuilder quadModelBuilder;
	
	private static QuadModel<UMCaseFlow, UMCase> q;

	@Before
	public void t_init() {
		Command cmd = TestCommandFactory.create_view_icr_UMCaseFlow();
		q = quadModelBuilder.build(cmd);
		assertNotNull(q);

		UserEndpointSession.setAttribute(cmd, q);
	}

	@After
	public void t_teardown() {
		q.getRoot().getProvider().getEventListener();
		QuadScopedEventListener qEventListener = (QuadScopedEventListener) q.getRoot().getProvider().getEventListener();
		StateAndConfigEventListener e = qEventListener.getParamEventListeners().stream()
				.filter(listener -> listener instanceof MockEventListener).findFirst().get();
		assertTrue(e instanceof MockEventListener);
		MockEventListener m = (MockEventListener) e;

		m.flushEvents();
		UserEndpointSession.clearSession();
	}
	
	@Test
	public void tc01_emitEvent_mappedentity_view() {
		Param<String> caseType =q.getView().findParamByPath("/pg3/aloha");
		caseType.setState("medical");
		MockEventListener m = getMockEventListener();
		assertEquals(2,m.getModelEvent().size());
		
		ModelEvent<Param<?>> me_core =retrieveModelEventByPath(m.getModelEvent(), "/caseType");	
		assertNotNull(me_core); 
		assertTrue(StringUtils.equalsIgnoreCase("/caseType", me_core.getPath()));
		
		ModelEvent<Param<?>> me_view =retrieveModelEventByPath(m.getModelEvent(), "/pg3/aloha");	
		assertNotNull(me_view); // there shoud be one event with /pg3/aloha
		assertTrue(StringUtils.equalsIgnoreCase("/pg3/aloha", me_view.getPath()));
		assertTrue(StringUtils.equalsIgnoreCase(me_view.getType(),"_replace"));		
	}
	
	@Test
	public void tc02_emitEvent_non_mappedentity_view() {
		Param<String> mappedCaseId =q.getView().findParamByPath("/pg3/mappedCaseId");
		mappedCaseId.setState("CASE1");
		MockEventListener m = getMockEventListener();
		assertEquals(1,m.getModelEvent().size());
		
		ModelEvent<Param<?>> me_view =retrieveModelEventByPath(m.getModelEvent(), "/pg3/mappedCaseId");	
		assertNotNull(me_view); // there shoud be one event with /pg3/aloha
		assertTrue(StringUtils.equalsIgnoreCase("/pg3/mappedCaseId", me_view.getPath()));
		assertTrue(StringUtils.equalsIgnoreCase(me_view.getType(),"_replace"));		
	}

	@Test
	public void tc03_setState_add_to_collectionEntity() {
		Param<List<ServiceLine>> pSAC_col = q.getCore().findParamByPath("/serviceLines");

		List<ServiceLine> sl_list = new ArrayList<>();
		ServiceLine sl1 = new ServiceLine();
		sl1.setElemId("sl_1");
		sl_list.add(sl1);
		ServiceLine sl2 = new ServiceLine();
		sl2.setElemId("sl_2");
		sl_list.add(sl2);
		pSAC_col.setState(sl_list);

		MockEventListener m = getMockEventListener();
		ModelEvent<Param<?>> me_1 = retrieveModelEventByPath(m.getModelEvent(), "/serviceLines/0");
		assertNotNull(me_1);
		assertTrue(StringUtils.equalsIgnoreCase("/serviceLines/0", me_1.getPath()));

		ModelEvent<Param<?>> me_2 = retrieveModelEventByPath(m.getModelEvent(), "/serviceLines/1");
		assertNotNull(me_2);
		assertTrue(StringUtils.equalsIgnoreCase("/serviceLines/1", me_2.getPath()));

		ModelEvent<Param<?>> me_3 = retrieveModelEventByPath(m.getModelEvent(), "/serviceLines");
		assertNotNull(me_3);
		assertTrue(StringUtils.equalsIgnoreCase("/serviceLines", me_3.getPath()));

		ModelEvent<Param<?>> me_4 = retrieveModelEventByPath(m.getModelEvent(),
				"/pg3/noConversionAttachedColServiceLines");
		assertNotNull(me_4);
		assertTrue(StringUtils.equalsIgnoreCase("/pg3/noConversionAttachedColServiceLines", me_4.getPath()));

		ModelEvent<Param<?>> me_5 = retrieveModelEventByPath(m.getModelEvent(), "/main/vCardServiceLines");
		assertNotNull(me_5);
		assertTrue(StringUtils.equalsIgnoreCase("/main/vCardServiceLines", me_5.getPath()));
		
		assertEquals(5, m.getModelEvent().size());
	}

	@Test
	public void tc04_setState_update_from_collectionEntity() {
		Param<List<ServiceLine>> pSAC_col = q.getCore().findParamByPath("/serviceLines");

		List<ServiceLine> sl_list = new ArrayList<>();
		ServiceLine sl_1 = new ServiceLine();
		sl_1.setElemId("sl_1");
		sl_list.add(sl_1);
		pSAC_col.setState(sl_list);
		
		Param<ServiceLine> pSAC_col_elem_1 = q.getCore().findParamByPath("/serviceLines/1");
		// initially null - since it has not yet been added to the collection
		assertNull(pSAC_col_elem_1);
		Param<List<ServiceLine>> pSAC_col_update = q.getCore().findParamByPath("/serviceLines");
		ListParam<ServiceLine> lpServiceLines = pSAC_col_update.findIfCollection();
		assertNotNull(lpServiceLines);
		
		ServiceLine sl_2 = new ServiceLine();
		sl_2.setElemId("sl_2");
		lpServiceLines.add(sl_2);

		/*
		 * After selectively updating one element in a collection: Events
		 * emitted: /serviceLines/1, /pg3/noConversionAttachedColServiceLines/1,
		 * /main/vCardServiceLines/1
		 */
		MockEventListener m = getMockEventListener();
		ModelEvent<Param<?>> me_4 = retrieveModelEventByPath(m.getModelEvent(),
				"/pg3/noConversionAttachedColServiceLines/1");
		assertNotNull(me_4);
		assertTrue(StringUtils.equalsIgnoreCase("/pg3/noConversionAttachedColServiceLines/1", me_4.getPath()));

		ModelEvent<Param<?>> me_5 = retrieveModelEventByPath(m.getModelEvent(), "/serviceLines/1");
		assertNotNull(me_5);
		assertTrue(StringUtils.equalsIgnoreCase("/serviceLines/1", me_5.getPath()));

		ModelEvent<Param<?>> me_6 = retrieveModelEventByPath(m.getModelEvent(), "/main/vCardServiceLines");
		assertNotNull(me_6);
		assertTrue(StringUtils.equalsIgnoreCase("/main/vCardServiceLines", me_6.getPath()));

		assertEquals(7, m.getModelEvent().size());
	}
	
	private ModelEvent<Param<?>> retrieveModelEventByPath(List<ModelEvent<Param<?>>> eventList, String path) {
		
		ModelEvent<Param<?>> me = eventList.stream()
				.filter(modelEvent -> StringUtils.equalsIgnoreCase(modelEvent.getPath(), path)).findFirst().get();

		return me;
	}
	
	private MockEventListener getMockEventListener() {
		q.getRoot().getProvider().getEventListener();
		QuadScopedEventListener qEventListener = (QuadScopedEventListener) q.getRoot().getProvider().getEventListener();
		StateAndConfigEventListener e = qEventListener.getParamEventListeners().stream()
				.filter(listener -> listener instanceof MockEventListener).findFirst().get();
		assertTrue(e instanceof MockEventListener);
		MockEventListener m = (MockEventListener) e;
		assertNotNull(m);
		assertNotNull(m.getModelEvent());
		return m;
	}
}
