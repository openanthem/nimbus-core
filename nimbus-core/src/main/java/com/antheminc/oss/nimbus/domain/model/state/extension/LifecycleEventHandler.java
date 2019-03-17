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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.bpm.BPMGateway;
import com.antheminc.oss.nimbus.domain.bpm.ProcessRepository;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.extension.Lifecycle;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ValueAccessor;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.entity.process.ProcessFlow;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandler;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandlerUtils;

import lombok.Getter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter
public class LifecycleEventHandler extends AbstractEventHandlerSupport<Lifecycle> {

	private JustLogit logit = new JustLogit(getClass());
	private DomainConfigBuilder domainConfigBuilder;
	private BPMGateway bpmGateway;
	private JavaBeanHandler javaBeanHandler;
	private ProcessRepository processRepo;

	
	public LifecycleEventHandler(BeanResolverStrategy beanResolver) {
		this.domainConfigBuilder = beanResolver.find(DomainConfigBuilder.class);
		this.bpmGateway = beanResolver.find(BPMGateway.class);
		this.javaBeanHandler = beanResolver.find(JavaBeanHandler.class);
		this.processRepo = beanResolver.find(ProcessRepository.class);
	}
	
	@Override
	public void onStateLoad(Lifecycle configuredAnnotation, Param<?> param) {
		loadProcessState(configuredAnnotation,param);
	}
	
	@Override
	public void onStateLoadNew(Lifecycle configuredAnnotation, Param<?> param) {
		loadProcessState(configuredAnnotation,param);
	}
	
	private Object getRefId(ModelConfig<?> parentModelConfig, ParamConfig<?> pConfig, Object entity) {
		ValueAccessor va = JavaBeanHandlerUtils.constructValueAccessor(parentModelConfig.getReferredClass(), pConfig.getCode());
		Object refId = getJavaBeanHandler().getValue(va, entity);
		return refId;
	}
	
	private void loadProcessState(Lifecycle configuredAnnotation, Param<?> param) {
		ExecutionEntity<?,?> e = (ExecutionEntity<?,?>)param.getRootExecution().getState();
		ProcessFlow processFlow = e.getFlow();
		if(processFlow != null)
			return;	
		String domainAlias = param.getConfig().getCode();
		ModelConfig<?> rootDomainConfig = getDomainConfigBuilder().getModel(domainAlias);
		ModelConfig<?> modelConfig = getDomainConfigBuilder().getModel(ProcessFlow.class);
		Repo repo = modelConfig.getRepo();
		String processStateAlias = StringUtils.isBlank(repo.alias()) ? modelConfig.getAlias() : repo.alias();
		
		String entityProcessAlias = domainAlias + "_" + processStateAlias;
		Long refId = null;
		if(rootDomainConfig.isMapped()) {
			ModelConfig<?> mapsToModelConfig = rootDomainConfig.findIfMapped().getMapsToConfig();
			Object mapsToState = param.findIfMapped().getMapsTo().getState();
			refId = (Long)getRefId(mapsToModelConfig, mapsToModelConfig.getIdParamConfig(), mapsToState);
			
		}else {
			refId = (Long)getRefId(rootDomainConfig, rootDomainConfig.getIdParamConfig(), param.getState());
		}		
		processFlow = processRepo._get((Long)refId, ProcessFlow.class, entityProcessAlias);
		if(processFlow == null) {
			processFlow = getBpmGateway().startBusinessProcess(param,configuredAnnotation.name());
			processFlow.setId(refId);
			processRepo._save(processFlow,entityProcessAlias);
		}
		e.setFlow(processFlow);
	}
	
	
}
