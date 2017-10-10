/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.definition.Execution.Config;
import com.anthem.oss.nimbus.core.domain.definition.Execution.KeyValue;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.StateEventListener;
import com.anthem.oss.nimbus.core.domain.model.state.internal.BaseStateEventListener;
import com.anthem.oss.nimbus.core.utils.ParamPathExpressionParser;

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
	
	private static final ThreadLocal<String> cmdScopeInThread = new ThreadLocal<>();
	
	public DefaultCommandExecutorGateway(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		
		this.executors = new HashMap<>();
	}
	
	@PostConstruct
	public void initDependencies() {
		this.loader = getBeanResolver().get(ExecutionContextLoader.class);
		this.pathVariableResolver = getBeanResolver().get(CommandPathVariableResolver.class);
	}

	@Override
	public final MultiOutput execute(CommandMessage cmdMsg) {
		// validate
		validateCommand(cmdMsg);
		
		// load execution context 
		ExecutionContext eCtx = loadExecutionContext(cmdMsg);
		
		final String lockId;
		
		if(cmdScopeInThread.get()==null) {
			lockId = UUID.randomUUID().toString();
			cmdScopeInThread.set(lockId);
			eCtx.getRootModel().getExecutionRuntime().onStartRootCommandExecution(cmdMsg.getCommand());
			
		} else {
			lockId = null;
		}
		
		try {
			return executeInternal(eCtx, cmdMsg);
		} finally {
			if(lockId!=null) {
				eCtx.getRootModel().getExecutionRuntime().onStopRootCommandExecution(cmdMsg.getCommand());
				cmdScopeInThread.set(null);
			}
		}
	}
	
	protected MultiOutput executeInternal(ExecutionContext eCtx, CommandMessage cmdMsg) {
		final String inputCommandUri = cmdMsg.getCommand().getAbsoluteUri();
		
		MultiOutput mOutput = new MultiOutput(inputCommandUri, eCtx, cmdMsg.getCommand().getAction(), cmdMsg.getCommand().getBehaviors());
		
		// get execution config
		Param<?> cmdParam = findParamByCommandOrThrowEx(eCtx);
		List<Execution.Config> execConfigs = cmdParam != null ? cmdParam.getConfig().getExecutionConfigs() : null;
		
		// if present, hand-off to each command within execution config
		if(CollectionUtils.isNotEmpty(execConfigs)) {
			List<MultiOutput> execConfigOutputs = executeConfig(eCtx, cmdParam, execConfigs);
			execConfigOutputs.stream().forEach(mOut->addMultiOutput(mOutput, mOut));

		} else {// otherwise, execute self
			List<Output<?>> selfExecOutputs = executeSelf(eCtx, cmdParam);
			selfExecOutputs.stream().forEach(out->addOutput(mOutput, out));
		}
		
		return mOutput;
	}

	protected void validateCommand(CommandMessage cmdMsg) {
		if(cmdMsg==null || cmdMsg.getCommand()==null)
			throw new InvalidArgumentException("Command must not be null for Gateway to process request");
		
		cmdMsg.getCommand().validate();
	}
	
	protected List<MultiOutput> executeConfig(ExecutionContext eCtx, Param<?> cmdParam, List<Execution.Config> execConfigs) {
		final CommandMessage cmdMsg = eCtx.getCommandMessage();
		boolean isPayloadUsed = false;
		
		// for-each config
		final List<MultiOutput> configExecOutputs = new ArrayList<>();
		execConfigs.stream().forEach(ec->{
			if(StringUtils.isNotBlank(ec.col())) {
				buildAndExecuteColExecConfig(eCtx, cmdParam, ec);
			}
			else {
				String completeConfigUri = eCtx.getCommandMessage().getCommand().getRelativeUri(ec.url());
				
				String resolvedConfigUri = pathVariableResolver.resolve(cmdParam, completeConfigUri); 
				Command configExecCmd = CommandBuilder.withUri(resolvedConfigUri).getCommand();
				
				// TODO decide on which commands should get the payload
				CommandMessage configCmdMsg = new CommandMessage(configExecCmd, resolvePayload(cmdMsg, configExecCmd, isPayloadUsed));
				
				// execute & add output to mOutput
				MultiOutput configOutput = execute(configCmdMsg);
				configExecOutputs.add(configOutput);
	//			addMultiOutput(mOutput,configOutput);
			}
		});	
		return configExecOutputs;
	}
	
	private void buildAndExecuteColExecConfig(ExecutionContext eCtx, Param<?> cmdParam, Config ec) {
		List<Execution.Config> colExecConfigs = new ArrayList<>();
		String colPath = ParamPathExpressionParser.stripPrefixSuffix(ec.col());
		
		ListParam listParam = findColParamByPath(cmdParam, colPath);
		
		for(int i=0; i < listParam.size(); i++) {
			colExecConfigs.add(buildExecConfig(ec, colPath, i));
		}
		executeConfig(eCtx, cmdParam, colExecConfigs);
	}

	private ListParam findColParamByPath(Param<?> cmdParam, String colPath) {
		Param<?> p = cmdParam.findParamByPath(colPath);
		if(p == null)
			p = cmdParam.getParentModel().findParamByPath(colPath);
		
		if(p == null)
			throw new InvalidConfigException("The param "+colPath+" not found from command: "+cmdParam);
		
		if(!p.isCollection())
			throw new InvalidConfigException("The param "+colPath+" must be a collection but found to be a non collection from command: "+cmdParam);
		
		return p.findIfCollection();
	}

	private Config buildExecConfig(Config ec, String colPath, int i) {
		return new Execution.Config() {
			
			public String url() {
				return StringUtils.replace(ec.url(),Constants.MARKER_COL_PARAM.code,colPath+Constants.SEPARATOR_URI.code+i);
			}
			public String col() {
				return "";
			}
			@Override
			public KeyValue[] kv() {
				return new KeyValue[]{};
			}
			@Override
		    public Class<? extends Annotation> annotationType() {
		        return Execution.Config.class;
		    }
		};
	}
	
	private String resolvePayload(CommandMessage cmdMsg, Command configExecCmd, boolean isPayloadUsed) {
		if(!isPayloadUsed) {
			if(configExecCmd.hasRawPayload()) {
				return configExecCmd.getRawPayload();
			}
			else if(cmdMsg.getCommand().hasRawPayload()) {
				return cmdMsg.getCommand().getRawPayload();
			}
			else if(cmdMsg.hasPayload()) {
				return cmdMsg.getRawPayload();
			}
		}
		return null;
	}
	
	protected List<Output<?>> executeSelf(ExecutionContext eCtx, Param<?> cmdParam) {
		final CommandMessage cmdMsg = eCtx.getCommandMessage();
		final String inputCommandUri = cmdMsg.getCommand().getAbsoluteUri();
		
		// for-each behavior:
		List<Output<?>> selfExecOutputs = new ArrayList<>();
		cmdMsg.getCommand().getBehaviors().stream().forEach(b->{
			
			// find command executor
			CommandExecutor<?> executor = lookupExecutor(cmdMsg.getCommand(), b);
			
			// execute command
			Input input = new Input(inputCommandUri, eCtx, cmdMsg.getCommand().getAction(), b);
			
			final Set<ParamEvent> _aggregatedEvents = new HashSet<>();
			StateEventListener cmdListener = new BaseStateEventListener() {

				@Override
				public void onStopCommandExecution(Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents) {
					for(ExecutionModel<?> rootKey : aggregatedEvents.keySet()) {
						List<ParamEvent> rawEvents = aggregatedEvents.get(rootKey);
						_aggregatedEvents.addAll(rawEvents);
					}
				}
			};
			
			eCtx.getRootModel().getExecutionRuntime().getEventDelegator().addTxnScopedListener(cmdListener);
			eCtx.getRootModel().getExecutionRuntime().onStartCommandExecution(cmdMsg.getCommand());

			Output<?> output = executor.execute(input);
			output.setAggregatedEvents(_aggregatedEvents);
			selfExecOutputs.add(output);

			eCtx.getRootModel().getExecutionRuntime().onStopCommandExecution(cmdMsg.getCommand());
			eCtx.getRootModel().getExecutionRuntime().getEventDelegator().removeTxnScopedListener(cmdListener);
			
//			mOutput.template().add(output);
			//addOutput(mOutput,output);
			
//			addEvents(eCtx, mOutput.getAggregatedEvents(), input, mOutput);
		});
		return selfExecOutputs;
	}
	
	private void addEvents(ExecutionContext eCtx, Set<ParamEvent> aggregatedEvents, Output<?> output, MultiOutput mOutput) {
		if(CollectionUtils.isEmpty(aggregatedEvents))
			return;
		
		aggregatedEvents.stream()
				.filter(ParamEvent::shouldAllow) //TODO move to listener
				.map(pe->new Output<>(output.getInputCommandUri(), eCtx, pe.getAction(), output.getBehaviors(), pe.getParam()))
				.forEach(mOutput.template()::add);
			;
	}
	
	private void addOutput(MultiOutput mOutput, Output<?> output){
		Object outputValue = output.getValue();
		if(outputValue instanceof MultiOutput){
			MultiOutput mOut = (MultiOutput)outputValue;
			for(Output<?> op: mOut.getOutputs()){
				addOutput(mOutput,op);
			}
		}else{
			mOutput.template().add(output);
			addEvents(output.getContext(), output.getAggregatedEvents(), output, mOutput);
		}
		
	}
	
	private void addMultiOutput(MultiOutput mOutput, MultiOutput newOutput){
		for(Output<?> output :newOutput.getOutputs()){
			addOutput(mOutput,output);
		}
	}	

	
	protected ExecutionContext loadExecutionContext(CommandMessage cmdMsg) {
		// create domain root if needed - for loading execution context
		final Command domainRootCmd;
		if(!cmdMsg.getCommand().isRootDomainOnly()) {
			String domainRootUri = cmdMsg.getCommand().buildUri(Type.DomainAlias);
			domainRootCmd = CommandBuilder.withUri(domainRootUri).stripRequestParams().getCommand();
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
