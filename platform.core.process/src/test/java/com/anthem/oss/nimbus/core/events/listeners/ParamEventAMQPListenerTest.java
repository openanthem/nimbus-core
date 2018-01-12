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
///**
// * 
// */
//package com.anthem.oss.nimbus.core.events.listeners;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//
//import java.util.Arrays;
//
//import org.apache.commons.lang.StringUtils;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.amqp.AmqpException;
//import org.springframework.amqp.rabbit.connection.Connection;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.anthem.oss.nimbus.core.domain.command.Command;
//import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
//import com.anthem.oss.nimbus.core.domain.definition.Domain;
//import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
//import com.anthem.oss.nimbus.core.domain.definition.Model;
//import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
//import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
//import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
//import com.anthem.oss.nimbus.core.domain.model.state.builder.DefaultQuadModelBuilder;
//import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;
//import com.anthem.oss.nimbus.core.integration.websocket.ParamEventAMQPListener;
//import com.anthem.oss.nimbus.core.session.UserEndpointSession;
//
//
//
///**
// * @author Swetha Vemuri
// *
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class ParamEventAMQPListenerTest extends TestModelFlowData {
//	
//	@Autowired
//	DefaultQuadModelBuilder quadModelBuilder;
//	
//	@Autowired
//	SimpMessagingTemplate messageTemplate;
//	
//	private static QuadModel<OrderBookFlow, Book> q;
//	
//	private static boolean done =false;
//	
//	@Autowired ParamEventAMQPListener listener ;
//	
//	@Before
//	public void t_init() {
//		
//		if(!done) {
//			quadModelBuilder.getDomainConfigApi().setBasePackages(
//					Arrays.asList(
//							ProcessFlow.class.getPackage().getName(),
//							Book.class.getPackage().getName(),
//							OrderBookFlow.class.getPackage().getName()
//							));
//			quadModelBuilder.getDomainConfigApi().reload();
//			done = true;
//		}
//			
//		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book/_new?b=$config").getCommand();
//		q = quadModelBuilder.build(cmd);
//		assertNotNull(q);
//
//		UserEndpointSession.setAttribute(cmd, q);	
//	}
//	
//	@Test
//	public void t01_createQuadModel_testdata(){
//		assertNotNull(q.getCore());
//		assertNotNull(q.getView());			
//	}
//	
//	@Test
//	public void tc02_shouldAllow_true_view() {
//		Param<?> p = q.getView().findParamByPath("/pg1/genre");
//		boolean shouldAllow = listener.shouldAllow(p);
//		assertNotNull(AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class));
//		assertEquals("view_book",AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class).value());	
//		assertNotNull(AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class));
//		Domain rootDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
//		assertEquals(1,Arrays.asList(rootDomain.includeListeners()).size());
//		Model pModel = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class);
//		assertEquals(0,Arrays.asList(pModel.excludeListeners()).size());
//		assertEquals(StringUtils.EMPTY,pModel.value());
//
//		assertTrue(shouldAllow);
//	}
//	
//	@Test
//	public void tc03_shouldAllow_false_core() {
//		Param<?> p = q.getCore().findParamByPath("/name");
//		boolean shouldAllow = listener.shouldAllow(p);
//		assertNotNull(AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class));
//		assertEquals("core_book",AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class).value());	
//		assertNotNull(AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class));
//		Domain rootDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
//		assertEquals(1,Arrays.asList(rootDomain.includeListeners()).size());
//		assertFalse(shouldAllow);
//	}
//	
//	
//	@Test
//	public void tc05_shouldAllow_false_no_websocketListener() {
//		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book1/_new?b=$config").getCommand();
//		QuadModel<OrderBook1Flow, BookWithoutPersistenceListener> q1 = quadModelBuilder.build(cmd);
//		assertNotNull(q1);
//		UserEndpointSession.setAttribute(cmd, q1);
//		boolean shouldAllow = listener.shouldAllow(q1.getView().findParamByPath("/orderDisplayId"));
//		assertFalse(shouldAllow);
//	}
//	
//	@Test
//	public void tc06_shouldAllow_false_excludedListener() {	
//		Command cmd = CommandBuilder.withUri("/xyz/admin/p/view_book3/_new?b=$config").getCommand();
//		QuadModel<OrderBook1Flow, BookWithoutPersistenceListener> q3 = quadModelBuilder.build(cmd);
//		assertNotNull(q3);
//		UserEndpointSession.setAttribute(cmd, q3);
//		Param<?> p = q3.getCore().findParamByPath("/title");
//		boolean shouldAllow = listener.shouldAllow(p);
//		assertNotNull(AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class));
//		assertEquals("core_book3",AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class).value());	
//		Domain rootDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
//		assertEquals(2,Arrays.asList(rootDomain.includeListeners()).size());
//		assertTrue(Arrays.asList(rootDomain.includeListeners()).contains(ListenerType.websocket));
//		
//		assertNotNull(AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class));		
//		Model pModel = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class);
//		assertEquals(2,Arrays.asList(pModel.excludeListeners()).size());
//		assertTrue(Arrays.asList(pModel.excludeListeners()).contains(ListenerType.websocket));
//		
//		assertFalse(shouldAllow);
//	}	
//	
//	@Test
//	public void tc07_shouldAllow_false_noRootDomain() {
//		fail("Cannot reach this scenario");
//	}
//	
//	@Test
//	public void tc08_listen_true(){
//		ModelEvent<Param<?>> event = new ModelEvent<>();
//		event.setType("_search");
//		event.setPayload(q.getCore().findParamByPath("/name"));
//		boolean listen = listener.listen(event);
//		assertNotNull(event.getPayload());	
//	}
//	
//	@Test
//	public void tc09_convertandsend() {
//		fail("Cannot reach this scenario - protected method");		
//	}
//	
//	private static class Recv {
//		private final static String QUEUE_NAME = "/queue/udpates";
//		ConnectionFactory factory = new ConnectionFactory() {
//			
//			@Override
//			public boolean removeConnectionListener(ConnectionListener listener) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public String getVirtualHost() {
//				return null;
//			}
//			
//			@Override
//			public String getUsername() {
//				return "guest";
//			}
//			
//			@Override
//			public int getPort() {
//				// TODO Auto-generated method stub
//				return 5672;
//			}
//			
//			@Override
//			public String getHost() {
//				return "localhost";
//			}
//			
//			@Override
//			public Connection createConnection() throws AmqpException {
//				return null;
//			}
//			
//			@Override
//			public void clearConnectionListeners() {
//				
//			}
//			
//			@Override
//			public void addConnectionListener(ConnectionListener listener) {
//				// TODO Auto-generated method stub
//				
//			}
//		};
//		
//		
//	}
//
//	
//}
