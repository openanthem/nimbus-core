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
package com.antheminc.oss.nimbus.domain.cmd.exec;

import java.util.Map;
import java.util.Optional;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.HierarchyMatchBasedBeanFinder;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.HierarchyMatch;
import com.antheminc.oss.nimbus.domain.model.state.InvalidStateException;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.support.JustLogit;

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
	
	protected Param<?> getRootDomainParam(ExecutionContext eCtx) {
		String rootDomainAlias = eCtx.getCommandMessage().getCommand().getRootDomainAlias();
		return eCtx.getRootModel().findParamByPath(rootDomainAlias);
	}
	
	protected QuadModel<?,?> getQuadModelOrThrowEx(ExecutionContext eCtx) {
		return Optional.ofNullable(getQuadModel(eCtx))
				.orElseThrow(()->new InvalidStateException("QuadModel cannot be null for execution context: "+eCtx));
	}

	protected QuadModel<?,?> getQuadModel(ExecutionContext eCtx) {
		return eCtx.getQuadModel();
	}

	@SuppressWarnings("unchecked")
	protected <T> Param<T> findParamByCommandOrThrowEx(ExecutionContext eCtx) {
		return (Param<T>) Optional.ofNullable(findParamByCommand(eCtx))
				.orElseThrow(()->new InvalidConfigException("Param state not found for path: "+eCtx.getCommandMessage().getCommand().getAbsoluteAlias()+" from execution context: "+eCtx));
		
	}
	
	@SuppressWarnings("unchecked")
	protected <T> Param<T> findParamByCommand(ExecutionContext eCtx) {
		Command cmd = eCtx.getCommandMessage().getCommand();
		if(cmd.isRootDomainOnly()) 
			return (Param<T>)getQuadModelOrThrowEx(eCtx).getView().getAssociatedParam();
		
		String path = cmd.buildAlias(cmd.getElementSafely(Type.DomainAlias).next());
		
		return getQuadModelOrThrowEx(eCtx).getView().findParamByPath(path);
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
