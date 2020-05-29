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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import java.util.Optional;

import javax.annotation.PostConstruct;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractCommandExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListElemParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

/**
 * @author Rakesh Patel
 * @author Soham Chakravarti
 */
@EnableLoggingInterceptor
public class DefaultActionExecutorDelete extends AbstractCommandExecutor<Boolean> {
	
	private ExecutionContextLoader loader;
	
	public DefaultActionExecutorDelete(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@PostConstruct
	public void initDependencies() {
		this.loader = getBeanResolver().get(ExecutionContextLoader.class);
	}
	
	
	@Override
	protected Output<Boolean> executeInternal(Input input) {
		ExecutionContext eCtx = input.getContext();
		
		Param<Object> p = findParamByCommandOrThrowEx(eCtx);
		
		// handle domain root only 
		if(eCtx.getCommandMessage().getCommand().isRootDomainOnly()) {
			handleRootDelete(p);
			
			loader.unload(eCtx);
		}
		else if(p.isCollection())
			handleCollection(p.findIfCollection());
		else if(p.isCollectionElem())
			handleCollectionElem(p.findIfCollectionElem());
		else
			handleParam(p);
			
		
		return Output.instantiate(input, eCtx, Boolean.TRUE);
	}
	
	protected void handleRootDelete(Param<?> p) {
		ModelConfig<?> rootDomainConfig = p.getRootDomain().getConfig();
		
		Optional.ofNullable(getRepositoryFactory().get(rootDomainConfig)).ifPresent(mRepo -> mRepo._delete(p));
		
		if(rootDomainConfig.isRemote())
			return;
	
		if(rootDomainConfig.isMapped()) {
			Param<?> mapsToParam = p.getRootDomain().findIfMapped().getMapsTo().getAssociatedParam();
			Optional.ofNullable(getRepositoryFactory().get(mapsToParam.getRootDomain().getConfig())).ifPresent(mapsToRepo -> mapsToRepo._delete(mapsToParam));
		}
	}
	
	protected void handleCollection(ListParam<Object> p) {
		p.clear();
	}
	
	protected void handleCollectionElem(ListElemParam<Object> p) {
		p.remove();
	}
	
	protected void handleParam(Param<Object> p) {
		p.setState(null);
	}
} 