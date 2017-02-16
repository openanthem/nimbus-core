/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType.CollectionType;
import com.anthem.oss.nimbus.core.domain.model.config.builder.EntityConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.StateType;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListElemParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListModel;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.builder.PageNavigationInitializer;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.repo.DefaultParamStateRepositoryLocal;
import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;
import com.anthem.oss.nimbus.core.rules.DefaultRulesEngineFactoryProducer;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.core.utils.JavaBeanHandler;
import com.anthem.oss.nimbus.test.sample.um.model.ServiceLine;
import com.anthem.oss.nimbus.test.sample.um.model.ServiceLine.AuditInfo;
import com.anthem.oss.nimbus.test.sample.um.model.UMCase;
import com.anthem.oss.nimbus.test.sample.um.model.view.Page_Pg3.Section_ServiceLine;
import com.anthem.oss.nimbus.test.sample.um.model.view.UMCaseFlow;

import test.com.anthem.nimbus.platform.spec.model.comamnd.TestCommandFactory;
import test.com.anthem.nimbus.platform.utils.JsonUtils;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@EnableConfigurationProperties
@ComponentScan(basePackageClasses={
		DomainConfigBuilder.class, EntityConfigBuilder.class, QuadModelBuilder.class, PageNavigationInitializer.class, DefaultRulesEngineFactoryProducer.class,
		DefaultParamStateRepositoryLocal.class, JavaBeanHandler.class})
