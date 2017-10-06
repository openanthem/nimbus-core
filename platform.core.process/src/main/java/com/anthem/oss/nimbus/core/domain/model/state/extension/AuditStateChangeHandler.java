/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.definition.extension.Audit;
import com.anthem.oss.nimbus.core.domain.expr.ExpressionEvaluator;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepositoryFactory;

/**
 * @author Soham Chakravarti
 *
 */
public class AuditStateChangeHandler implements OnStateChangeHandler<Audit> {

	private ExpressionEvaluator expressionEvaluator;
	
	private CommandExecutorGateway commandGateway;
	
	private ModelRepositoryFactory repositoryFactory;
	
	public AuditStateChangeHandler(BeanResolverStrategy beanResolver) {
		this.expressionEvaluator = beanResolver.get(ExpressionEvaluator.class);
		this.commandGateway = beanResolver.get(CommandExecutorGateway.class);
		this.repositoryFactory = beanResolver.get(ModelRepositoryFactory.class);
	}
	
	@Override
	public void handle(Audit configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
		
	}
}
