/**
 * 
 */

package com.anthem.nimbus.platform.core.process.api.cache.session;

/**
 * @author Jayant Chaudhuri
 * Platform Session provides the ability for platform to access session variables. 
 * Platform Session can lookup any existing HTTPSession in the thread and access session variables.
 * For non HTTP requests,  platform session provides the ability to load a session by session id and associate the session
 * with the current thread
 *
 */
public class PlatformSession2 {} /*
	
	private static DomainConfigBuilder domainConfigAPI;
	private static DefaultQuadModelBuilder quadModelBuilder;

	@SuppressWarnings("unchecked")
	private static <R> R getAttribute(String key){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (R)requestAttributes.getAttribute(key,RequestAttributes.SCOPE_REQUEST);
	}

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
	
	public static <R> R getOrThrowEx(Command cmd) {
		return getOrThrowEx(cmd.getRootDomainUri());
	}
	
	private static <R> R getOrThrowEx(String key) {
		R value = getAttribute(key);
		if(value != null) return value;
		
		throw new FrameworkRuntimeException("Required value not found with provided key: "+key);
	}
	
	
	private static QuadModel<?,?> buildQuadModelForStateInCache(Command cmd){
		if(domainConfigAPI == null){
			domainConfigAPI = ProcessBeanResolver.appContext.getBean(DomainConfigBuilder.class);
		}	
		if(quadModelBuilder == null){
			quadModelBuilder = ProcessBeanResolver.appContext.getBean(DefaultQuadModelBuilder.class);
		}	
		ActionExecuteConfig<?, ?> aec = domainConfigAPI.getActionExecuteConfig(cmd);
		ModelConfig<?> mConfig = aec.getOutput().getModel();
		//Class<?> coreClass = mConfig.isMapped() ? mConfig.getRe().value() : mConfig.getReferredClass();
		Class<?> coreClass = mConfig.isMapped() ? mConfig.getReferredClass() : mConfig.getReferredClass(); //TODO commented above line during refactor test. please revisit
		Object core = ClassLoadUtils.newInstance(coreClass);
		
		
		ProcessFlow flowState = ClassLoadUtils.newInstance(ProcessFlow.class);
		
		Class<?> viewClass = mConfig.getReferredClass();
		Object view = ClassLoadUtils.newInstance(viewClass);
		
		//QuadModel<?,?> quadModel = quadModelBuilder.build(cmd, c-> core, v -> view, flow -> flowState);
		QuadModel<?,?> quadModel = null; // TODO commented above line during refactor test. please revisit
		return quadModel;		
	}
	
	private static void putAttribute(String key, Object value, int scope){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		requestAttributes.setAttribute(key, value, scope);
	}
	
	private static Object pullAttribute(String key, int scope){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return requestAttributes.getAttribute(key, scope);
	}
	
	private static void removeAttribute(String key, int scope){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		requestAttributes.removeAttribute(key, scope);
	}	
	
	private static String getExecutionStateExistsKey(String rootDomainUri){
		StringBuilder executionStateExistsKey = new StringBuilder(rootDomainUri);
		executionStateExistsKey.append(".containsExecutionState");
		return executionStateExistsKey.toString();
		
	}
	
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
*/