/**
 * 
 */
package com.anthem.nimbus.platform.core.domain;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.anthem.nimbus.platform.core.process.api.domain.ModelConfigBuilder;
import com.anthem.nimbus.platform.core.process.api.domain.ModelConfigFactory;
import com.anthem.nimbus.platform.core.process.api.domain.StateAndConfigBuilder;
import com.anthem.nimbus.platform.spec.model.client.access.ClientUserRole;
import com.anthem.nimbus.platform.spec.model.client.user.ClientUser;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ModelStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ParamStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;
import com.anthem.nimbus.platform.spec.model.dsl.binder.TypeStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.CoreModelConfig;
import com.anthem.nimbus.platform.spec.model.view.dsl.config.ViewModelConfig;
import com.anthem.nimbus.platform.spec.model.view.dsl.config.ViewParamConfig;
import com.anthem.oss.nimbus.core.domain.config.DomainConfig;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigAPI;
import com.anthem.oss.nimbus.core.domain.config.builder.ExecutionInputConfigHandler;
import com.anthem.oss.nimbus.core.domain.config.builder.ExecutionOutputConfigHandler;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.anthem.oss.nimbus.core.domain.model.config.builder.ModelConfigVistor;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderSupport;
import com.anthem.oss.nimbus.core.entity.user.flow.ClientUserManagementFlow;
import com.anthem.oss.nimbus.core.entity.user.role.flow.UserRoleManagementFlow;
import com.fasterxml.jackson.databind.ObjectMapper;

import test.com.anthem.nimbus.platform.spec.contract.event.ObservableEventPublisher;

/**
 * @author Soham Chakravarti
 *
 */
