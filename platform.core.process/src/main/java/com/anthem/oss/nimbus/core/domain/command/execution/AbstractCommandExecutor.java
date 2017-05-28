/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public abstract class AbstractCommandExecutor<R> extends BaseCommandExecutorStrategies implements CommandExecutor<R> {

	private final ModelRepositoryFactory repositoryFactory;

	public AbstractCommandExecutor(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		this.repositoryFactory = beanResolver.get(ModelRepositoryFactory.class);
	}
	
	@Override
	final public Output<R> execute(Input input) {
		// TODO pre/post/error event generation
		
		return executeInternal(input);
	}
	
	protected abstract Output<R> executeInternal(Input input);

}
