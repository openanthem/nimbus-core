/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.Map;
import java.util.Optional;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.state.HierarchyMatch;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor
public class BaseCommandExecutorStrategies {

	private final BeanResolverStrategy beanResolver;
	
	private final HierarchyMatchBasedBeanFinder hierarchyMatchBeanLoader;

	protected final JustLogit logit = new JustLogit(this.getClass());

	public BaseCommandExecutorStrategies(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
		this.hierarchyMatchBeanLoader = beanResolver.get(HierarchyMatchBasedBeanFinder.class);
	}
	
	/*
	protected CommandExecutor<?> lookupExecutor(String prefix, String e, Action a, Behavior b) {
		if(e==null) return lookupExecutor(prefix, a, b);
		
		return Optional
			.ofNullable(lookupExecutor(prefix+e, a, b))	//pattern: default.e#pre_new$execute
			.orElse(lookupExecutor(prefix+e));			//pattern: default.e#pre
	}
	*/
	
	protected <T> Param<T> findParamByCommand(ExecutionContext eCtx) {
		Command cmd = eCtx.getCommandMessage().getCommand();
		String path = cmd.buildAlias(cmd.getElement(Type.DomainAlias).get());
		
		return eCtx.getQuadModel().getView().findParamByPath(path);
	}
	
	protected <T> T lookupBeanOrThrowEx(Class<T> type, Map<String, T> localCache, Action a, Behavior b) {
		return Optional.ofNullable(lookupBean(type, localCache, a, b))
					.orElseThrow(()->new InvalidConfigException("Bean of type "+type+" must be configured with bean name following pattern:"
							+ " a) {prefix}."+ a.name() + b.name()+ " OR "
							+ " b) {prefix}."+ b.name()));
	}
	
	protected <T> T lookupBeanOrThrowEx(Class<T> type, Map<String, T> localCache, String name) {
		return Optional.ofNullable(lookupBean(type, localCache, name))
				.orElseThrow(()->new InvalidConfigException("Bean of type "+type+" must be configured with bean name following pattern: "+name));		
	}
	
	protected <T> T lookupBean(Class<T> type, Map<String, T> localCache, Action a, Behavior b) {
		String abName = a.name() + b.name();
		T abExec = lookupBean(type, localCache, abName);
		if(abExec!=null) return abExec;
		
		String bName = b.name();
		T bExec = lookupBean(type, localCache, bName);
		return bExec;
	}

	protected <T> T lookupBean(Class<T> type, Map<String, T> localCache, String name) {
		if(localCache.containsKey(name)) 
			return localCache.get(name);
		
		T exec = beanResolver.find(type, name);
		
		if(exec==null) // don't cache if null
			return null;
		
		localCache.put(name, exec);
		return exec;
	}
	
	public<T extends HierarchyMatch> T findMatchingBean(Class<T> type, String beanIdToFind) {
		return hierarchyMatchBeanLoader.findMatchingBean(type, beanIdToFind);
	}	
	
////	(ProcessTaskExecutor)hierarchyMatchBeanLoader.findMatchingBean(HierarchyMatchProcessTaskExecutor.class, buildLookupKeyFromCommand(cmd))
//
//	
//	private String buildLookupKeyFromCommand(Command command){
////		String uri = command.getAbsoluteUri();
//		
////		String behavior = command.getCurrentBehavior().name();
////		String action = command.getAction().toString();
//		
//		StringBuilder postFix = new StringBuilder();
//		postFix.append(action).append(Constants.SEPARATOR_URI.code).append(behavior);
//		
//		StringBuilder processUri = new StringBuilder();
//		if(StringUtils.isNotBlank(command.getEvent())) {
//			processUri.append(command.getEvent()+Constants.SEPARATOR_URI.code);
//		}
//		processUri.append(uri);
//		processUri.append(Constants.SEPARATOR_URI.code).append(postFix.toString());
//		String processUriString = processUri.toString();
//		return processUriString;
//	}	
}