public class DomainConfigAPITest {/*

	public static DomainConfigAPI domainConfig;
	public static StateAndConfigBuilder stateBuilder;
	
	public final static ModelConfigVistor visitedModels = new ModelConfigVistor(); 
	
	public final static ObservableEventPublisher eventPublisher = new ObservableEventPublisher(); 
	
	public final static StateAndConfigSupportProvider provider = new StateAndConfigSupportProvider(eventPublisher, null, null);
	
	@BeforeClass
	public static void init() {
		if(domainConfig!=null) return;
		
		domainConfig = new DomainConfigAPI();
		domainConfig.setBasePackages(Arrays.asList(
				UMCase.class.getPackage().getName(),
				ClientUser.class.getPackage().getName(),
				ClientUserRole.class.getPackage().getName(),
				UMCaseFlow.class.getPackage().getName(),
				ClientUserManagementFlow.class.getPackage().getName(),
				UserRoleManagementFlow.class.getPackage().getName()
		));
		
		domainConfig.setModelConfigBuilder(new ModelConfigBuilder());
		domainConfig.getModelConfigBuilder().setExecInputHandler(new ExecutionInputConfigHandler());
		domainConfig.getModelConfigBuilder().setExecOutputHandler(new ExecutionOutputConfigHandler());
		domainConfig.getModelConfigBuilder().setFactory(new ModelConfigFactory());
		
		domainConfig.getModelConfigBuilder().getTypeClassMappings().put("java.time.LocalDate", "date");
		domainConfig.getModelConfigBuilder().getTypeClassMappings().put("java.lang.String", "string");
		
		domainConfig.load();
		
		stateBuilder = new StateAndConfigBuilder();
	}
	
	@Test
	public void test_ConfigService() throws Exception {
		DomainConfig dc = domainConfig.getDomain("um-case");
		Assert.assertNotNull(dc);
		
		CoreModelConfig<UMCase> mConfig = (CoreModelConfig<UMCase>)dc.getActionExecuteConfigs().get(0).getInput().getModel();
		Assert.assertNotNull(mConfig);
		
		ModelStateAndConfig<UMCase, CoreModelConfig<UMCase>> mState = stateBuilder.build(provider, null, mConfig, null);
		Assert.assertNotNull(mState);
		
		UMCase umCase = mState.getCreator().get();
		Assert.assertSame(umCase, mState.getState());
		
		Param<String> requestType = (Param<String>)mState.templateParams().find("requestType");
		Assert.assertNotNull(requestType);
		Assert.assertSame(umCase.getRequestType(), requestType.getState());
		
		requestType.setState("outp");
		Assert.assertSame(umCase.getRequestType(), requestType.getState());
		
		ParamStateAndConfig patient = ((ParamStateAndConfig)mState.templateParams().find("patient"));
		Assert.assertNotNull(patient);
		//Assert.assertSame(umCase.getPatient(), patient.getState());
		
		Assert.assertSame(ParamType.Nested.class, patient.getType().getConfig().getClass());
		
		ModelStateAndConfig patientState = ((ModelStateAndConfig)((TypeStateAndConfig.Nested)patient.getType()).getModel());
		Assert.assertNotNull(patientState);
		
		
		ParamStateAndConfig pSubscriberId = ((ParamStateAndConfig)patientState.templateParams().find("subscriberId"));
		
		ParamStateAndConfig pFirstName = ((ParamStateAndConfig)patientState.templateParams().find("firstName"));
		
		Assert.assertNull(pFirstName.getState());
		
		String PATIENT_SUBS_ID = "A1221";
		pSubscriberId.setState(PATIENT_SUBS_ID);
	
		String PATIENT_FIRST_NM = "John";
		pFirstName.setState(PATIENT_FIRST_NM);
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(mState);
		
		System.out.println(json);
	}
	
	@Test
	public void test_dc_um_case() throws Exception {
		DomainConfig dc = domainConfig.getDomain("flow_um-case");
		Assert.assertNotNull(dc);
	}
	
	@Test
	public void test_dc_user_mgmt() throws Exception {
		DomainConfig dc = domainConfig.getDomain("flow_client-user");
		Assert.assertNotNull(dc);
	}
	
	//@Test
	public void test_FlowConfigService() throws Exception {
		DomainConfig dc = domainConfig.getDomain("flow_um-case");
		Assert.assertNotNull(dc);
		
		ViewModelConfig<UMCaseFlow, UMCase> mConfig = (ViewModelConfig<UMCaseFlow, UMCase>)dc.getActionExecuteConfigs().get(0).getInput().getModel();
		Assert.assertNotNull(mConfig);
		
		//UMCaseFlow flow = createUMCaseFlow();
		ModelStateAndConfig<UMCaseFlow, ViewModelConfig<UMCaseFlow, UMCase>> mState = stateBuilder.build(provider, null, mConfig, null);
		Assert.assertNotNull(mState);
		
		ParamStateAndConfig pg1_vParam = (ParamStateAndConfig)mState.templateParams().find("pg1");
		Assert.assertNotNull(mState);
		
		ModelStateAndConfig pg1_vModel = ((ModelStateAndConfig)((TypeStateAndConfig.Nested)pg1_vParam.getType()).getModel());
		Assert.assertNotNull(pg1_vModel);
		
		ParamStateAndConfig pg1_caseInfo_vParam = (ParamStateAndConfig)pg1_vModel.templateParams().find("caseInfo");
		Assert.assertNotNull(pg1_caseInfo_vParam);
		
		ModelStateAndConfig pg1_caseInfo_vModel = ((ModelStateAndConfig)((TypeStateAndConfig.Nested)pg1_caseInfo_vParam.getType()).getModel());
		Assert.assertNotNull(pg1_caseInfo_vModel);
		
		ParamStateAndConfig pg1_caseInfo_requestType = (ParamStateAndConfig)pg1_caseInfo_vModel.templateParams().find("requestType");
		Assert.assertNotNull(pg1_caseInfo_requestType);
		
		 lookup core model 
		DomainConfig dcCoreUMCase = domainConfig.getDomain("um-case");
		Assert.assertNotNull(dcCoreUMCase);
		
		ModelConfig umCaseConfig = dcCoreUMCase.getActionExecuteConfigs().get(0).getInput().getModel();
		Assert.assertNotNull(dcCoreUMCase);
		
		 match for same instance 
		ParamConfig requestType = (ParamConfig)umCaseConfig.templateParams().find("requestType");
		Assert.assertNotNull(requestType);
		Assert.assertSame(requestType, ((ViewParamConfig)pg1_caseInfo_requestType.getConfig()).getCore());
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(mState);
		
		System.out.println(json);
		
	}
	
	public static class TestLambda {
		
		public TestLambda() {
			System.out.println("@@@ class instantiated...");
		}
		
		public String getSomething() {
			System.out.println("@@ Getter called..");
			return "@@";
		}
		
		public void setSomething(String s) {
			System.out.println("@@ Setter called.." + s);
		}
	}
	
*/}
