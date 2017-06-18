/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.InvalidArgumentException;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
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
	
	private CommandPathVariableResolver pathVariableResolver;
	
	private ExecutionContextLoader loader;
	
	public DefaultCommandExecutorGateway(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		
		this.executors = new HashMap<>();
	}
	
	@PostConstruct
	public void initDependencies() {
		this.loader = getBeanResolver().get(ExecutionContextLoader.class);
		this.pathVariableResolver = getBeanResolver().get(CommandPathVariableResolver.class);
	}

	
	protected void validateCommand(CommandMessage cmdMsg) {
		if(cmdMsg==null || cmdMsg.getCommand()==null)
			throw new InvalidArgumentException("Command must not be null for Gateway to process request");
		
		cmdMsg.getCommand().validate();
	}
	
	@Override
	public MultiOutput execute(CommandMessage cmdMsg) {
		// validate
		validateCommand(cmdMsg);
		
		final String inputCommandUri = cmdMsg.getCommand().getAbsoluteUri();
		
		// load execution context 
		ExecutionContext eCtx = loadExecutionContext(cmdMsg);
		
		MultiOutput mOutput = new MultiOutput(inputCommandUri, eCtx, cmdMsg.getCommand().getAction(), cmdMsg.getCommand().getBehaviors());
		
		// get execution config
		Param<?> cmdParam = findParamByCommand(eCtx);
		List<Execution.Config> execConfigs = cmdParam != null ? cmdParam.getConfig().getExecutionConfigs() : null;
		
		// if present, hand-off to each command within execution config
		if(CollectionUtils.isNotEmpty(execConfigs)) {
			executeConfig(eCtx, cmdParam, mOutput, execConfigs);

		} else {// otherwise, execute self
			executeSelf(eCtx, cmdParam, mOutput);
		}
		
		return mOutput;
	}
	

	protected void executeConfig(ExecutionContext eCtx, Param<?> cmdParam, MultiOutput mOutput, List<Execution.Config> execConfigs) {
		final CommandMessage cmdMsg = eCtx.getCommandMessage();
		boolean isPayloadUsed = false;
		
		// for-each config
		execConfigs.stream().forEach(ec->{
			String completeConfigUri = eCtx.getCommandMessage().getCommand().getRelativeUri(ec.url());
			
			String resolvedConfigUri = pathVariableResolver.resolve(eCtx, cmdParam, completeConfigUri); 
			Command configExecCmd = CommandBuilder.withUri(resolvedConfigUri).getCommand();
			
			// TODO decide on which commands should get the payload
			CommandMessage configCmdMsg = new CommandMessage(configExecCmd, resolvePayload(cmdMsg, configExecCmd, isPayloadUsed));
			
			// execute & add output to mOutput
			MultiOutput configOutput = execute(configCmdMsg);
			mOutput.template().add(configOutput);
			
		});	
	}
	
	private String resolvePayload(CommandMessage cmdMsg, Command configExecCmd, boolean isPayloadUsed) {
		String payload = null;
		
		if(!isPayloadUsed && cmdMsg.hasPayload())
			payload = cmdMsg.getRawPayload();
		/*
		else if(configExecCmd.getRequestParams()!=null && configExecCmd.getRequestParams().get("a")!=null) {
			String a[] = configExecCmd.getRequestParams().get("a");
			if(a!=null && a.length==1)
				payload = a[0];
		}
		*/
		return payload;
	}
	
	protected void executeSelf(ExecutionContext eCtx, Param<?> cmdParam, MultiOutput mOutput) {
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
		// create domain root if needed - for loading execution context
		final Command domainRootCmd;
		if(!cmdMsg.getCommand().isRootDomainOnly()) {
			String domainRootUri = cmdMsg.getCommand().buildUri(Type.DomainAlias);
			domainRootCmd = CommandBuilder.withUri(domainRootUri).getCommand();
		} else {
			domainRootCmd = cmdMsg.getCommand();
		}
		
		ExecutionContext loaderCtx = loader.load(domainRootCmd);
		
		// create context for passed in command and payload
		ExecutionContext eCtx = new ExecutionContext(cmdMsg, loaderCtx.getQuadModel());
		return eCtx;
	}
	
	protected CommandExecutor<?> lookupExecutor(Command cmd, Behavior b) {
		return lookupBeanOrThrowEx(CommandExecutor.class, executors, cmd.getAction(), b);
	}

}
