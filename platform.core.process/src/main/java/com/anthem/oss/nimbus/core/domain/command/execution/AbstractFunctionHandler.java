/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;

import lombok.Data;

/**
 * @author Rakesh Patel
 *
 */
@Data
public abstract class AbstractFunctionHandler<T, R> implements FunctionHandler<T, R> {

	@Autowired DomainConfigBuilder domainConfigBuilder;
	
	@Autowired ModelRepositoryFactory repFactory;
	
	@Autowired CommandMessageConverter converter;
	
	
	protected ModelConfig<?> getRootDomainConfig(ExecutionContext eCtx) {
		return getDomainConfigBuilder().getRootDomainOrThrowEx(eCtx.getCommandMessage().getCommand().getRootDomainAlias());
	}
	
}