@ContextConfiguration(initializers=ConfigFileApplicationContextInitializer.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@ActiveProfiles("local")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuadModelCollectionsTest {
	
	@Autowired QuadModelBuilder quadModelBuilder;
	
	private static boolean done = false;
	
	@Before
	public void t_init() {
		if(!done) {
			quadModelBuilder.getDomainConfigApi().setBasePackages(
					Arrays.asList(
							ProcessFlow.class.getPackage().getName(),
							UMCase.class.getPackage().getName(),
							UMCaseFlow.class.getPackage().getName()
							));
			quadModelBuilder.getDomainConfigApi().reload();
			done = true;
		}
		
		Command cmd = TestCommandFactory.create_view_icr_UMCaseFlow();
		//QuadModel<UMCaseFlow, UMCase> q = quadModelBuilder.build(cmd, (mConfig)->ModelsTemplate.newInstance(mConfig.getReferredClass()));
		QuadModel<UMCaseFlow, UMCase> q = quadModelBuilder.build(cmd);
		assertNotNull(q);
		
		UserEndpointSession.setAttribute(cmd, q);
	}
	
	@Test
	public void tc01_sanity_check_core_builders() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		
		// ParamSAC
		Param<List<ServiceLine>> pSAC_col = q.getCore().findParamByPath("/serviceLines");
		assertNotNull(pSAC_col);
		assertEquals("/c/serviceLines", pSAC_col.getPath());
		
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
		assertTrue(pSAC_config.getType() instanceof ParamType.NestedCollection);
		
		// ParamSAC: ParamConfig.Type.NestedCollection => ParamType for List<ServiceLine>
		ParamType.NestedCollection<ServiceLine> pSAC_config_type = pSAC_config.getType().findIfCollection(); 
		assertSame(pSAC_config_type.getModel(), mSAC_col_config);
		assertSame(CollectionType.list, pSAC_config_type.getCollectionType());
		
		// list param element
		assertNotNull(pSAC_config_type.getElementConfig());
		assertTrue(pSAC_config_type.getElementConfig().getType().isNested());
		assertFalse(pSAC_config_type.getElementConfig().getType().isCollection());

		// list param element: type
		ParamType.Nested<ServiceLine> typNestedColElem = pSAC_config_type.getElementConfig().getType().findIfNested();
		assertSame(ServiceLine.class, typNestedColElem.getReferredClass());
		
		//ParamType
		// param sac : ListParam
		assertTrue(ListParam.class.isAssignableFrom(pSAC_col.getClass()));
		
	}
	
	@Test
	public void tc02_sanity_check_core_ops() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		
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
		
		UMCase core = q.getCore().getState();
		
		assertNotNull(core);
		assertSame(sl_0, core.getServiceLines().get(0));
		
		Param<ServiceLine> p_sl_0 = q.getCore().findParamByPath("/serviceLines/0");
		assertNotNull(p_sl_0);
		assertSame(sl_0, p_sl_0.getState());
	}

	
	@Test
	public void tc03_just_add() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		assertNotNull(q.getCore());

		//add to list
		ServiceLine elem = new ServiceLine();
		q.getCore().findParamByPath("/serviceLines").findIfCollection().add(elem);
		
		assertSame(1, q.getCore().getState().getServiceLines().size());
		assertSame(elem, q.getCore().getState().getServiceLines().get(0));
		assertSame(elem, q.getCore().findParamByPath("/serviceLines/0").findIfCollectionElem().getState());
		assertSame(elem, q.getCore().findParamByPath("/serviceLines/0").getState());
	}
	
	@Test
	public void tc04_just_get_list() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		assertNotNull(q.getCore());
		
		//get list
		assertNull(q.getCore().findParamByPath("/serviceLines").getState());
	}
	
	@Test
	public void tc05_just_set_list() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		
		//set list
		List<ServiceLine> newList = new ArrayList<>();
		
		ServiceLine sl_0 = new ServiceLine();
		newList.add(sl_0);
		
		ServiceLine sl_1 = new ServiceLine();
		newList.add(sl_1);
		
		//set
		q.getCore().findParamByPath("/serviceLines").setState(newList);
		UMCase core = q.getCore().getState();
		
		assertNotSame(newList, q.getCore().findParamByPath("/serviceLines").getState());
		assertNotSame(core.getServiceLines(), newList);
		assertSame(2, q.getCore().findParamByPath("/serviceLines").findIfCollection().size());
		assertSame(2, q.getCore().findParamByPath("/serviceLines").findIfCollection().getType().findIfCollection().getModel().getParams().size());
		
		assertSame(sl_0, q.getCore().findParamByPath("/serviceLines/0").getState());
		assertSame(sl_1, q.getCore().findParamByPath("/serviceLines/1").getState());
		
		AtomicInteger counter = new AtomicInteger(0);
		q.getCore().templateParams().find("serviceLines").getType().findIfCollection().getModel().getParams()
			.stream()
			.sequential()
			.peek(p->{
				assertSame(newList.get(counter.getAndIncrement()), p.getState());
			});
	}
	
	@Test
	public void tc06_col_nesting() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		
		ServiceLine elem = new ServiceLine();
		
		q.getCore().findParamByPath("/serviceLines").findIfCollection().add(elem);
		UMCase core = q.getCore().getState();
		
		assertSame(elem, core.getServiceLines().get(0));
		assertSame(elem, q.getCore().findParamByPath("/serviceLines/0").getState());
		
		AuditInfo a0 = new AuditInfo();
		a0.setBy("Me");
		a0.setWhen(new Date());
		a0.setWhy("just so");
		
		ListParam<AuditInfo> p_a = q.getCore().findParamByPath("/serviceLines/0/discharge/audits").findIfCollection();
		assertNotNull(p_a);
		assertEquals("/c/serviceLines/0/discharge/audits", p_a.getPath());
		
		p_a.add(a0);
		assertEquals("/c/serviceLines/0/discharge/audits/0", p_a.findParamByPath("/0").getPath());
		
		assertSame(a0, core.getServiceLines().get(0).getDischarge().getAudits().get(0));
	}
	
	@Test
	public void tv01_sanity_check_view_builders() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		Model<UMCaseFlow> vUMCase = ((Model<UMCaseFlow>)q.getView());
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
		
		assertSame(vp_casl_n, vpOneServiceLine.getState());
		assertSame(vp_casl_n, q.getCore().getState().getOneServiceLine());
		
		Param<ServiceLine> cpOneServiceLine = q.getCore().findParamByPath("/oneServiceLine");
		assertNotNull(cpOneServiceLine);
		assertFalse(cpOneServiceLine.isMapped());
		assertTrue(cpOneServiceLine.isNested());
		assertFalse(cpOneServiceLine.isCollection());
		assertFalse(cpOneServiceLine.isCollectionElem());
		
		assertSame(vp_casl_n, cpOneServiceLine.getState());
		assertSame(vpOneServiceLine.getState(), cpOneServiceLine.getState());
		
		Param<ServiceLine> mappedService = q.getView().findParamByPath("/pg3/coreAttachedOneServiceLine/service");
		Param<ServiceLine> mapsToService = q.getCore().findParamByPath("/oneServiceLine/service");
		assertNotSame(mappedService, mapsToService);
		assertSame(mappedService.getState(), mapsToService.getState());
		
	}
	
	@Test
	public void tv02_leaf_v2c() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		
		String _ALOHA = "View Says: ALOHA";
		q.getView().findParamByPath("/pg3/aloha").setState(_ALOHA);
		
		assertSame(_ALOHA, q.getCore().getState().getCaseType());
		assertNotNull(((Model<UMCaseFlow>)q.getView()).getState().getPg3());
		assertNull(((Model<UMCaseFlow>)q.getView()).getState().getPg3().getAloha());		//coz view leaf param is mapped
		assertSame(_ALOHA, q.getCore().findParamByPath("/caseType").getState());
	}
	
	@Test 
	public void tv03_leaf_c2v() throws Exception{
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		
		String _ALOHA = "Core Says: ALOHA";
		q.getCore().findParamByPath("/caseType").setState(_ALOHA);
		//Thread.sleep(1000);
		assertSame(_ALOHA, q.getCore().getState().getCaseType());
		assertNull(((Model<UMCaseFlow>)q.getView()).getState().getPg3().getAloha());		//coz view leaf param is mapped
		assertSame(_ALOHA, q.getView().findParamByPath("/pg3/aloha").getState());
	}
	
	@Test
	public void tv04_col_v2c_just_set_noConversion() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		
		//set list
		List<ServiceLine> newList = new ArrayList<>();
		
		ServiceLine sl_0 = new ServiceLine();
		sl_0.setService("VIEW SAYS: 0th service");
		newList.add(sl_0);
		
		ServiceLine sl_1 = new ServiceLine();
		sl_1.setService("VIEW SAYS: 1st service");
		newList.add(sl_1);
		
		ListParam<ServiceLine> vp_list = q.getRoot().findParamByPath("/v/pg3/noConversionAttachedColServiceLines").findIfCollection();
		assertNotNull(vp_list);
		assertNull(vp_list.getState());
		
		//set
		vp_list.setState(newList);
		assertNotNull(vp_list.getState());
		
		assertNotSame(newList, q.getCore().findParamByPath("/serviceLines").getState());
		assertNotSame(newList, vp_list.getState());
		
		assertSame(2, vp_list.getState().size());
		assertSame(2, vp_list.getType().findIfCollection().getModel().getParams().size()); 
		
		UMCase core = q.getCore().getState();
		
		AtomicInteger counter = new AtomicInteger(0);
		q.getCore().templateParams().find("serviceLines").getType().findIfCollection().getModel().getParams()
			.stream()
			.sequential()
			.peek(p->{
				assertSame(core.getServiceLinesConverted().get(counter.getAndIncrement()), p.getState());
			});
	}

	@Test
	public void tv05_col_v2c_noConversion() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		Model<UMCaseFlow> vUMCase = ((Model<UMCaseFlow>)q.getView());
		assertNotNull(vUMCase);
		
		assertNull(vUMCase.getState());
		
		//if added to core via SAC, should reflect in corresponding view SAC
		ListParam<ServiceLine> vpCoreAttachedServiceLines = q.getView().findParamByPath("/pg3/noConversionAttachedColServiceLines").findIfCollection();
		assertNotNull(vpCoreAttachedServiceLines);
		
		ServiceLine vp_casl_n = new ServiceLine();
		vp_casl_n.setService("It's elementary Watson!");
		vpCoreAttachedServiceLines.add(vp_casl_n);
		
		
		// ensure view models have been initialized
		UMCaseFlow view = ((Model<UMCaseFlow>)q.getView()).getState();
		assertNotNull(view.getPg3());
		assertNotNull(view.getPg3().getNoConversionAttachedColServiceLines());
		//==?? assertNull(view.getPg3().getCoreAttachedServiceLines().get(0));	//mapped direct would not set value into view, only onto mappedCore
		assertEquals(vp_casl_n.getService(), vpCoreAttachedServiceLines.get(0).getService());
		
		// match with core
		UMCase core = q.getCore().getState();
		
		assertSame(core.getServiceLines().size(), vpCoreAttachedServiceLines.size());
		
		ServiceLine sl_n = core.getServiceLines().get(0);
		assertNotNull(sl_n);
		assertSame(vp_casl_n, sl_n);
		assertSame(vp_casl_n.getService(), sl_n.getService());
		assertSame(vp_casl_n, q.getCore().findParamByPath("/serviceLines").findIfCollection().get(0));
	}
	
	@Test
	public void tv06_col_v2c_just_set_conversion() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		
		//set list
		List<Section_ServiceLine> newList = new ArrayList<>();
		
		Section_ServiceLine sl_0 = new Section_ServiceLine();
		sl_0.setService("VIEW SAYS: 0th service");
		newList.add(sl_0);
		
		Section_ServiceLine sl_1 = new Section_ServiceLine();
		sl_1.setService("VIEW SAYS: 1st service");
		newList.add(sl_1);
		
		ListParam<Section_ServiceLine> vp_list = q.getRoot().findParamByPath("/v/pg3/viewAttachedServiceLinesConverted").findIfCollection();
		assertNotNull(vp_list);
		assertNull(vp_list.getState());
		
		//set
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

		UMCase core = q.getCore().getState();
		
		AtomicInteger counter = new AtomicInteger(0);
		q.getCore().templateParams().find("serviceLinesConverted").getType().findIfCollection().getModel().getParams()
			.stream()
			.sequential()
			.peek(p->{
				assertSame(core.getServiceLinesConverted().get(counter.getAndIncrement()).getService(), p.findParamByPath("/service").getState());
			});
	}
	
	@Test
	public void tv07_col_v2c_conversion() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		Model<UMCaseFlow> vUMCase = ((Model<UMCaseFlow>)q.getView());
		assertNotNull(vUMCase);
		
		assertNull(vUMCase.getState());
		
		ListParam<Section_ServiceLine> vpServiceLines2 = q.getRoot().findParamByPath("/v/pg3/viewAttachedServiceLinesConverted").findIfCollection();
		assertNotNull(vpServiceLines2);
		assertSame(0, vpServiceLines2.size());
		
		Section_ServiceLine vpColElem_sl_n0 = new Section_ServiceLine();
		vpColElem_sl_n0.setService("VIEW SAYS: It's elementary Watson!");
		vpServiceLines2.add(vpColElem_sl_n0);
		
		
		UMCase umcase = q.getCore().getState();
		assertNotNull(umcase);
		assertNotNull(umcase.getServiceLinesConverted());
		assertSame(1, umcase.getServiceLinesConverted().size());
		assertSame(1, vpServiceLines2.size());
		assertNotNull(umcase.getServiceLinesConverted().get(0));
		assertTrue(umcase.getServiceLinesConverted().get(0) instanceof ServiceLine);
		assertSame(vpColElem_sl_n0.getService(), umcase.getServiceLinesConverted().get(0).getService());
		
		ListParam<ServiceLine> cpServiceLines = q.getCore().findParamByPath("/serviceLinesConverted").findIfCollection();
		
		ServiceLine cpColElem_sl_n0 = cpServiceLines.get(0);
		assertNotNull(cpColElem_sl_n0);
		assertSame(vpColElem_sl_n0.getService(), cpColElem_sl_n0.getService());
		
	}
	
	@Test
	public void tv08_leaf_c2v_converted() throws Exception {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		
		ServiceLine coreSingle = new ServiceLine();
		coreSingle.setService("Life is Ka!");
		
		q.getRoot().findParamByPath("/c/oneServiceLineConverted").setState(coreSingle);
		assertSame(coreSingle, q.getRoot().findParamByPath("/c/oneServiceLineConverted").getState());
		assertSame(coreSingle, q.getCore().getState().getOneServiceLineConverted());
		//Thread.sleep(1000);
		assertNotNull(q.getRoot().findParamByPath("/v/pg3/viewAttachedOneServiceLineConverted").getState());
		assertNotSame(coreSingle, q.getRoot().findParamByPath("/v/pg3/viewAttachedOneServiceLineConverted").getState());
		assertSame(coreSingle.getService(), q.getRoot().findParamByPath("/v/pg3/viewAttachedOneServiceLineConverted/service").getState());
	}
	
	@Test
	public void tv09_leaf_v2c_converted() throws Exception {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		
		Section_ServiceLine coreSingle = new Section_ServiceLine();
		coreSingle.setService("Life is Ka!");
		
		q.getRoot().findParamByPath("/v/pg3/viewAttachedOneServiceLineConverted").setState(coreSingle);
		assertNotSame(coreSingle, q.getRoot().findParamByPath("/v/pg3/viewAttachedOneServiceLineConverted").getState());
		assertSame(((Model<UMCaseFlow>)q.getView()).getState().getPg3().getViewAttachedOneServiceLineConverted(), q.getRoot().findParamByPath("/v/pg3/viewAttachedOneServiceLineConverted").getState());
		//Thread.sleep(1000);
		assertNotNull(q.getRoot().findParamByPath("/c/oneServiceLineConverted").getState());
		assertNotSame(coreSingle, q.getRoot().findParamByPath("/c/oneServiceLineConverted").getState());
		assertSame(coreSingle.getService(), q.getRoot().findParamByPath("/c/oneServiceLineConverted/service").getState());
	}
	
	@Test
	public void tv10_col_c2v_noConversion() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		Model<UMCaseFlow> vUMCase = ((Model<UMCaseFlow>)q.getView());
		assertNotNull(vUMCase);
		
		assertNull(vUMCase.getState());
		
		//if added to core via SAC, should reflect in corresponding view SAC
		
		ServiceLine sl_n = new ServiceLine();
		sl_n.setService("It's elementary Watson!");
		q.getCore().findParamByPath("/serviceLines").findIfCollection().add(sl_n);
		UMCase core = q.getCore().getState();
		
		ListParam<ServiceLine> vpServiceLines = q.getView().findParamByPath("/pg3/noConversionAttachedColServiceLines").findIfCollection();
		assertNotNull(vpServiceLines);
		assertNotNull(vpServiceLines.getState());
		
		assertSame(core.getServiceLines().size(), vpServiceLines.size());
		assertNotNull(q.getCore().findParamByPath("/serviceLines").getState());
		assertSame(q.getCore().findParamByPath("/serviceLines").getState(), vpServiceLines.getState());
		
		Param<ServiceLine> vpServiceLine_0Param = q.getView().findParamByPath("/pg3/noConversionAttachedColServiceLines/0");
		assertNotNull(vpServiceLine_0Param);
		
		ListElemParam<ServiceLine> vpServiceLine_0 = vpServiceLine_0Param.findIfCollectionElem();
		assertSame(sl_n, vpServiceLine_0Param.getState());
		assertSame(sl_n, vpServiceLines.get(0));
		assertSame(vpServiceLine_0.getState(), vpServiceLines.get(0));
		
		ServiceLine vp_sl_n = vpServiceLines.get(0);
		assertNotNull(vp_sl_n);
		assertSame(sl_n.getService(), vp_sl_n.getService());
	}
	
	@Test
	public void tv11_col_c2v_converted() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		Model<UMCaseFlow> vUMCase = ((Model<UMCaseFlow>)q.getView());
		assertNotNull(vUMCase);
		
		assertNull(vUMCase.getState());
		
		//if added to core via SAC, should reflect in corresponding view SAC
		
		ServiceLine sl_n = new ServiceLine();
		sl_n.setService("It's elementary Watson!");
		q.getCore().findParamByPath("/serviceLinesConverted").findIfCollection().add(sl_n);
		assertSame(sl_n, q.getCore().findParamByPath("/serviceLinesConverted/0").getState());
		
		UMCase core = q.getCore().getState();
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
		assertNotSame(sl_n, vpServiceLines.get(0));
		assertSame(vpServiceLine_0.getState(), vpServiceLines.get(0));
		
		Section_ServiceLine vp_sl_n = vpServiceLines.get(0);
		assertNotNull(vp_sl_n);
		assertSame(sl_n.getService(), q.getView().findParamByPath("/pg3/viewAttachedServiceLinesConverted/0/service").getState());
	}

	@Test
	public void tv12_leaf_detached_c2v_noConversion() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());

		ServiceLine sl_n = new ServiceLine();
		sl_n.setService("It's elementary Watson!");
		q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine.m").setState(sl_n);
		assertSame(sl_n, q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine.m").getState());

		assertSame(sl_n, q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine").getState());
	}

	@Test
	public void tv13_leaf_detached_v2c_noConversion() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());

		ServiceLine sl_n = new ServiceLine();
		sl_n.setService("It's elementary Watson!");
		q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine").setState(sl_n);
		assertSame(sl_n, q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine").getState());

		assertSame(sl_n, q.getView().findParamByPath("/pg3/noConversionDetachedOneServiceLine.m").getState());
	}

	@Test
	public void tv14_leaf_detached_c2v_converted() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		
		ServiceLine coreSingle = new ServiceLine();
		coreSingle.setService("Life is Ka!");
		
		q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine.m").setState(coreSingle);
		assertSame(coreSingle, q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine.m").getState());
		
		assertNotNull(q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine").getState());
		assertNotSame(coreSingle, q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine").getState());
		assertSame(coreSingle.getService(), q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine/service").getState());

	}

	@Test
	public void tv15_leaf_detached_v2c_converted() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());

		Section_ServiceLine viewModel = new Section_ServiceLine();
		viewModel.setService("\"I am a genius\", said the fool");
		
		q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine").setState(viewModel);
		assertNotSame(viewModel, q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine").getState());
		assertSame(q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine").getState(), ((Model<UMCaseFlow>)q.getView()).getState().getPg3().getConvertedDetachedOneServiceLine());

		assertNotNull(q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine.m").getState());
		assertNotSame(viewModel, q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine.m").getState());
		
		assertNotSame(q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine").getState(), 
				q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine.m").getState());
		
		assertSame(q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine/service").getState(), 
				q.getRoot().findParamByPath("/v/pg3/convertedDetachedOneServiceLine.m/service").getState());
		
	}


	@Test
	public void tv16_col_detached_c2v_noConversion() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		Model<UMCaseFlow> vUMCase = ((Model<UMCaseFlow>)q.getView());
		assertNotNull(vUMCase);
		
		assertNull(vUMCase.getState());
		
		//if added to core via SAC, should reflect in corresponding view SAC
		
		ServiceLine sl_n = new ServiceLine();
		sl_n.setService("It's elementary Watson!");
		q.getView().findParamByPath("/pg3/noConversionDetachedColServiceLines.m").findIfCollection().add(sl_n);
		
		ListParam<ServiceLine> vpServiceLines = q.getView().findParamByPath("/pg3/noConversionAttachedColServiceLines").findIfCollection();
		assertNotNull(vpServiceLines);
		assertNotNull(vpServiceLines.getState());
		
		ListParam<ServiceLine> detachedServiceLines = q.getView().findParamByPath("/pg3/noConversionAttachedColServiceLines.m").findIfCollection();
		assertNotNull(detachedServiceLines);
		assertNotNull(detachedServiceLines.getState());
		
		assertSame(detachedServiceLines.size(), vpServiceLines.size());
		assertSame(q.getView().findParamByPath("/pg3/noConversionDetachedColServiceLines.m").getState(), vpServiceLines.getState());
		
		Param<ServiceLine> vpServiceLine_0Param = q.getView().findParamByPath("/pg3/noConversionAttachedColServiceLines/0");
		assertNotNull(vpServiceLine_0Param);
		
		ListElemParam<ServiceLine> vpServiceLine_0 = vpServiceLine_0Param.findIfCollectionElem();
		assertSame(sl_n, vpServiceLine_0Param.getState());
		assertSame(sl_n, vpServiceLines.get(0));
		assertSame(vpServiceLine_0.getState(), vpServiceLines.get(0));
		
		ServiceLine vp_sl_n = vpServiceLines.get(0);
		assertNotNull(vp_sl_n);
		assertSame(sl_n.getService(), vp_sl_n.getService());
	}

	@Test
	public void tv17_col_detached_v2c_noConversion() {
		fail("not yet implemented");
	}

	@Test
	public void tv18_col_detached_c2v_converted() {
		fail("not yet implemented");
	}

	@Test
	public void tv19_col_detached_v2c_converted() {
		fail("not yet implemented");
	}

	
	@After
	public void z_print() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(TestCommandFactory.create_view_icr_UMCaseFlow());
		printJson(q);
		//System.out.println("### Counter: "+ DomainConfigAPITest.eventPublisher.counter);
	}
	
	public void printJson(QuadModel<?, ?> q) {
		String json = JsonUtils.get().convert(q.getView());
		System.out.println("@@@  ");
		System.out.println("@@@  "+ json);
		System.out.println("@@@  ");
	}
}
