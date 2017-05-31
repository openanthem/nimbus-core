/**
 * 
 */
package com.anthem.oss.nimbus.core.events.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.DefaultQuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.internal.AbstractEvent.PersistenceMode;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ParamStateAtomicPersistenceEventListener;
import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * TestClass to test the functionality of ParamStatePersistenceEventListener.java and ParamStateAtomicPersistenceEventListener.java
 * 
 * @author Swetha Vemuri
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParamStateAtomicPersistenceEventListenerTest extends TestModelFlowData{
	
	@Autowired
	DefaultQuadModelBuilder quadModelBuilder;
	
	private static QuadModel<OrderBookFlow, Book> q;
	
	private static boolean done =false;
	
	@Autowired ParamStateAtomicPersistenceEventListener listener ;

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
	
	@Test
	public void t01_createQuadModel_testdata(){
		assertNotNull(q.getCore());
		assertNotNull(q.getView());			
	}
	
	@Test
	public void tc02_shouldAllow_true_core() {
		Param<?> p = q.getCore().findParamByPath("/name");
		boolean shouldAllow = listener.shouldAllow(p);
		assertNotNull(AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class));
		assertEquals("core_book",AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class).value());	
		assertNotNull(AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class));
		assertEquals("core_book",AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class).value());			
		Domain rootDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
		assertEquals(1,Arrays.asList(rootDomain.includeListeners()).size());
		assertTrue(shouldAllow);
	}
	
	@Test
	public void tc03_shouldAllow_false_view() {
		Param<?> p = q.getView().findParamByPath("/pg1/genre");
		boolean shouldAllow = listener.shouldAllow(p);
		assertNotNull(AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class));
		assertEquals("view_book",AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class).value());	
		assertNotNull(AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class));
		Model pModel = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class);
		assertEquals(0,Arrays.asList(pModel.excludeListeners()).size());
		assertEquals(StringUtils.EMPTY,pModel.value());
		Domain rootDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
		assertEquals(1,Arrays.asList(rootDomain.includeListeners()).size());
		assertFalse(shouldAllow);
	}
	
	@Test
	public void tc04_shouldAllow_false_view_notMapped() {
		boolean shouldAllow = listener.shouldAllow(q.getView().findParamByPath("/pg1/isbn"));
		assertFalse(shouldAllow);
	}
	
	@Test
	public void tc05_shouldAllow_false_no_persistenceListener() {
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book1/_new?b=$config").getCommand();
		QuadModel<OrderBook1Flow, BookWithoutPersistenceListener> q1 = quadModelBuilder.build(cmd);
		assertNotNull(q1);
		UserEndpointSession.setAttribute(cmd, q1);
		boolean shouldAllow = listener.shouldAllow(q1.getCore().findParamByPath("/title"));
		assertFalse(shouldAllow);
	}
	
	@Test
	public void tc06_shouldAllow_false_excludedListener() {
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book3/_new?b=$config").getCommand();
		QuadModel<OrderBook3Flow, BookWithExcludedListener> q2 = quadModelBuilder.build(cmd);
		assertNotNull(q2);
		UserEndpointSession.setAttribute(cmd, q2);
		boolean shouldAllow = listener.shouldAllow(q2.getCore().findParamByPath("/title"));
		assertFalse(shouldAllow);
	}	
	
	@Test(expected =NullPointerException.class)
	public void tc07_shouldAllow_false_noRootDomain() {
		Param<?> p = q.getCore().findParamByPath("/pg1/genre");
		boolean shouldAllow = listener.shouldAllow(p);
		assertNull(AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class));
		assertFalse(shouldAllow);
	}
	
	@Test
	public void tc08_shouldAllow_false_mode_batch() {
		listener.setMode(PersistenceMode.BATCH);
		boolean shouldAllow = listener.shouldAllow(q.getCore().findParamByPath("/name"));
		assertFalse(shouldAllow);
	}
	
	@Ignore
	public void tc09_listen_true_repo() {
		ModelEvent<Param<?>> event = new ModelEvent<>();
		event.setType("_search");
		event.setPayload(q.getCore().findParamByPath("/name"));
		boolean listen = listener.listen(event);
		assertNotNull(event.getPayload().getRootDomain().getConfig().getRepo());
		assertEquals(Repo.Database.rep_mongodb,event.getPayload().getRootDomain().getConfig().getRepo());				
	}
	
	@Test
	public void tc09_shouldAllow_true_mode_atomic() {
		listener.setMode(PersistenceMode.ATOMIC);
		boolean shouldAllow = listener.shouldAllow(q.getCore().findParamByPath("/name"));
		assertTrue(shouldAllow);
	}
	
	@Test(expected = InvalidConfigException.class)
	public void tc10_listen_false_null_repo() {
		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book2/_new?b=$config").getCommand();
		QuadModel<OrderBook2Flow, BookWithoutRepo> q2 = quadModelBuilder.build(cmd);
		assertNotNull(q2);
		UserEndpointSession.setAttribute(cmd, q2);
		ModelEvent<Param<?>> event = new ModelEvent<>();
		event.setType("_new");
		event.setPayload(q2.getCore().findParamByPath("/title"));
		assertNull(event.getPayload().getRootDomain().getConfig().getRepo());
		listener.listen(event);	
	}
	
	@Test
	public void tc11_listen_false_null_repohandler() {
		fail("Cannot reach this scenario");
	}
	
}
