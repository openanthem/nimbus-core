/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.context.config.annotation.RefreshScope;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;

/**
 * @author Soham Chakravarti
 *
 */
@RefreshScope
public class DefaultCommandExecutorGateway extends BaseCommandExecutorStrategies implements CommandExecutorGateway {
	
	@SuppressWarnings("rawtypes")
	private final Map<String, CommandExecutor> executors;
	
	private final ExecutionContextLoader loader;
	
	
	public DefaultCommandExecutorGateway(BeanResolverStrategy beanResolver, HierarchyMatchBasedBeanFinder hierarchyMatchBeanLoader) {
		super(beanResolver);
		
		this.executors = new HashMap<>();
		this.loader = beanResolver.get(ExecutionContextLoader.class);
	}

	
	@Override
	public MultiOutput execute(CommandMessage cmdMsg) {
		final String inputCommandUri = cmdMsg.getCommand().getAbsoluteUri();
		
		// load execution context 
		ExecutionContext eCtx = loadExecutionContext(cmdMsg);
		
		MultiOutput mOutput = new MultiOutput(inputCommandUri, eCtx, cmdMsg.getCommand().getAction(), cmdMsg.getCommand().getBehaviors());
		
		// for-each behavior: 
		cmdMsg.getCommand().getBehaviors().stream().forEach(b->{
			
			// find command executor
			CommandExecutor<?> executor = lookupExecutor(cmdMsg.getCommand(), b);
			
			// execute command
			Input input = new Input(inputCommandUri, eCtx, cmdMsg.getCommand().getAction(), b);
			Output<?> output = executor.execute(input);			
			
			mOutput.template().add(output);
		});

		return mOutput;
	}

	protected ExecutionContext loadExecutionContext(CommandMessage cmdMsg) {
		return loader.load(cmdMsg);
	}
	
	protected CommandExecutor<?> lookupExecutor(Command cmd, Behavior b) {
		return lookupBeanOrThrowEx(CommandExecutor.class, executors, cmd.getAction(), b);
	}

}
