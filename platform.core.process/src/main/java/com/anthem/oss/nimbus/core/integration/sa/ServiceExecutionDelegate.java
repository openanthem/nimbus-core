/**
 * 
 */
package com.anthem.oss.nimbus.core.integration.sa;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.ActivitiContext;
import com.anthem.nimbus.platform.core.process.api.ActivitiContext.ServiceActivatorContext;

import lombok.extern.slf4j.Slf4j;



/**
 * @author AC67870
 *
 */
@RefreshScope
@Slf4j
@Component @Scope("prototype")
public class ServiceExecutionDelegate implements JavaDelegate, ApplicationContextAware {

	private static final String PROCESS_ENGINE_GTWY_KEY = "processGatewayContext";
	private static final String NAME_SEPERATOR = "_";
	private static final String REQUEST_NAME_SUFFIX = "_request";
	private static final String RESPONSE_NAME_SUFFIX = "_response";
	
	@Value("${scriptHandlerType:Script}")
	private String SCRIPT_HANDLER;
	
	@Value("${customHandlerType:Custom}")
	private String CUSTOM_HANDLER;
	
	@Value("${ruleBasedRequestHandler:defaultRuleBasedRequestHandler}")
	private String DEFAULT_RULE_REQUEST_HANDLER;
	
	@Value("${ruleBasedResponseHandler:defaultRuleBasedResponseHandler}")
	private String DEFAULT_RULE_RESPONSE_HANDLER;
	
	@Value("${scriptBasedRequestHandler:defaultScriptBasedRequestHandler}")
	private String DEFAULT_SCRIPT_REQUEST_HANDLER;
	
	@Value("${scriptBasedResponseHandler:defaultScriptBasedResponseHandler}")
	private String DEFAULT_SCRIPT_RESPONSE_HANDLER;

	private transient ServiceActivatorHandler<Object> serviceExecutionHandler;
	private transient ServiceActivatorHandler<Object> serviceRequestMappingHandler;
	private transient ServiceActivatorHandler<Object> serviceResponseMappingHandler;

	private Expression serviceName;
	private Expression serviceMethod;
	private Expression executionHandler;
	private Expression requestHandlerType;
	private Expression requestHandlerName;
	private Expression responseHandlerType;
	private Expression responseHandlerName;

	private transient ApplicationContext context;
	
	@Autowired
	private transient ServiceExecutionHelper helper;
	
	@Override
	public void execute(DelegateExecution execution) {
		ActivitiContext aCtx = (ActivitiContext) execution.getVariable(PROCESS_ENGINE_GTWY_KEY);
		
		helper.setACtx(aCtx);
		
		initializeHandlers(execution);

		ServiceActivatorContext sa = aCtx.createAndAddServiceActivatorContext(new ServiceActivatorDefinition() {

			@Override
			public String getServiceName() {
				return (String) serviceName.getValue(execution);
			}

			@Override
			public String getServiceMethod() {
				return (String) serviceMethod.getValue(execution);
			}

			@Override
			public String getRequestHandler() {
				return requestHandlerName != null ? (String) requestHandlerName.getValue(execution)
						: getServiceName() + NAME_SEPERATOR + getServiceMethod() + REQUEST_NAME_SUFFIX;
			}

			@Override
			public String getResponseHandler() {
				return responseHandlerName != null ? (String) responseHandlerName.getValue(execution)
						: getServiceName() + NAME_SEPERATOR + getServiceMethod() + RESPONSE_NAME_SUFFIX;
			}
		});

		sa.setDelegateExecution(execution);
		sa.setId(execution.getCurrentActivityId()); /* can be used to look up service activator contexts created until the current execution step. pass the task id
													 (configured in the BPMN) to look up. */
		helper.setActivityId(sa.getId());

		sa.setRequestExecutionHolder(sa.new Holder());
		handleCurrentExecutionStep(sa.getRequestExecutionHolder(), serviceRequestMappingHandler, sa);

		sa.setServiceExecutionHolder(sa.new Holder());
		try{
			handleCurrentExecutionStep(sa.getServiceExecutionHolder(), serviceExecutionHandler, sa);
		}
		catch(Exception ex) {
			log.error("The ServiceExecution step failed and the exception will be output as the processEngine output",ex);
			aCtx.getProcessEngineContext().setOutput(ex);
			return;
		}
		
		sa.setResponseExecutionHolder(sa.new Holder());
		Object output = handleCurrentExecutionStep(sa.getRequestExecutionHolder(), serviceResponseMappingHandler, sa);

		aCtx.getProcessEngineContext().setOutput(output);

	}
	

	@SuppressWarnings("unchecked")
	private void initializeHandlers(DelegateExecution execution) {

		String handlerName = (String) executionHandler.getValue(execution);
		serviceExecutionHandler = (ServiceActivatorHandler<Object>) context.getBean(handlerName);

		String rqstHandlerType = (String) requestHandlerType.getValue(execution);
		if (StringUtils.equalsIgnoreCase(rqstHandlerType, CUSTOM_HANDLER)) {
			String rqstHandlerName = (String) requestHandlerName.getValue(execution);
			serviceRequestMappingHandler = (ServiceActivatorHandler<Object>) context.getBean(rqstHandlerName);
		} else if (StringUtils.equalsIgnoreCase(rqstHandlerType, SCRIPT_HANDLER)) {
			serviceRequestMappingHandler = (ServiceActivatorHandler<Object>) context.getBean(DEFAULT_SCRIPT_REQUEST_HANDLER);
		} else {
			serviceRequestMappingHandler = (ServiceActivatorHandler<Object>) context.getBean(DEFAULT_RULE_REQUEST_HANDLER);
		}

		if (responseHandlerType != null) {
			String rspHandlerType = (String) responseHandlerType.getValue(execution);

			if (StringUtils.equalsIgnoreCase(rspHandlerType, CUSTOM_HANDLER)) {
				String rspHandlerName = (String) responseHandlerName.getValue(execution);
				serviceResponseMappingHandler = (ServiceActivatorHandler<Object>) context.getBean(rspHandlerName);
			} else if (StringUtils.equalsIgnoreCase(rspHandlerType, SCRIPT_HANDLER)) {
				serviceResponseMappingHandler = (ServiceActivatorHandler<Object>) context.getBean(DEFAULT_SCRIPT_RESPONSE_HANDLER);
			} else {
				serviceResponseMappingHandler = (ServiceActivatorHandler<Object>) context.getBean(DEFAULT_RULE_RESPONSE_HANDLER);
			}
		}

	}

	private <T> T handleCurrentExecutionStep(ServiceActivatorContext.Holder holder,
			ServiceActivatorHandler<T> handler, ServiceActivatorContext input) throws ServiceActivatorException {
		try {
			holder.setInput(input);
			T output = handler.handle(input);
			holder.setOutput(output);
			return output;
		} catch (Exception ex) {
			holder.setException(ex);
			if(ex instanceof ServiceActivatorException) {
				throw ex;
			}
			throw new ServiceActivatorException(ex);
		}
	}

//	private <T> T handleResponseStep(
//			ServiceActivatorContext.Holder holder, ServiceActivatorHandler<T> handler,
//			ServiceActivatorContext input) throws ServiceActivatorException {
//		try {
//			holder.setInput(input);
//			T output = handler.handle(input);
//			holder.setOutput(output);
//			return output;
//		} catch (Exception ex) {
//			holder.setException(ex);
//			throw ex;
//		}
//	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

}
