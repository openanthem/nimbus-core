/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.model.state;
///**
// * 
// */
//package com.anthem.oss.nimbus.core.domain.model.state;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertSame;
//
//import java.util.Arrays;
//import java.util.UUID;
//
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
//import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.anthem.oss.nimbus.core.domain.command.Command;
//import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
//import com.anthem.oss.nimbus.core.domain.model.config.builder.EntityConfigBuilder;
//import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
//import com.anthem.oss.nimbus.core.domain.model.state.builder.PageNavigationInitializer;
//import com.anthem.oss.nimbus.core.domain.model.state.builder.DefaultQuadModelBuilder;
//import com.anthem.oss.nimbus.core.domain.model.state.repo.DefaultParamStateRepositoryLocal;
//import com.anthem.oss.nimbus.core.entity.process.PageNode;
//import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;
//import com.anthem.oss.nimbus.core.rules.DefaultRulesEngineFactoryProducer;
//import com.anthem.oss.nimbus.core.session.UserEndpointSession;
//import com.anthem.oss.nimbus.core.utils.JavaBeanHandler;
//import com.anthem.oss.nimbus.test.sample.um.model.UMCase;
//import com.anthem.oss.nimbus.test.sample.um.model.view.UMCaseFlow;
//
//import test.com.anthem.nimbus.platform.spec.model.comamnd.TestCommandFactory;
//
///**
// * @author Soham Chakravarti
// *
// */
//@RunWith(SpringRunner.class)
//@EnableConfigurationProperties
//@ComponentScan(basePackageClasses={
//		DomainConfigBuilder.class, EntityConfigBuilder.class, DefaultQuadModelBuilder.class, PageNavigationInitializer.class, DefaultRulesEngineFactoryProducer.class,
//		DefaultParamStateRepositoryLocal.class, JavaBeanHandler.class})
//@ContextConfiguration(initializers=ConfigFileApplicationContextInitializer.class)
//@ImportAutoConfiguration(RefreshAutoConfiguration.class)
//@ActiveProfiles("local")
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class ProcessFlowCollectionsTest {
//	@Autowired DefaultQuadModelBuilder quadModelBuilder;
//	
//	private static boolean done = false;
//	
//	@Before
//	public void t_init() {
//		if(!done) {
//			quadModelBuilder.getDomainConfigApi().setBasePackages(
//					Arrays.asList(
//							ProcessFlow.class.getPackage().getName(),
//							UMCase.class.getPackage().getName(),
//							UMCaseFlow.class.getPackage().getName()
//							));
//			quadModelBuilder.getDomainConfigApi().reload();
//			done = true;
//		}
//		
//		Command cmd = TestCommandFactory.create_view_icr_UMCaseFlow();
//		//QuadModel<UMCaseFlow, UMCase> q = quadModelBuilder.build(cmd, (mConfig)->ModelsTemplate.newInstance(mConfig.getReferredClass()));
//		QuadModel<UMCaseFlow, UMCase> q = quadModelBuilder.build(cmd);
//		assertNotNull(q);
//		
//		UserEndpointSession.setAttribute(cmd, q);
//	}
//	
//	@Test
//	public void tc01_sanity_check_core_builders() {
//		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
//		
//        @SuppressWarnings("unchecked")
//		ListParam<PageNode> param = q.getFlow().findParamByPath("/pageNavigation/pageNodes").findIfCollection();
//        
//        final String pageId = "T_PAGE_ID";
//        final String pageNm = "T_PAGE_NM";
//        
//        PageNode pageNode = new PageNode();
//        pageNode.setId(UUID.randomUUID().toString());
//        pageNode.setPageId(pageId);
//        pageNode.setPageName(pageNm);
//        
//        param.add(pageNode);
//        
//        assertNotNull(q.getRoot().getState().getFlow());
//        assertNotNull(q.getRoot().getState().getFlow().getPageNavigation());
//        assertNotNull(q.getRoot().getState().getFlow().getPageNavigation().getPageNodes());
//        assertSame(1, q.getRoot().getState().getFlow().getPageNavigation().getPageNodes().size());
//	}
//}
