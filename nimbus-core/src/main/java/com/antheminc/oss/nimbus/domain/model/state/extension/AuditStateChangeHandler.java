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

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.extension.Audit;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.LeafParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.antheminc.oss.nimbus.domain.model.state.repo.IdSequenceRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;
import com.antheminc.oss.nimbus.entity.audit.AuditEntry;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandler;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * WIP: initial implementation. review TODO comments for planned refactor
 * 
 * @author Soham Chakravarti
 *
 */
@EnableLoggingInterceptor
@Getter(AccessLevel.PROTECTED)
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
	
	private final IdSequenceRepository idSequenceRepo;
	
	public AuditStateChangeHandler(BeanResolverStrategy beanResolver) {
		this.repositoryFactory = beanResolver.get(ModelRepositoryFactory.class);
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
		this.javaBeanHandler = beanResolver.get(JavaBeanHandler.class);
		this.idSequenceRepo =  beanResolver.get(IdSequenceRepository.class);
	}
	
	
	@Override
	public boolean shouldAllow(Audit configuredAnnotation, Param<?> param) {
		if(param.getPath().contains(MapsTo.DETACHED_SIMULATED_FIELD_NAME))
			return false; 
		
		return true;
	}
	
	@Override
	public void onStateChange(Audit configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
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
		Long domainRootRefId = Long.valueOf(String.valueOf(persistableDomainRoot.getIdParam().getState()));
		String propertyPath = leafParam.getPath();
		String propertyType = leafParam.getType().getName();
		Object newValue = leafParam.getState();
		
		AuditEntry ae = getJavaBeanHandler().instantiate(configuredAnnotation.value());
		ae.setDomainRootAlias(domainRootAlias);
		ae.setDomainRootRefId(domainRootRefId);
		ae.setPropertyPath(propertyPath);
		ae.setPropertyType(propertyType);
		ae.setNewValue(newValue);

		ModelConfig<?> auditConfig = Optional.ofNullable(getDomainConfigBuilder().getModel(configuredAnnotation.value()))
				.orElseThrow(()->new InvalidConfigException("Annotated AuditEntry class not been loaded by f/w for: "+configuredAnnotation.value()));

		String auditHistoryAlias = findAuditHistoryAlias(auditConfig, configuredAnnotation);
		Repo repo = findAuditHistoryRepo(auditConfig, configuredAnnotation);
		
		ModelRepository db = getRepositoryFactory().get(repo);
		
		// autogenerate id
		String repoAlias = StringUtils.isNotBlank(repo.alias()) ? repo.alias() : auditHistoryAlias;
		Long id = getIdSequenceRepo().getNextSequenceId(repoAlias);
		ae.setId(id);
		
		db._save(repoAlias, ae);
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
