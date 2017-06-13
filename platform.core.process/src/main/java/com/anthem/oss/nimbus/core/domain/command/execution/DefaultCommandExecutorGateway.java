/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.utils.ParamPathExpressionParser;

/**
 * @author Soham Chakravarti
 *
 */
@RefreshScope
public class DefaultCommandExecutorGateway extends BaseCommandExecutorStrategies implements CommandExecutorGateway {
	
	@SuppressWarnings("rawtypes")
	private final Map<String, CommandExecutor> executors;
	
	private ExecutionContextLoader loader;
	
	public DefaultCommandExecutorGateway(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		
		this.executors = new HashMap<>();
	}
	
	@PostConstruct
	public void initDependencies() {
		this.loader = getBeanResolver().get(ExecutionContextLoader.class);
	}

	
	@Override
	public MultiOutput execute(CommandMessage cmdMsg) {
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
			
			String resolvedConfigUri = replaceVariables(cmdParam, completeConfigUri); 
			Command configExecCmd = CommandBuilder.withUri(resolvedConfigUri).getCommand();
			
			// TODO decide on which commands should get the payload
			CommandMessage configCmdMsg = isPayloadUsed ? new CommandMessage(configExecCmd, null) : new CommandMessage(configExecCmd, cmdMsg.getRawPayload());
			
			// execute & add output to mOutput
			MultiOutput configOutput = execute(configCmdMsg);
			mOutput.template().add(configOutput);
			
		});	
	}
	
	

	protected static String replaceVariables(Param<?> cmdParam, String in) {
		Map<Integer, String> entries = ParamPathExpressionParser.parse(in);
		if(MapUtils.isEmpty(entries))
			return in;
		
		String out = in;
		for(Integer i : entries.keySet()) {
			String key = entries.get(i);
			
			// look for relative path to passed in param's parent model
			String path = ParamPathExpressionParser.stripPrefixSuffix(key);
			Param<?> p = cmdParam.getParentModel().findParamByPath(path);
			
			String val = String.valueOf(p.getState());
			
			out = StringUtils.replace(out, key, val, 1);
		}
		
		return out;
	}
	
	protected void executeSelf(ExecutionContext eCtx, Param<?> cmdParam, MultiOutput mOutput) {
		final CommandMessage cmdMsg = eCtx.getCommandMessage();
		final String inputCommandUri = cmdMsg.getCommand().getAbsoluteUri();
		
		// for-each behavior:
		if(CollectionUtils.isEmpty(cmdMsg.getCommand().getBehaviors())) {
			cmdMsg.getCommand().templateBehaviors().add(Behavior.$execute);
		}
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
