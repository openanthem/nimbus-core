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
package com.antheminc.oss.nimbus.domain.model.state.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfigType;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfigType.CollectionType;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListElemParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListModel;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.StateType;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultListParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.test.FrameworkIntegrationTestScenariosApplication;
import com.antheminc.oss.nimbus.test.scenarios.s3.core.ServiceLine;
import com.antheminc.oss.nimbus.test.scenarios.s3.core.ServiceLine.AuditInfo;
import com.antheminc.oss.nimbus.test.scenarios.s3.core.SimpleCase;
import com.antheminc.oss.nimbus.test.scenarios.s3.view.Page_Pg3.Section_ServiceLine;
import com.antheminc.oss.nimbus.test.scenarios.s3.view.VRSimpleCaseFlow;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=FrameworkIntegrationTestScenariosApplication.class)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuadModelCollectionsTest {
	
	@Autowired QuadModelBuilder quadModelBuilder;
	@Autowired SessionProvider sessionProvider;
	
	public static Command create_view_main() {

		Command c = new Command("/hooli/comm/thebox/p/view_simplecase/_new");

		c.createRoot(Type.ClientAlias, "hooli")
			.createNext(Type.ClientOrgAlias, "compression")
			.createNext(Type.AppAlias, "icr")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "view_simplecase")
			;//.createNext(Type.Action, Action._new.name());
		c.setAction(Action._new);
		c.templateBehaviors().add(Behavior.$config);
		return c;
	}
	
	@Before
	public void before() {
		Command cmd = create_view_main();
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = quadModelBuilder.build(cmd);
		assertNotNull(q);
		
		sessionProvider.setAttribute(cmd, q);
	}
	
	@Test
	public void t0_mapstoPath() {
		Long id = 1L;
		SimpleCase core = new SimpleCase();
		core.setId(id);
		
		Command cmd = create_view_main();
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = quadModelBuilder.build(cmd, new ExecutionEntity<>(new VRSimpleCaseFlow(), core));
		
		Param<String> coreId = q.getView().findParamByPath("/.m/id");
		assertSame(id, coreId.getState());
	}
	
	@Test
	public void t0_configState() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		Param<?> pAloha = q.getView().findParamByPath("/pg3/aloha");
		assertNotNull(pAloha);
		assertTrue(pAloha.isVisible());
		
		pAloha.setVisible(false);
		
