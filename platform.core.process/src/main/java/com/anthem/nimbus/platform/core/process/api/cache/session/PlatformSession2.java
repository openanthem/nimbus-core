/**
 * 
 */

package com.anthem.nimbus.platform.core.process.api.cache.session;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.anthem.nimbus.platform.core.process.api.ProcessBeanResolver;
import com.anthem.nimbus.platform.spec.model.dsl.binder.FlowState;
import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigAPI;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.util.ClassLoadUtils;

/**
 * @author Jayant Chaudhuri
 * Platform Session provides the ability for platform to access session variables. 
 * Platform Session can lookup any existing HTTPSession in the thread and access session variables.
 * For non HTTP requests,  platform session provides the ability to load a session by session id and associate the session
 * with the current thread
 *
 */
public class PlatformSession2 {
	
	private static DomainConfigAPI domainConfigAPI;
	private static QuadModelBuilder quadModelBuilder;

	@SuppressWarnings("unchecked")
	private static <R> R getAttribute(String key){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (R)requestAttributes.getAttribute(key,RequestAttributes.SCOPE_REQUEST);
	}

	/**
	 * 
	 * @param cmd
	 * @return
	 */
	public static <R> R getAttribute(Command cmd) {
		String rootDomainUri = getRootDomainUri(cmd);
		R attribute = (R)getAttribute(rootDomainUri); 
		if(attribute != null){
			return attribute;
		}
		String executionStateExistsKey = getExecutionStateExistsKey(rootDomainUri);
		Object stateExistsInCache = pullAttribute(executionStateExistsKey, RequestAttributes.SCOPE_SESSION);
		if(stateExistsInCache != null){
			QuadModel<?,?> quadModel = buildQuadModelForStateInCache(cmd);
			putAttribute(rootDomainUri, quadModel, RequestAttributes.SCOPE_REQUEST);			
			return (R)quadModel;
		}
		return null;
	}

	/**
	 * 
	 * @param cmd
	 * @param quadModel
	 */
	public static void setAttribute(Command cmd, QuadModel<?,?> quadModel){
		String rootDomainUri = getRootDomainUri(cmd);
		putAttribute(rootDomainUri, quadModel, RequestAttributes.SCOPE_REQUEST);
		
		// Put an indicator in the session to indicate that the execution state for the root domain in available in the session cache
		String executionStateExistsKey = getExecutionStateExistsKey(rootDomainUri);
		if(quadModel != null){
			putAttribute(executionStateExistsKey, Boolean.TRUE, RequestAttributes.SCOPE_SESSION);
		}else{
			removeAttribute(executionStateExistsKey, RequestAttributes.SCOPE_SESSION);
		}
	}
	
     /**
     * 
     * @param cmd
     * @return
     */
	public static <R> R getOrThrowEx(Command cmd) {
		return getOrThrowEx(cmd.getRootDomainUri());
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	private static <R> R getOrThrowEx(String key) {
		R value = getAttribute(key);
		if(value != null) return value;
		
		throw new FrameworkRuntimeException("Required value not found with provided key: "+key);
	}
	
	
	/**
	 * This method creates a quad model for an execution state that exists in the session cache.
	 * This does not populate the state values. Each individual getState call within the system would go to the session cache and 
	 * retrieve the attribute value
	 * @param cmd
	 * @return
	 */
	private static QuadModel<?,?> buildQuadModelForStateInCache(Command cmd){
		if(domainConfigAPI == null){
			domainConfigAPI = ProcessBeanResolver.appContext.getBean(DomainConfigAPI.class);
		}	
		if(quadModelBuilder == null){
			quadModelBuilder = ProcessBeanResolver.appContext.getBean(QuadModelBuilder.class);
		}	
		ActionExecuteConfig<?, ?> aec = domainConfigAPI.getActionExecuteConfig(cmd);
		ModelConfig<?> mConfig = aec.getOutput().getModel();
		Class<?> coreClass = mConfig.isMapped() ? mConfig.getMapsTo().value() : mConfig.getReferredClass();
		Object core = ClassLoadUtils.newInstance(coreClass);
		
		
		FlowState flowState = ClassLoadUtils.newInstance(FlowState.class);
		
		Class<?> viewClass = mConfig.getReferredClass();
		Object view = ClassLoadUtils.newInstance(viewClass);
		
		QuadModel<?,?> quadModel = quadModelBuilder.build(cmd, c-> core, v -> view, flow -> flowState);
		return quadModel;		
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param scope
	 */
	private static void putAttribute(String key, Object value, int scope){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		requestAttributes.setAttribute(key, value, scope);
	}
	
	/**
	 * 
	 * @param key
	 * @param scope
	 * @return
	 */
	private static Object pullAttribute(String key, int scope){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return requestAttributes.getAttribute(key, scope);
	}
	
	/**
	 * 
	 * @param key
	 * @param scope
	 */
	private static void removeAttribute(String key, int scope){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		requestAttributes.removeAttribute(key, scope);
	}	
	
	/**
	 * 
	 * @param rootDomainUri
	 * @return
	 */
	private static String getExecutionStateExistsKey(String rootDomainUri){
		StringBuilder executionStateExistsKey = new StringBuilder(rootDomainUri);
		executionStateExistsKey.append(".containsExecutionState");
		return executionStateExistsKey.toString();
		
	}
	
	/**
	 * 
	 * @param cmd
	 * @return
	 */
	private static String getRootDomainUri(Command cmd){
		String rootDomainUri = cmd.getRootDomainUri();
		String refId = cmd.getElement(Type.DomainAlias).get().getRefId();
		StringBuilder domainUri = new StringBuilder(rootDomainUri);
		if(refId != null){
			domainUri.append(Constants.SEPARATOR_URI_VALUE.code).append(refId);
		}
		return domainUri.toString();
	
	}
	
    
}
