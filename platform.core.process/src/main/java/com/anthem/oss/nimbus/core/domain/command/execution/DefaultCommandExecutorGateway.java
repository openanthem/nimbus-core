/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
@RefreshScope
public class DefaultCommandExecutorGateway extends BaseCommandExecutorStrategies implements CommandExecutorGateway {
	
	@SuppressWarnings("rawtypes")
	private final Map<String, CommandExecutor> executors;
	
	private final ExecutionContextLoader loader;
	
	
	public DefaultCommandExecutorGateway(BeanResolverStrategy beanResolver) {
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
		
		// get execution config
		Param<?> p = findParamByCommand(eCtx);
		List<Execution.Config> execConfigs = p.getConfig().getExecutionConfigs();
		
		// if present, hand-off to each command within execution config
		if(CollectionUtils.isNotEmpty(execConfigs)) {
			executeConfig(eCtx, mOutput, execConfigs);

		} else {// otherwise, execute self
			executeSelf(eCtx, mOutput);
		}
		
		return mOutput;
	}
	

	protected void executeConfig(ExecutionContext eCtx, MultiOutput mOutput, List<Execution.Config> execConfigs) {
		final CommandMessage cmdMsg = eCtx.getCommandMessage();
		
		// for-each config
		execConfigs.stream().forEach(ec->{
			
			// prepare config command 
			String configExecPath = StringUtils.contains(ec.url(), Constants.SEPARATOR_URI_PLATFORM.code+Constants.SEPARATOR_URI.code)  // check if url has "/p/" 
										? eCtx.getCommandMessage().getCommand().buildAlias(Type.PlatformMarker) + ec.url() : ec.url();
										
			Command configExecCmd = CommandBuilder.withUri(configExecPath).getCommand();
			
			// TODO decide on which commands should get the payload
			CommandMessage configCmdMsg = new CommandMessage(configExecCmd, cmdMsg.getRawPayload());
			
			// execute & add output to mOutput
			MultiOutput configOutput = execute(configCmdMsg);
			mOutput.template().add(configOutput);
			
		});	
	}
	
	protected void executeSelf(ExecutionContext eCtx, MultiOutput mOutput) {
		final CommandMessage cmdMsg = eCtx.getCommandMessage();
		final String inputCommandUri = cmdMsg.getCommand().getAbsoluteUri();
		
		// for-each behavior: 
		cmdMsg.getCommand().getBehaviors().stream().forEach(b->{
			
			// find command executor
			CommandExecutor<?> executor = lookupExecutor(cmdMsg.getCommand(), b);
			
			// execute command
			Input input = new Input(inputCommandUri, eCtx, cmdMsg.getCommand().getAction(), b);
			Output<?> output = executor.execute(input);			
			
			mOutput.template().add(output);
		});
	}

	protected ExecutionContext loadExecutionContext(CommandMessage cmdMsg) {
		return loader.load(cmdMsg);
	}
	
	protected CommandExecutor<?> lookupExecutor(Command cmd, Behavior b) {
		return lookupBeanOrThrowEx(CommandExecutor.class, executors, cmd.getAction(), b);
	}

}