//		Param<Boolean> mAloha_enabled = mAloha_runtime.findParamByPath("/enabled");
//		assertNotNull(mAloha_enabled);
//		assertTrue(mAloha_enabled.getState());
//		
//		mAloha_enabled.setState(false);
//		assertFalse(mAloha_enabled.getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t1_leafstates() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		ServiceLine sl = new ServiceLine();
		sl.setService("Karma");
		
		final DefaultListParamState<ServiceLine> serviceLines = (DefaultListParamState<ServiceLine>) q.getCore().findParamByPath("/serviceLines").findIfCollection();
		serviceLines.add(sl);
		
		SimpleCase core = q.getCore().getState();
		SimpleCase leaf = q.getCore().getLeafState();
		
		assertNotNull(core);
		assertNotNull(leaf);
		
		assertNotNull(core.getServiceLines());
		assertNotNull(leaf.getServiceLines());
		
		assertEquals(sl, core.getServiceLines().get(0));
		
		assertSame(sl.getService(), q.getCore().findParamByPath("/serviceLines/0/service").getState());
		
		List<ServiceLine> leafServiceLines = q.getCore().<List<ServiceLine>>findParamByPath("/serviceLines").getLeafState(); 
		assertNotNull(leafServiceLines);
		assertNotNull(leafServiceLines.get(0));
		assertSame(sl.getService(), leafServiceLines.get(0).getService());
		
		
		assertSame(sl.getService(), q.getCore().findParamByPath("/serviceLines/0/service").getLeafState());
		assertSame(sl.getService(), q.getCore().<ServiceLine>findParamByPath("/serviceLines/0").getLeafState().getService());
	}

	
	@Test
	public void tc01_sanity_check_core_builders() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		// ParamSAC
		Param<List<ServiceLine>> pSAC_col = q.getCore().findParamByPath("/serviceLines");
		assertNotNull(pSAC_col);
		assertEquals("/core_simplecase/serviceLines", pSAC_col.getPath());
		
		assertTrue(pSAC_col.getType().isNested());
		assertTrue(pSAC_col.getType().isCollection());
		assertTrue(pSAC_col.getType() instanceof StateType.NestedCollection);
		
		// ParamSAC: TypeSAC
		StateType.NestedCollection<ServiceLine> pSAC_col_type = pSAC_col.getType().findIfCollection(); 
		
		// ParamSAC: TypeSAC.NestedCollection => SAC Type for List<ServiceLine>
		ListModel<ServiceLine> mSAC_col = pSAC_col_type.getModel();
		assertNotNull(mSAC_col);
		
		// ParamSAC: TypeSAC.NestedCollection.Config => ModelConfig for List<ServiceLine
		ModelConfig<List<ServiceLine>> mSAC_col_config = mSAC_col.getConfig(); 
		assertNotNull(mSAC_col_config);
		
		assertTrue(List.class.isAssignableFrom(mSAC_col_config.getReferredClass()));
		
		// ParamSAC: ParamConfig => Config for List<ServiceLine>
		ParamConfig<List<ServiceLine>> pSAC_config =  pSAC_col.getConfig();
		assertNotNull(pSAC_config);
		assertTrue(List.class.isAssignableFrom(pSAC_config.getReferredClass()));
		
		assertTrue(pSAC_config.getType().isNested());
		assertTrue(pSAC_config.getType().isCollection());
		assertTrue(pSAC_config.getType() instanceof ParamConfigType.NestedCollection);
		
		// ParamSAC: ParamConfig.Type.NestedCollection => ParamType for List<ServiceLine>
		ParamConfigType.NestedCollection<ServiceLine> pSAC_config_type = pSAC_config.getType().findIfCollection(); 
		assertSame(pSAC_config_type.getModelConfig(), mSAC_col_config);
		assertSame(CollectionType.list, pSAC_config_type.getCollectionType());
		
		// list param element
		assertNotNull(pSAC_config_type.getElementConfig());
		assertTrue(pSAC_config_type.getElementConfig().getType().isNested());
		assertFalse(pSAC_config_type.getElementConfig().getType().isCollection());

		// list param element: type
		ParamConfigType.Nested<ServiceLine> typNestedColElem = pSAC_config_type.getElementConfig().getType().findIfNested();
		assertSame(ServiceLine.class, typNestedColElem.getReferredClass());
		
		//ParamType
		// param sac : ListParam
		assertTrue(ListParam.class.isAssignableFrom(pSAC_col.getClass()));
		
		// array-{primitive}
		Param<String[]> pArrayString_types = q.getCore().findParamByPath("/types");
		assertNotNull(pArrayString_types);
		assertEquals("array-string", pArrayString_types.getConfig().getType().getName());
		assertNull(pArrayString_types.getState());
		
		String[] types = new String[]{"ONE", "TWO"};
		
		// set
		pArrayString_types.setState(types);
		assertSame(types, pArrayString_types.getState());
		assertSame(types, pArrayString_types.getLeafState());
		
	}
	
	@Test
	public void tc02_sanity_check_core_ops() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		assertNotNull(q.getCore());
		// ParamSAC
		Param<List<ServiceLine>> pSAC_col = q.getCore().findParamByPath("/serviceLines");
		assertNotNull(pSAC_col);
		
		assertTrue(pSAC_col.getType().isCollection());
		ListParam<ServiceLine> lpServiceLines = pSAC_col.findIfCollection();
		
		// initially is null
		assertNull(lpServiceLines.getState());
		
		ServiceLine sl_0 = new ServiceLine();
		lpServiceLines.add(sl_0);
		
		assertSame(1, lpServiceLines.size());
		assertNotNull(lpServiceLines.getState());
		
		SimpleCase core = q.getCore().getState();
		
		assertNotNull(core);
		assertEquals(sl_0, core.getServiceLines().get(0));
		
		Param<ServiceLine> p_sl_0 = q.getCore().findParamByPath("/serviceLines/0");
		assertNotNull(p_sl_0);
		assertEquals(sl_0, p_sl_0.getState());
	}
	
	@Test
	public void tc03_col_c_add() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		assertNotNull(q.getCore());

		//add to list
		ServiceLine elem = new ServiceLine();
		@SuppressWarnings("unchecked")
		final DefaultListParamState<ServiceLine> serviceLines = (DefaultListParamState<ServiceLine>) 
				q.getCore().findParamByPath("/serviceLines").findIfCollection();
				
		serviceLines.add(elem);
		
		assertSame(1, q.getCore().getState().getServiceLines().size());
		assertEquals(elem, q.getCore().getState().getServiceLines().get(0));
		assertEquals(elem, q.getCore().findParamByPath("/serviceLines/0").findIfCollectionElem().getState());
		assertEquals(elem, q.getCore().findParamByPath("/serviceLines/0").getState());
	}
	
	@Test
	public void tc04_col_c_getList() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		assertNotNull(q.getCore());
		
		//get list
		assertNull(q.getCore().findParamByPath("/serviceLines").getState());
	}
	
	@Test
	public void tc05_col_c_setList() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		//set list
		List<ServiceLine> newList = new ArrayList<>();
		
		ServiceLine sl_0 = new ServiceLine();
		newList.add(sl_0);
		
		ServiceLine sl_1 = new ServiceLine();
		newList.add(sl_1);
		
		//set
		final DefaultParamState<Object> serviceLines = (DefaultParamState<Object>) q.getCore().findParamByPath("/serviceLines");
		
		serviceLines.setState(newList);
		SimpleCase core = q.getCore().getState();
		
		assertNotSame(newList, q.getCore().findParamByPath("/serviceLines").getState());
		assertNotSame(core.getServiceLines(), newList);
		assertSame(2, q.getCore().findParamByPath("/serviceLines").findIfCollection().size());
		assertSame(2, q.getCore().findParamByPath("/serviceLines").findIfCollection().getType().findIfCollection().getModel().getParams().size());
		
		assertEquals(sl_0, q.getCore().findParamByPath("/serviceLines/0").getState());
		assertEquals(sl_1, q.getCore().findParamByPath("/serviceLines/1").getState());
		
		AtomicInteger counter = new AtomicInteger(0);
		q.getCore().templateParams().find("serviceLines").getType().findIfCollection().getModel().getParams()
			.stream()
			.sequential()
			.peek(p->{
				assertEquals(newList.get(counter.getAndIncrement()), p.getState());
			});
	}
	
	@Test
	public void tc06_col_c_add_nestedSet() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		ServiceLine elem = new ServiceLine();
		
		@SuppressWarnings("unchecked")
		final DefaultListParamState<ServiceLine> serviceLines = 
			(DefaultListParamState<ServiceLine>) q.getCore().findParamByPath("/serviceLines").findIfCollection();
		
		serviceLines.add(elem);
		SimpleCase core = q.getCore().getState();
		
		assertEquals(elem, core.getServiceLines().get(0));
		assertEquals(elem, q.getCore().findParamByPath("/serviceLines/0").getState());
		
		AuditInfo a0 = new AuditInfo();
		a0.setBy("Me");
		a0.setWhen(new Date());
		a0.setWhy("just so");
		
		ListParam<AuditInfo> p_a = q.getCore().findParamByPath("/serviceLines/0/discharge/audits").findIfCollection();
		assertNotNull(p_a);
		assertEquals("/core_simplecase/serviceLines/0/discharge/audits", p_a.getPath());
		
		p_a.add(a0);
		assertEquals("/core_simplecase/serviceLines/0/discharge/audits/0", p_a.findParamByPath("/0").getPath());
		
		assertEquals(a0, core.getServiceLines().get(0).getDischarge().getAudits().get(0));
	}
	
	@Test
	public void tv01_sanity_check_view_builders() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		Model<VRSimpleCaseFlow> vUMCase = ((Model<VRSimpleCaseFlow>)q.getView());
		assertNotNull(vUMCase);
		
		assertNull(vUMCase.getState());
		
		//if added to core via SAC, should reflect in corresponding view SAC
		
		// ensure that view collection is not initialized yet
		assertNull(q.getView().getState());
		
		Param<ServiceLine> vpOneServiceLine = q.getView().findParamByPath("/pg3/coreAttachedOneServiceLine");
		assertNotNull(vpOneServiceLine);
		assertTrue(vpOneServiceLine.isMapped());
		assertTrue(vpOneServiceLine.isNested());
		assertFalse(vpOneServiceLine.isCollection());
		assertFalse(vpOneServiceLine.isCollectionElem());
		assertEquals("/oneServiceLine", vpOneServiceLine.getConfig().findIfMapped().getPath().value());
		
		// set value and check in mapsTo core
		ServiceLine vp_casl_n = new ServiceLine();
		vp_casl_n.setService("It's elementary Watson!");
		
		vpOneServiceLine.setState(vp_casl_n);
		
		assertNotNull(vpOneServiceLine.getState());
		assertSame(vp_casl_n.getService(), q.getCore().getState().getOneServiceLine().getService());
		
		Param<ServiceLine> cpOneServiceLine = q.getCore().findParamByPath("/oneServiceLine");
		assertNotNull(cpOneServiceLine);
		assertFalse(cpOneServiceLine.isMapped());
		assertTrue(cpOneServiceLine.isNested());
		assertFalse(cpOneServiceLine.isCollection());
		assertFalse(cpOneServiceLine.isCollectionElem());
		
		assertEquals(vp_casl_n, cpOneServiceLine.getState());
		assertSame(vpOneServiceLine.getState(), cpOneServiceLine.getState());
		
		Param<ServiceLine> mappedService = q.getView().findParamByPath("/pg3/coreAttachedOneServiceLine/service");
		Param<ServiceLine> mapsToService = q.getCore().findParamByPath("/oneServiceLine/service");
		assertNotSame(mappedService, mapsToService);
		assertSame(mappedService.getState(), mapsToService.getState());
		assertEquals("/view_simplecase/pg3/coreAttachedOneServiceLine/service", mappedService.getPath());
	}
	
	@Test
	public void tv02_leaf_attached_v2c_noConversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		String _ALOHA = "View Says: ALOHA";
		final DefaultParamState<Object>	viewParam =	(DefaultParamState<Object>) q.getView().findParamByPath("/pg3/aloha");
		
		viewParam.setState(_ALOHA);
		
		assertSame(_ALOHA, q.getCore().getState().getCaseType());
		assertNotNull(((Model<VRSimpleCaseFlow>)q.getView()).getState().getPg3());
		assertNull(((Model<VRSimpleCaseFlow>)q.getView()).getState().getPg3().getAloha());		//coz view leaf param is mapped
		assertSame(_ALOHA, q.getCore().findParamByPath("/caseType").getState());
	}
	
	@Test 
	public void tv03_leaf_attached_c2v_noConversion() throws Exception{
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		String _ALOHA = "Core Says: ALOHA";
		final DefaultParamState<Object> viewParam = (DefaultParamState<Object>) q.getCore().findParamByPath("/caseType");
		
		viewParam.setState(_ALOHA);
		
		//Thread.sleep(1000);
		assertSame(_ALOHA, q.getCore().getState().getCaseType());
		assertNull(((Model<VRSimpleCaseFlow>)q.getView()).getState().getPg3().getAloha());		//coz view leaf param is mapped
		assertSame(_ALOHA, q.getView().findParamByPath("/pg3/aloha").getState());
	}
	
	@Test
	public void tv04_col_attached_v2c_set_noConversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		//set list
		List<ServiceLine> newList = new ArrayList<>();
		
		ServiceLine sl_0 = new ServiceLine();
		sl_0.setService("VIEW SAYS: 0th service");
		newList.add(sl_0);
		
		ServiceLine sl_1 = new ServiceLine();
		sl_1.setService("VIEW SAYS: 1st service");
		newList.add(sl_1);
		
		@SuppressWarnings("unchecked")
		final ListParam<ServiceLine> vp_list = 
			q.getRoot().findParamByPath("/view_simplecase/pg3/noConversionAttachedColServiceLines").findIfCollection();
		assertNotNull(vp_list);
		assertNull(vp_list.getState());
		
		//set
		vp_list.setState(newList);
		assertNotNull(vp_list.getState());
		
		assertNotSame(newList, q.getCore().findParamByPath("/serviceLines").getState());
		assertNotSame(newList, vp_list.getState());
		
		assertSame(2, vp_list.getState().size());
		assertSame(2, vp_list.getType().findIfCollection().getModel().getParams().size()); 
		
		SimpleCase core = q.getCore().getState();
		
		for(int i=0; i<2; i++) {
			assertEquals(core.getServiceLines().get(i).getService(), q.getCore().findParamByPath("/serviceLines/"+i+"/service").getState());
		}
		
		// set again
		List<ServiceLine> againList = new ArrayList<>();
		
		ServiceLine againsl_0 = new ServiceLine();
		againsl_0.setService("again VIEW SAYS: 0th service");
		againList.add(againsl_0);
		
		ServiceLine againsl_1 = new ServiceLine();
		againsl_1.setService("again VIEW SAYS: 1st service");
		againList.add(againsl_1);
		
		vp_list.setState(againList);
		assertSame(2, vp_list.size());
		assertSame(2, vp_list.getState().size());
		assertSame(2, vp_list.getType().findIfCollection().getModel().getParams().size()); 
		
		assertSame(againsl_0.getService(), q.getRoot().findParamByPath("/view_simplecase/pg3/noConversionAttachedColServiceLines/0/service").getState());
		assertSame(againsl_1.getService(), q.getRoot().findParamByPath("/view_simplecase/pg3/noConversionAttachedColServiceLines/1/service").getState());
	}

	@Test
	public void tv05_col_attached_v2c_add_noConversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		Model<VRSimpleCaseFlow> vUMCase = ((Model<VRSimpleCaseFlow>)q.getView());
		assertNotNull(vUMCase);
		
		assertNull(vUMCase.getState());
		
		// if added to core via SAC, should reflect in corresponding view SAC
		@SuppressWarnings("unchecked")
		final ListParam<ServiceLine> vpCoreAttachedServiceLines = 
			q.getView().findParamByPath("/pg3/noConversionAttachedColServiceLines").findIfCollection();
		assertNotNull(vpCoreAttachedServiceLines);
		
		// add
		ServiceLine vp_casl_n = new ServiceLine();
		vp_casl_n.setService("It's elementary Watson!");
		
		vpCoreAttachedServiceLines.add(vp_casl_n);
		
		
		// ensure view models have been initialized
		VRSimpleCaseFlow view = ((Model<VRSimpleCaseFlow>)q.getView()).getState();
		assertNotNull(view.getPg3());
		assertNotNull(view.getPg3().getNoConversionAttachedColServiceLines());
		//==?? assertNull(view.getPg3().getCoreAttachedServiceLines().get(0));	//mapped direct would not set value into view, only onto mappedCore
		assertEquals(vp_casl_n.getService(), vpCoreAttachedServiceLines.getState(0).getService());
		
		// match with core
		SimpleCase core = q.getCore().getState();
		
		assertSame(core.getServiceLines().size(), vpCoreAttachedServiceLines.size());
		
		ServiceLine sl_n = core.getServiceLines().get(0);
		assertNotNull(sl_n);
		assertEquals(vp_casl_n, sl_n);
		assertSame(vp_casl_n.getService(), sl_n.getService());
		assertEquals(vp_casl_n, q.getCore().findParamByPath("/serviceLines").findIfCollection().getState(0));
	}
	
	@Test
	public void tv06_col_attached_v2c_set_conversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		// set list
		List<Section_ServiceLine> newList = new ArrayList<>();
		
		Section_ServiceLine sl_0 = new Section_ServiceLine();
		sl_0.setService("VIEW SAYS: 0th service");
		newList.add(sl_0);
		
		Section_ServiceLine sl_1 = new Section_ServiceLine();
		sl_1.setService("VIEW SAYS: 1st service");
		newList.add(sl_1);
		
		@SuppressWarnings("unchecked")
		final ListParam<Section_ServiceLine> vp_list = 
			q.getRoot().findParamByPath("/view_simplecase/pg3/viewAttachedServiceLinesConverted").findIfCollection();
		assertNotNull(vp_list);
		assertNull(vp_list.getState());
		
		// set
		vp_list.setState(newList);
		assertNotNull(vp_list.getState());
		
		assertNotSame(newList, q.getCore().findParamByPath("/serviceLinesConverted").getState());
		assertNotSame(newList, vp_list.getState());
		
		assertSame(2, vp_list.getState().size());
		assertSame(2, vp_list.getType().findIfCollection().getModel().getParams().size());
		
		assertNotSame(sl_0, q.getCore().findParamByPath("/serviceLinesConverted/0").getState());
		assertEquals(sl_0.getService(), q.getCore().findParamByPath("/serviceLinesConverted/0/service").getState());
		
		assertNotSame(sl_1, q.getCore().findParamByPath("/serviceLinesConverted/1").getState());
		assertEquals(sl_1.getService(), q.getCore().findParamByPath("/serviceLinesConverted/1/service").getState());

		SimpleCase core = q.getCore().getState();
		
		AtomicInteger counter = new AtomicInteger(0);
		q.getCore().templateParams().find("serviceLinesConverted").getType().findIfCollection().getModel().getParams()
			.stream()
			.sequential()
			.peek(p->{
				assertSame(core.getServiceLinesConverted().get(counter.getAndIncrement()).getService(), p.findParamByPath("/service").getState());
			});
	}
	
	@Test
	public void tv07_col_attached_v2c_add_conversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		Model<VRSimpleCaseFlow> vUMCase = ((Model<VRSimpleCaseFlow>)q.getView());
		assertNotNull(vUMCase);
		
		assertNull(vUMCase.getState());
		
		@SuppressWarnings("unchecked")
		final ListParam<Section_ServiceLine> vpServiceLines2 = 
				q.getRoot().findParamByPath("/view_simplecase/pg3/viewAttachedServiceLinesConverted").findIfCollection();
		assertNotNull(vpServiceLines2);
		assertSame(0, vpServiceLines2.size());
		
		// add
		Section_ServiceLine vpColElem_sl_n0 = new Section_ServiceLine();
		vpColElem_sl_n0.setService("VIEW SAYS: It's elementary Watson!");
		
		vpServiceLines2.add(vpColElem_sl_n0);
		
		
		SimpleCase umcase = q.getCore().getState();
		assertNotNull(umcase);
		assertNotNull(umcase.getServiceLinesConverted());
		assertSame(1, umcase.getServiceLinesConverted().size());
		assertSame(1, vpServiceLines2.size());
		assertNotNull(umcase.getServiceLinesConverted().get(0));
		assertTrue(umcase.getServiceLinesConverted().get(0) instanceof ServiceLine);
		assertSame(vpColElem_sl_n0.getService(), umcase.getServiceLinesConverted().get(0).getService());
		
		@SuppressWarnings("unchecked")
		final ListParam<ServiceLine> cpServiceLines = 
				q.getCore().findParamByPath("/serviceLinesConverted").findIfCollection();
		
		ServiceLine cpColElem_sl_n0 = cpServiceLines.getState(0);
		assertNotNull(cpColElem_sl_n0);
		assertSame(vpColElem_sl_n0.getService(), cpColElem_sl_n0.getService());
		
	}
	
	@Test
	public void tv08_leaf_attached_c2v_conversion() throws Exception {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		ServiceLine coreSingle = new ServiceLine();
		coreSingle.setService("Life is Ka!");
		
		// set
		final DefaultParamState<Object> serviceLineParam = 
				(DefaultParamState<Object>) q.getRoot().findParamByPath("/core_simplecase/oneServiceLineConverted");
		
		serviceLineParam.setState(coreSingle);
		
		assertEquals(coreSingle, q.getRoot().findParamByPath("/core_simplecase/oneServiceLineConverted").getState());
		assertEquals(coreSingle, q.getCore().getState().getOneServiceLineConverted());
		assertNotNull(q.getRoot().findParamByPath("/view_simplecase/pg3/viewAttachedOneServiceLineConverted").getState());
		assertNotSame(coreSingle, q.getRoot().findParamByPath("/view_simplecase/pg3/viewAttachedOneServiceLineConverted").getState());
		assertEquals(coreSingle.getService(), q.getRoot().findParamByPath("/view_simplecase/pg3/viewAttachedOneServiceLineConverted/service").getState());
	}
	
	@Test
	public void tv09_leaf_attached_v2c_conversion() throws Exception {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		Section_ServiceLine coreSingle = new Section_ServiceLine();
		coreSingle.setService("Life is Ka!");
		
		// set
		
		final DefaultParamState<Object> serviceLineParam = 
				(DefaultParamState<Object>) q.getRoot().findParamByPath("/view_simplecase/pg3/viewAttachedOneServiceLineConverted");
		
		serviceLineParam.setState(coreSingle);
		
		assertNotSame(coreSingle, q.getRoot().findParamByPath("/view_simplecase/pg3/viewAttachedOneServiceLineConverted").getState());
		assertSame(((Model<VRSimpleCaseFlow>)q.getView()).getState().getPg3().getViewAttachedOneServiceLineConverted(), q.getRoot().findParamByPath("/view_simplecase/pg3/viewAttachedOneServiceLineConverted").getState());
		assertNotNull(q.getRoot().findParamByPath("/core_simplecase/oneServiceLineConverted").getState());
		assertNotSame(coreSingle, q.getRoot().findParamByPath("/core_simplecase/oneServiceLineConverted").getState());
		assertSame(coreSingle.getService(), q.getRoot().findParamByPath("/core_simplecase/oneServiceLineConverted/service").getState());
	}
	
	@Test
	public void tv10_col_attached_c2v_add_noConversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		Model<VRSimpleCaseFlow> vUMCase = ((Model<VRSimpleCaseFlow>)q.getView());
		assertNotNull(vUMCase);
		
		assertNull(vUMCase.getState());
		
		// add
		ServiceLine sl_n = new ServiceLine();
		sl_n.setService("It's elementary Watson!");
		
		@SuppressWarnings("unchecked")
		final DefaultListParamState<ServiceLine> serviceLineParams = 
				(DefaultListParamState<ServiceLine>) q.getCore().findParamByPath("/serviceLines").findIfCollection();
		
		serviceLineParams.add(sl_n);

		SimpleCase core = q.getCore().getState();
		
		ListParam<ServiceLine> vpServiceLines = q.getView().findParamByPath("/pg3/noConversionAttachedColServiceLines").findIfCollection();
		assertNotNull(vpServiceLines);
		assertNotNull(vpServiceLines.getState());
		
		assertSame(core.getServiceLines().size(), vpServiceLines.size());
		assertNotNull(q.getCore().findParamByPath("/serviceLines").getState());
		assertSame(q.getCore().findParamByPath("/serviceLines").getState(), vpServiceLines.getState());
		
		Param<ServiceLine> vpServiceLine_0Param = q.getView().findParamByPath("/pg3/noConversionAttachedColServiceLines/0");
		assertNotNull(vpServiceLine_0Param);
		
		ListElemParam<ServiceLine> vpServiceLine_0 = vpServiceLine_0Param.findIfCollectionElem();
		assertEquals(sl_n, vpServiceLine_0Param.getState());
		assertEquals(sl_n, vpServiceLines.getState(0));
		assertEquals(vpServiceLine_0.getState(), vpServiceLines.getState(0));
		
		ServiceLine vp_sl_n = vpServiceLines.getState(0);
		assertNotNull(vp_sl_n);
		assertSame(sl_n.getService(), vp_sl_n.getService());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void tv11_col_attached_c2v_add_conversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		Model<VRSimpleCaseFlow> vUMCase = ((Model<VRSimpleCaseFlow>)q.getView());
		assertNotNull(vUMCase);
		
		assertNull(vUMCase.getState());
		
		ServiceLine sl_n = new ServiceLine();
		sl_n.setService("It's elementary Watson!");
		
		final DefaultListParamState<ServiceLine> serviceLineParams = 
				(DefaultListParamState<ServiceLine>) q.getCore().findParamByPath("/serviceLinesConverted").findIfCollection();
		
		serviceLineParams.add(sl_n);
		
		assertEquals(sl_n, q.getCore().findParamByPath("/serviceLinesConverted/0").getState());
		
		SimpleCase core = q.getCore().getState();
		assertNotNull(core.getServiceLinesConverted());
		assertNotNull(core.getServiceLinesConverted().get(0));
		
		ListParam<Section_ServiceLine> vpServiceLines = q.getView().findParamByPath("/pg3/viewAttachedServiceLinesConverted").findIfCollection();
		assertNotNull(vpServiceLines);
		assertSame(core.getServiceLinesConverted().size(), vpServiceLines.size());
		assertNotSame(q.getCore().findParamByPath("/serviceLinesConverted").getState(), vpServiceLines.getState());
		
		
		Param<Section_ServiceLine> vpServiceLine_0Param = q.getView().findParamByPath("/pg3/viewAttachedServiceLinesConverted/0");
		assertNotNull(vpServiceLine_0Param);
		
		ListElemParam<Section_ServiceLine> vpServiceLine_0 = vpServiceLine_0Param.findIfCollectionElem();
		assertNotSame(sl_n, vpServiceLine_0Param.getState());
		assertNotSame(sl_n, vpServiceLines.getState(0));
		assertEquals(vpServiceLine_0.getState(), vpServiceLines.getState(0));
		
		Section_ServiceLine vp_sl_n = vpServiceLines.getState(0);
		assertNotNull(vp_sl_n);
		assertSame(sl_n.getService(), q.getView().findParamByPath("/pg3/viewAttachedServiceLinesConverted/0/service").getState());
	}

	@Test
	public void tv12_leaf_detached_c2v_noConversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());

		ServiceLine sl_n = new ServiceLine();
		sl_n.setService("It's elementary Watson!");
		
		
		final DefaultParamState<Object> serviceLineParam = 
				(DefaultParamState<Object>) q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine.m");
		
		serviceLineParam.setState(sl_n);
		
		assertEquals(sl_n, q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine.m").getState());
		assertEquals(sl_n, q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine").getState());
		
		assertSame(q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine.m").getState(), q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine").getState());
	}

	@Test
	public void tv13_leaf_detached_v2c_noConversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());

		ServiceLine sl_n = new ServiceLine();
		sl_n.setService("It's elementary Watson!");
		final DefaultParamState<Object> serviceLineParam = 
				(DefaultParamState<Object>) q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine");
		
		serviceLineParam.setState(sl_n);
		assertEquals(sl_n, q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine").getState());
		assertEquals(sl_n, q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine.m").getState());
		
		assertSame(q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine").getState(), q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine.m").getState());
	}

	@Test
	public void tv14_leaf_detached_c2v_conversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		ServiceLine coreSingle = new ServiceLine();
		coreSingle.setService("Life is Ka!");
		
		final DefaultParamState<Object> serviceLineParam = 
				(DefaultParamState<Object>) q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine.m");
		
		serviceLineParam.setState(coreSingle);
		
		assertEquals(coreSingle, q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine.m").getState());
		
		assertNotNull(q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine").getState());
		assertNotSame(coreSingle, q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine").getState());
		assertSame(coreSingle.getService(), q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine/service").getState());

	}

	@Test
	public void tv15_leaf_detached_v2c_conversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());

		Section_ServiceLine viewModel = new Section_ServiceLine();
		viewModel.setService("\"I am a genius\", said the fool");
		
		final DefaultParamState<Object> serviceLineParam = 
				(DefaultParamState<Object>) q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine");
		
		serviceLineParam.setState(viewModel);
		
		assertNotSame(viewModel, q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine").getState());
		assertSame(q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine").getState(), ((Model<VRSimpleCaseFlow>)q.getView()).getState().getPg3().getConvertedDetachedOneServiceLine());

		assertNotNull(q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine.m").getState());
		assertNotSame(viewModel, q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine.m").getState());
		
		assertNotSame(q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine").getState(), 
				q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine.m").getState());
		
		assertSame(q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine/service").getState(), 
				q.getRoot().findParamByPath("/view_simplecase/pg3/convertedDetachedOneServiceLine.m/service").getState());
		
	}


	@Test
	public void tv16_col_detached_c2v_add_noConversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		Model<VRSimpleCaseFlow> vUMCase = ((Model<VRSimpleCaseFlow>)q.getView());
		assertNotNull(vUMCase);
		
		assertNull(vUMCase.getState());
		
		ServiceLine sl_n = new ServiceLine();
		sl_n.setService("It's elementary Watson!");
		
		@SuppressWarnings("unchecked")
		final ListParam<ServiceLine> cpDetachedServiceLines = q.getView().findParamByPath("/pg3/noConversionDetachedColServiceLines.m").findIfCollection();
		assertNotNull(cpDetachedServiceLines);
		
		// add
		cpDetachedServiceLines.add(sl_n);
		//q.getView().findParamByPath("/pg3/noConversionDetachedColServiceLines.m").findIfCollection().add(sl_n);
		
		ListParam<ServiceLine> vpServiceLines = q.getView().findParamByPath("/pg3/noConversionDetachedColServiceLines").findIfCollection();
		assertNotNull(vpServiceLines);
		assertNotNull(vpServiceLines.getState());
		
		assertNotNull(cpDetachedServiceLines.getState());
		
		assertSame(cpDetachedServiceLines.size(), vpServiceLines.size());
		assertSame(q.getView().findParamByPath("/pg3/noConversionDetachedColServiceLines.m").getState(), vpServiceLines.getState());
		
		Param<ServiceLine> vpServiceLine_0Param = q.getView().findParamByPath("/pg3/noConversionDetachedColServiceLines/0");
		assertNotNull(vpServiceLine_0Param);
		
		ListElemParam<ServiceLine> vpServiceLine_0 = vpServiceLine_0Param.findIfCollectionElem();
		assertEquals(sl_n, vpServiceLine_0Param.getState());
		assertEquals(sl_n, vpServiceLines.getState(0));
		assertSame(vpServiceLine_0.getState(), vpServiceLines.getState(0));
		
		ServiceLine vp_sl_n = vpServiceLines.getState(0);
		assertNotNull(vp_sl_n);
		assertEquals(sl_n.getService(), vp_sl_n.getService());
	}

	@Test
	public void tv17_col_detached_v2c_add_noConversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		ServiceLine sl = new ServiceLine();
		sl.setService("Its a bird..");
		
		// add
		@SuppressWarnings("unchecked")
		final DefaultListParamState<ServiceLine> serviceLineParams = 
				(DefaultListParamState<ServiceLine>) q.getView().findParamByPath("/pg3/noConversionDetachedColServiceLines").findIfCollection();
		
		serviceLineParams.add(sl);
	
		ListParam<ServiceLine> vpServiceLines = q.getView().findParamByPath("/pg3/noConversionDetachedColServiceLines").findIfCollection();
		assertNotNull(vpServiceLines);
		assertNotNull(vpServiceLines.getState());
		
		ListParam<ServiceLine> detachedServiceLines = q.getView().findParamByPath("/pg3/noConversionDetachedColServiceLines.m").findIfCollection();
		assertNotNull(detachedServiceLines);
		assertNotNull(detachedServiceLines.getState());
		
		assertSame(detachedServiceLines.size(), vpServiceLines.size());
		assertSame(detachedServiceLines.getState(), vpServiceLines.getState());
		
		Param<ServiceLine> vpServiceLine_0Param = q.getView().findParamByPath("/pg3/noConversionDetachedColServiceLines/0");
		assertNotNull(vpServiceLine_0Param);
		
		ListElemParam<ServiceLine> vpServiceLine_0 = vpServiceLine_0Param.findIfCollectionElem();
		assertEquals(sl, vpServiceLine_0Param.getState());
		assertEquals(sl, vpServiceLines.getState(0));
		assertSame(vpServiceLine_0.getState(), vpServiceLines.getState(0));
		
		ServiceLine vp_sl_n = vpServiceLines.getState(0);
		assertNotNull(vp_sl_n);
		assertEquals(sl.getService(), vp_sl_n.getService());

	}

	@Test
	public void tv18_col_detached_c2v_set_conversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		ServiceLine sl_0 = new ServiceLine();
		sl_0.setService("Batman");
		
		ServiceLine sl_1 = new ServiceLine();
		sl_1.setService("Robin");
		
		List<ServiceLine> coreServiceLines = new ArrayList<>();
		coreServiceLines.add(sl_0);
		coreServiceLines.add(sl_1);
		
		// core
		@SuppressWarnings("unchecked")
		final ListParam<ServiceLine> detachedCoreServiceLines = 
				q.getView().findParamByPath("/pg3/viewDetachedServiceLinesConverted.m").findIfCollection();
		assertNotNull(detachedCoreServiceLines);
		assertNull(detachedCoreServiceLines.getState());
		
		// view
		ListParam<Section_ServiceLine> detachedViewServiceLines = q.getView().findParamByPath("/pg3/viewDetachedServiceLinesConverted").findIfCollection();
		assertNotNull(detachedViewServiceLines);
		assertNull(detachedViewServiceLines.getState());
		
		// set to core
		detachedCoreServiceLines.setState(coreServiceLines);
		
		// validate core
		assertNotNull(detachedCoreServiceLines.getState());
		assertSame(coreServiceLines.size(), detachedCoreServiceLines.size());
		
		for(int i=0; i<coreServiceLines.size(); i++) {
			ServiceLine expected = coreServiceLines.get(i);
			ServiceLine actual = detachedCoreServiceLines.getState(i);
			assertEquals(expected, actual);
		}
		
		// validate view
		assertNotNull(detachedViewServiceLines.getState());
		assertSame(coreServiceLines.size(), detachedViewServiceLines.size());
		
		for(int i=0; i<coreServiceLines.size(); i++) {
			ServiceLine expected = coreServiceLines.get(i);
			
			assertSame(expected.getService(), detachedViewServiceLines.findParamByPath("/"+i+"/service").getState());
		}
		
		// validate entity as a whole using leaf-state
		List<Section_ServiceLine> detachedViewServiceLinesState = detachedViewServiceLines.getLeafState();
		assertNotNull(detachedViewServiceLinesState);
		assertSame(coreServiceLines.size(), detachedViewServiceLinesState.size());
		
		for(int i=0; i<coreServiceLines.size(); i++) {
			ServiceLine expected = coreServiceLines.get(i);
			Section_ServiceLine actual = detachedViewServiceLinesState.get(i);
			assertSame(expected.getService(), actual.getService());
		}
	}

	@Test
	public void tv19_col_detached_v2c_set_conversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		Section_ServiceLine vsl1 = new Section_ServiceLine();
		vsl1.setService("Life is Ka!");

		Section_ServiceLine vsl2 = new Section_ServiceLine();
		vsl2.setService("..and he followed the Man in the black.");
		
		List<Section_ServiceLine> viewServiceLinesState = new ArrayList<>();
		viewServiceLinesState.add(vsl1);
		viewServiceLinesState.add(vsl2);
	
		// core
		@SuppressWarnings("unchecked")
		final ListParam<ServiceLine> detachedCoreServiceLines = 
				q.getView().findParamByPath("/pg3/viewDetachedServiceLinesConverted.m").findIfCollection();
		assertNotNull(detachedCoreServiceLines);
		assertNull(detachedCoreServiceLines.getState());
		
		// view
		ListParam<Section_ServiceLine> detachedViewServiceLines = q.getView().findParamByPath("/pg3/viewDetachedServiceLinesConverted").findIfCollection();
		assertNotNull(detachedViewServiceLines);
		assertNull(detachedViewServiceLines.getState());
		
		// set to view
		detachedViewServiceLines.setState(viewServiceLinesState);

		// validate core
		assertNotNull(detachedCoreServiceLines.getState());
		assertSame(viewServiceLinesState.size(), detachedCoreServiceLines.size());
		
		for(int i=0; i<viewServiceLinesState.size(); i++) {
			Section_ServiceLine expected = viewServiceLinesState.get(i);
			ServiceLine actual = detachedCoreServiceLines.getState(i);
			assertSame(expected.getService(), actual.getService());
		}
		
		// validate view
		assertNotNull(detachedViewServiceLines.getState());
		assertSame(viewServiceLinesState.size(), detachedViewServiceLines.size());
		
		for(int i=0; i<viewServiceLinesState.size(); i++) {
			Section_ServiceLine expected = viewServiceLinesState.get(i);
			
			assertSame(expected.getService(), detachedViewServiceLines.findParamByPath("/"+i+"/service").getState());
		}
		
		// validate entity as a whole using leaf-state
		List<Section_ServiceLine> detachedViewServiceLinesState = detachedViewServiceLines.getLeafState();
		assertNotNull(detachedViewServiceLinesState);
		assertSame(viewServiceLinesState.size(), detachedViewServiceLinesState.size());
		
		for(int i=0; i<viewServiceLinesState.size(); i++) {
			Section_ServiceLine expected = viewServiceLinesState.get(i);
			Section_ServiceLine actual = detachedViewServiceLinesState.get(i);
			assertSame(expected.getService(), actual.getService());
		}
	}

	@Test
	public void tv20_col_attached_v2c_set_conversion_existing() {
		final String K_CASE_TYPE = "test case type";
		
		ServiceLine sl_0 = new ServiceLine();
		sl_0.setService("Batman");
		
		ServiceLine sl_1 = new ServiceLine();
		sl_1.setService("Robin");
		
		List<ServiceLine> coreServiceLines = new ArrayList<>();
		coreServiceLines.add(sl_0);
		coreServiceLines.add(sl_1);
		
		SimpleCase existingCore = new SimpleCase();
		existingCore.setCaseType(K_CASE_TYPE);
		existingCore.setServiceLinesConverted(coreServiceLines);
		
		ExecutionEntity<VRSimpleCaseFlow, SimpleCase> eState = new ExecutionEntity<>();
		eState.setCore(existingCore);
		
		Command cmd = create_view_main();
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = quadModelBuilder.build(cmd, eState);
		q.getRoot().initState();
		
		@SuppressWarnings("unchecked")
		final ListParam<Section_ServiceLine> vp_list = 
				q.getRoot().findParamByPath("/view_simplecase/pg3/viewAttachedServiceLinesConverted").findIfCollection();
		
		assertNotNull(vp_list);
		assertNotNull(vp_list.getState());
		
		for(int i=0; i<coreServiceLines.size(); i++) {
			ServiceLine expected = coreServiceLines.get(i);
			Section_ServiceLine actual = vp_list.getLeafState(i);
			assertSame(expected.getService(), actual.getService());
		}
	}
	
	@Test
	public void tv21_colMappedAttrib_attached_v2c_set_conversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		final ListParam<String> vp_nestedService = q.getRoot().findParamByPath("/view_simplecase/pg3/attachedNestedColAttribServices").findIfCollection();
		assertNotNull(vp_nestedService);
		
		ListParam<ServiceLine> cp_ServiceLines = q.getCore().findParamByPath("/serviceLines").findIfCollection();
		assertNotNull(cp_ServiceLines);
		
		assertSame(0, vp_nestedService.size());
		assertSame(0, cp_ServiceLines.size());
		
		String service = "some new service";
		vp_nestedService.add(service);
		
		assertSame(1, vp_nestedService.size());
		assertSame(1, cp_ServiceLines.size());
		
		Param<?> convertedAttachedList = q.getView().findParamByPath("/pg3/viewAttachedServiceLinesConverted");
		
		assertSame(service, vp_nestedService.getState(0));
		assertSame(service, cp_ServiceLines.getState(0).getService());
	}
	
	@Test
	public void tv22_col_attached_v2c_delete_noConversion() {
		// setup
		tv04_col_attached_v2c_set_noConversion();

		// validate setup
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		@SuppressWarnings("unchecked")
		final ListParam<ServiceLine> vp_list = 
				q.getRoot().findParamByPath("/view_simplecase/pg3/noConversionAttachedColServiceLines").findIfCollection();
		
		assertNotNull(vp_list);
		assertNotNull(vp_list.getState());
		int oldSize = vp_list.size();
		
		// add another new elem
		ServiceLine newElem = new ServiceLine();
		newElem.setService("New elem added for Delete testing");
		
		vp_list.add(newElem);
		assertEquals(oldSize+1, vp_list.size());
		
		ListParam<ServiceLine> cp_list = q.getRoot().findParamByPath("/core_simplecase/serviceLines").findIfCollection();
		assertEquals(oldSize+1, cp_list.size());
		
		assertEquals(newElem, vp_list.getState(oldSize));
		assertEquals(newElem, cp_list.getState(oldSize));
		
		// dp delete
		ListElemParam<?> lastElemAdded = q.getRoot().findParamByPath("/view_simplecase/pg3/noConversionAttachedColServiceLines/"+oldSize).findIfCollectionElem();
		assertNotNull(lastElemAdded);
		assertEquals(newElem, lastElemAdded.getState());
		
		boolean isRemoved = vp_list.remove((ListElemParam<ServiceLine>)lastElemAdded);
		assertTrue(isRemoved);
		
		// validate post delete
		assertEquals(oldSize, vp_list.size());
		assertEquals(oldSize, cp_list.size());
	}

	@Test
	public void tv23_col_attached_elemNestedAttrib_v2c_delete_conversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		
		final ListParam<String> vp_nestedService = q.getRoot().findParamByPath("/view_simplecase/pg3/attachedNestedColAttribServices").findIfCollection();
		assertNotNull(vp_nestedService);
		
		ListParam<ServiceLine> cp_ServiceLines = q.getCore().findParamByPath("/serviceLines").findIfCollection();
		assertNotNull(cp_ServiceLines);

		ListParam<?> vp_noConvertedAttachedList = q.getView().findParamByPath("/pg3/noConversionAttachedColServiceLines").findIfCollection();
		assertNotNull(vp_noConvertedAttachedList);
		
		assertEquals(0, vp_nestedService.size());
		assertEquals(0, cp_ServiceLines.size());
		assertEquals(0, vp_noConvertedAttachedList.size());
		
		// add
		final String K_SERVICE_0 = "0 - some new service "+new Date();
		vp_nestedService.add(K_SERVICE_0);
		
		assertEquals(1, vp_nestedService.size());
		assertEquals(1, cp_ServiceLines.size());
		assertEquals(1, vp_noConvertedAttachedList.size());
		
		assertSame(K_SERVICE_0, vp_nestedService.getState(0));
		assertSame(K_SERVICE_0, cp_ServiceLines.getState(0).getService());
		assertSame(K_SERVICE_0, vp_noConvertedAttachedList.findStateByPath("/0/service"));
		
		// add again
		final String K_SERVICE_1 = "1 - some new service "+new Date();
		vp_nestedService.add(K_SERVICE_1);
		
		assertEquals(2, vp_nestedService.size());
		assertEquals(2, cp_ServiceLines.size());
		assertEquals(2, vp_noConvertedAttachedList.size());
		
		assertSame(K_SERVICE_0, vp_nestedService.getState(0));
		assertSame(K_SERVICE_0, cp_ServiceLines.getState(0).getService());
		assertSame(K_SERVICE_0, vp_noConvertedAttachedList.findStateByPath("/0/service"));
		assertSame(K_SERVICE_1, vp_nestedService.getState(1));
		assertSame(K_SERVICE_1, cp_ServiceLines.getState(1).getService());
		assertSame(K_SERVICE_1, vp_noConvertedAttachedList.findStateByPath("/1/service"));
		
		// elemId:0 entry
		Param<?> vp_nestedService_0 = vp_nestedService.findParamByPath("/0");
		assertNotNull(vp_nestedService_0);
		assertSame(K_SERVICE_0, vp_nestedService_0.getState());
		
		// delete elemId:0
		assertTrue(vp_nestedService_0.findIfCollectionElem().remove());
		
		assertEquals(1, vp_nestedService.size());
		assertEquals(1, cp_ServiceLines.size());
		assertEquals(1, vp_noConvertedAttachedList.size());

		assertSame(K_SERVICE_1, vp_nestedService.getState(0));
		assertSame(K_SERVICE_1, cp_ServiceLines.getState(0).getService());
		assertSame(K_SERVICE_1, vp_noConvertedAttachedList.findStateByPath("/1/service")); // elemId doesn't change
		
		// add again 
		final String K_SERVICE_2 = "2 - some new service "+new Date();
		vp_nestedService.add(K_SERVICE_2);
		
		assertEquals(2, vp_nestedService.size());
		assertEquals(2, cp_ServiceLines.size());
		assertEquals(2, vp_noConvertedAttachedList.size());
		
		assertSame(K_SERVICE_1, vp_nestedService.getState(0));
		assertSame(K_SERVICE_1, cp_ServiceLines.getState(0).getService());
		assertSame(K_SERVICE_1, vp_noConvertedAttachedList.findStateByPath("/1/service")); // elemId doesn't change
		assertSame(K_SERVICE_2, vp_nestedService.getState(1));
		assertSame(K_SERVICE_2, cp_ServiceLines.getState(1).getService());
		assertSame(K_SERVICE_2, vp_noConvertedAttachedList.findStateByPath("/2/service")); // elemId increments from last max
		
		// elemId:1 entry :: CORE
		Param<ServiceLine> cp_ServiceLines_1= cp_ServiceLines.findParamByPath("/1");
		assertNotNull(cp_ServiceLines_1);
		assertSame(K_SERVICE_1, cp_ServiceLines_1.getState().getService());
		
		// delete elemId:1 entry :: CORE
		assertTrue(cp_ServiceLines_1.findIfCollectionElem().remove());

		assertEquals(1, vp_nestedService.size());
		assertEquals(1, cp_ServiceLines.size());
		assertEquals(1, vp_noConvertedAttachedList.size());
		
		assertSame(K_SERVICE_2, vp_nestedService.getState(0));
		assertSame(K_SERVICE_2, cp_ServiceLines.getState(0).getService());
		assertSame(K_SERVICE_2, vp_noConvertedAttachedList.findStateByPath("/2/service")); // elemId doesn't change

		// add again :: CORE (c2v)
		final String K_SERVICE_3 = "3 - some new service "+new Date();
		ServiceLine core_serviceLine_3 = new ServiceLine();
		core_serviceLine_3.setService(K_SERVICE_3);
		
		cp_ServiceLines.add(core_serviceLine_3);
		
		assertEquals(2, vp_nestedService.size());
		assertEquals(2, cp_ServiceLines.size());
		assertEquals(2, vp_noConvertedAttachedList.size());
		
		assertSame(K_SERVICE_2, vp_nestedService.getState(0));
		assertSame(K_SERVICE_2, cp_ServiceLines.getState(0).getService());
		assertSame(K_SERVICE_2, vp_noConvertedAttachedList.findStateByPath("/2/service")); // elemId doesn't change
		assertSame(K_SERVICE_3, vp_nestedService.getState(1));
		assertSame(K_SERVICE_3, cp_ServiceLines.getState(1).getService());
		assertSame(K_SERVICE_3, vp_noConvertedAttachedList.findStateByPath("/3/service")); // elemId increments from last max
	
	}
	
	@Test
	public void tv24_nested_attached_noConversion() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		Model<VRSimpleCaseFlow> vUMCase = ((Model<VRSimpleCaseFlow>)q.getView());
		assertNotNull(vUMCase);
		
		assertNull(vUMCase.getState());
		
		//if added to core via SAC, should reflect in corresponding view SAC
		
		// ensure that view collection is not initialized yet
		assertNull(q.getView().getState());
		
		Param<ServiceLine> vpOneServiceLine = q.getView().findParamByPath("/pg3/coreAttachedOneServiceLine");
		assertNotNull(vpOneServiceLine);
		
		Param<String> vpOneServiceLineService = q.getView().findParamByPath("/pg3/coreAttachedOneServiceLine/service");
		assertNotNull(vpOneServiceLineService);
		
		final String K_service = "It's elementary Watson!";
		assertNull(vpOneServiceLine.getState());
		
		vpOneServiceLineService.setState(K_service);
		
		assertNotNull(vpOneServiceLine.getState());
		
		Param<ServiceLine> cpOneServiceLine = q.getCore().findParamByPath("/oneServiceLine");
		assertNotNull(cpOneServiceLine);
		assertNotNull(cpOneServiceLine.getState());
		
		assertSame(K_service, cpOneServiceLine.getState().getService());
		
		Param<ServiceLine> mappedService = q.getView().findParamByPath("/pg3/coreAttachedOneServiceLine/service");
		Param<ServiceLine> mapsToService = q.getCore().findParamByPath("/oneServiceLine/service");
		assertNotSame(mappedService, mapsToService);
		assertSame(mappedService.getState(), mapsToService.getState());
		assertSame(vpOneServiceLineService.getState(), mapsToService.getState());
	}
	
	//@After
	public void after() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(create_view_main().getRootDomainUri());
		printJson(q);
		//System.out.println("### Counter: "+ DomainConfigAPITest.eventPublisher.counter);
	}
	
	public void printJson(QuadModel<?, ?> q) {
//		String json = JsonUtils.get().convert(q.getView());
//		System.out.println("@@@  ");
//		System.out.println("@@@  "+ json);
//		System.out.println("@@@  ");
	}
}
