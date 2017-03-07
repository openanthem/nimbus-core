/**
 * 
 */
package com.anthem.oss.nimbus.core.events.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

import com.anthem.nimbus.platform.core.process.api.AbstractPlatformIntegrationTests;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadScopedEventListener;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;
import com.anthem.oss.nimbus.core.events.listeners.TestModelFlowData.Book;
import com.anthem.oss.nimbus.core.events.listeners.TestModelFlowData.Book.Publisher;
import com.anthem.oss.nimbus.core.events.listeners.TestModelFlowData.OrderBookFlow;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;

import test.com.anthem.nimbus.platform.spec.contract.event.MockEventListener;

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
	
	private static QuadModel<OrderBookFlow, Book> q;
	
	private static boolean done =false;

	@Before
	public void t_init() {
		if(!done) {
			quadModelBuilder.getDomainConfigApi().setBasePackages(
					Arrays.asList(
							ProcessFlow.class.getPackage().getName(),
							Book.class.getPackage().getName(),
							OrderBookFlow.class.getPackage().getName()
							));
			quadModelBuilder.getDomainConfigApi().reload();
			done = true;
		}
			
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_new?b=$config").getCommand();
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
		Param<String> genre =q.getView().findParamByPath("/pg1/genre");
		genre.setState("fiction");
		MockEventListener m = getMockEventListener();
		assertEquals(2,m.getModelEvent().size());
		
		ModelEvent<Param<?>> me_core =retrieveModelEventByPath(m.getModelEvent(), "/category");	
		assertNotNull(me_core); 
		assertTrue(StringUtils.equalsIgnoreCase("/category", me_core.getPath()));
		
		ModelEvent<Param<?>> me_view =retrieveModelEventByPath(m.getModelEvent(), "/pg1/genre");	
		assertNotNull(me_view); // there shoud be one event with /pg1/genre
		assertTrue(StringUtils.equalsIgnoreCase("/pg1/genre", me_view.getPath()));
		assertTrue(StringUtils.equalsIgnoreCase(me_view.getType(),"_replace"));		
	}
	
	@Test
	public void tc02_emitEvent_non_mappedentity_view() {
		Param<String> nonMappedEntity =q.getView().findParamByPath("/pg1/isbn");
		nonMappedEntity.setState("123451abc");
		MockEventListener m = getMockEventListener();
		assertEquals(1,m.getModelEvent().size());
		
		ModelEvent<Param<?>> me_view =retrieveModelEventByPath(m.getModelEvent(), "/pg1/isbn");	
		assertNotNull(me_view); // there shoud be one event with /pg3/aloha
		assertTrue(StringUtils.equalsIgnoreCase("/pg1/isbn", me_view.getPath()));
		assertTrue(StringUtils.equalsIgnoreCase(me_view.getType(),"_replace"));		
	}

	@Test
	public void tc03_setState_add_to_collectionEntity() {
		Param<List<Publisher>> pSAC_col = q.getCore().findParamByPath("/publishers");

		List<Publisher> publisher_lst = new ArrayList<>();
		Publisher p_1 = new Publisher();
		p_1.setName("penguin");
		publisher_lst.add(p_1);
		Publisher p_2 = new Publisher();
		p_2.setCode("JKB");
		publisher_lst.add(p_2);
		pSAC_col.setState(publisher_lst);

		MockEventListener m = getMockEventListener();
		ModelEvent<Param<?>> me_1 = retrieveModelEventByPath(m.getModelEvent(), "/publishers/0");
		assertNotNull(me_1);
		assertTrue(StringUtils.equalsIgnoreCase("/publishers/0", me_1.getPath()));

		ModelEvent<Param<?>> me_2 = retrieveModelEventByPath(m.getModelEvent(), "/publishers/1");
		assertNotNull(me_2);
		assertTrue(StringUtils.equalsIgnoreCase("/publishers/1", me_2.getPath()));

		ModelEvent<Param<?>> me_3 = retrieveModelEventByPath(m.getModelEvent(), "/publishers");
		assertNotNull(me_3);
		assertTrue(StringUtils.equalsIgnoreCase("/publishers", me_3.getPath()));

		ModelEvent<Param<?>> me_4 = retrieveModelEventByPath(m.getModelEvent(),
				"/pg1/publishingHouse");
		assertNotNull(me_4);
		assertTrue(StringUtils.equalsIgnoreCase("/pg1/publishingHouse", me_4.getPath()));
		assertEquals(4, m.getModelEvent().size());
	}

	@Test
	public void tc04_setState_update_from_collectionEntity() {
		Param<List<Publisher>> pSAC_col = q.getCore().findParamByPath("/publishers");

		List<Publisher> publisher_lst = new ArrayList<>();
		Publisher p_1 = new Publisher();
		p_1.setName("penguin");
		publisher_lst.add(p_1);
		pSAC_col.setState(publisher_lst);
		
		Param<Publisher> pSAC_col_elem_1 = q.getCore().findParamByPath("/publishers/1");
		// initially null - since it has not yet been added to the collection
		assertNull(pSAC_col_elem_1);
		Param<List<Publisher>> pSAC_col_update = q.getCore().findParamByPath("/publishers");
		ListParam<Publisher> lp_publishers = pSAC_col_update.findIfCollection();
		assertNotNull(lp_publishers);
		
		Publisher p_2 = new Publisher();
		p_2.setName("JKRowling");
		lp_publishers.add(p_2);

		MockEventListener m = getMockEventListener();
		ModelEvent<Param<?>> me_4 = retrieveModelEventByPath(m.getModelEvent(),
				"/pg1/publishingHouse/1");
		assertNotNull(me_4);
		assertTrue(StringUtils.equalsIgnoreCase("/pg1/publishingHouse/1", me_4.getPath()));

		ModelEvent<Param<?>> me_5 = retrieveModelEventByPath(m.getModelEvent(), "/publishers/1");
		assertNotNull(me_5);
		assertTrue(StringUtils.equalsIgnoreCase("/publishers/1", me_5.getPath()));
		
		assertEquals(5, m.getModelEvent().size());
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
