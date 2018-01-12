/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import java.util.Optional;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.extension.Audit;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.LeafParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepositoryFactory;
import com.anthem.oss.nimbus.core.entity.audit.AuditEntry;
import com.anthem.oss.nimbus.core.utils.JavaBeanHandler;

/**
 * WIP: initial implementation. review TODO comments for planned refactor
 * 
 * @author Soham Chakravarti
 *
 */
public class AuditStateChangeHandler implements OnStateChangeHandler<Audit> {

//	private ExpressionEvaluator expressionEvaluator;
//	
//	private CommandExecutorGateway commandGateway;

//	@ConfigConditional(config={
//			@Config(url="/cmcase_audit_history/_new?fn=initEntity", kv={
//					@KeyValue(k="/entityId", v="<!/.d/id!>"), // <! ..evaluate in context of audit_string.. !>
//					@KeyValue(k="/property", v="assignment"), // string literal  v="LocalDateTime.now()"
//					@KeyValue(k="/oldValue", v="<!param(/).getTransientOldState()!>"),  // <! .p1. !> == param(audit_string).findStateByPath(.p1.)
//					@KeyValue(k="/newValue", v="<!/!>"),
//					@KeyValue(k="/newValue", v="/oldValue")  // <! ..evaluate in context of cmcase_audit_history.. !>
//			})
//		})

	private ModelRepositoryFactory repositoryFactory;
	
	private DomainConfigBuilder domainConfigBuilder;
	
	private JavaBeanHandler javaBeanHandler;
	
	public AuditStateChangeHandler(BeanResolverStrategy beanResolver) {
		this.repositoryFactory = beanResolver.get(ModelRepositoryFactory.class);
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
		this.javaBeanHandler = beanResolver.get(JavaBeanHandler.class);
	}
	
	@Override
	public void handle(Audit configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
		LeafParam<?> leafParam = 
				Optional.ofNullable(event).map(ParamEvent::getParam).filter(Param::isLeafOrCollectionWithLeafElems).map(Param::findIfLeaf)
					.orElseThrow(()->new InvalidConfigException("Auditing supported for Leaf parameters, but found configuration for paramEvent: "+ event));

		// mapped no-conversion: skip
		if(leafParam.isMapped() && !leafParam.findIfMapped().requiresConversion())
			return;
		
		// must find @Repo on view or core
		Model<?> persistableDomainRoot = findPersistableDomainRoot(leafParam);
		
		// TODO create simulate @Config entry which will re-use implementation for @ConfigConditional handler to execute
		String domainRootAlias = leafParam.getRootDomain().getConfig().getAlias();
		String domainRootRefId = String.valueOf(persistableDomainRoot.getIdParam().getState());
		String propertyPath = leafParam.getPath();
		String propertyType = leafParam.getType().getName();
		Object oldValue = leafParam.getTransientOldState();
		Object newValue = leafParam.getState();
		
		AuditEntry ae = javaBeanHandler.instantiate(configuredAnnotation.value());
		ae.setDomainRootAlias(domainRootAlias);
		ae.setDomainRootRefId(domainRootRefId);
		ae.setPropertyPath(propertyPath);
		ae.setPropertyType(propertyType);
		ae.setOldValue(oldValue);
		ae.setNewValue(newValue);

		ModelConfig<?> auditConfig = Optional.ofNullable(domainConfigBuilder.getModel(configuredAnnotation.value()))
				.orElseThrow(()->new InvalidConfigException("Annotated AuditEntry class not been loaded by f/w for: "+configuredAnnotation.value()));

		String auditHistoryAlias = findAuditHistoryAlias(auditConfig, configuredAnnotation);
		Repo repo = findAuditHistoryRepo(auditConfig, configuredAnnotation);
		ModelRepository db = repositoryFactory.get(repo);
		
		db._save(auditHistoryAlias, ae);
	}
	
	private String findAuditHistoryAlias(ModelConfig<?> auditConfig, Audit configuredAnnotation) {
		return Optional.ofNullable(auditConfig)
				.map(ModelConfig::getAlias)
				.orElseThrow(()->new InvalidConfigException("Configured Audit Entry must have domain or model alias declared, but not found for: "+configuredAnnotation.value()));
	}
	
	private Repo findAuditHistoryRepo(ModelConfig<?> auditConfig, Audit configuredAnnotation) {
		return Optional.ofNullable(auditConfig)
				.map(ModelConfig::getRepo)
				.orElseThrow(()->new InvalidConfigException("Configured Audit Entry must have Repo declared, but not found for: "+configuredAnnotation.value()));
	}
	
	private Model<?> findPersistableDomainRoot(LeafParam<?> leafParam) {
		Model<?> rootDomain = leafParam.getRootDomain();
		
		// if view
		if(rootDomain.isMapped()) { 
				
			if(Repo.Database.isPersistable(rootDomain.getConfig().getRepo()))
				return rootDomain;
			
			// try core
			Model<?> coreRootDomain = rootDomain.findIfMapped().getMapsTo().getRootDomain();
			if(Repo.Database.isPersistable(coreRootDomain.getConfig().getRepo()))
				return coreRootDomain;
			
			throw new InvalidConfigException("No persistable view or core domain entity found to Audit on for leafParam: "+leafParam);
			
		}
		
		// core only
		if(Repo.Database.isPersistable(rootDomain.getConfig().getRepo()))
			return rootDomain;

		throw new InvalidConfigException("No persistable core domain entity found to Audit on for leafParam: "+leafParam);
	}
}
