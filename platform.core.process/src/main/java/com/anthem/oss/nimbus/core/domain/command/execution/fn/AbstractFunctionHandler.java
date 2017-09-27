/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.fn;

import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandPathVariableResolver;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepositoryFactory;

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
	
	@Autowired CommandPathVariableResolver pathVariableResolver;
	
	
	protected ModelConfig<?> getRootDomainConfig(ExecutionContext eCtx) {
		return getDomainConfigBuilder().getRootDomainOrThrowEx(eCtx.getCommandMessage().getCommand().getRootDomainAlias());
	}
	
}
