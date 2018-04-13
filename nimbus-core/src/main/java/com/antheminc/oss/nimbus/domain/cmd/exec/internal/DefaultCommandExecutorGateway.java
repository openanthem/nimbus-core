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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidArgumentException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.BaseCommandExecutorStrategies;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextPathVariableResolver;
import com.antheminc.oss.nimbus.domain.cmd.exec.ParamPathExpressionParser;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.defn.Execution;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Execution.KeyValue;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.InvalidStateException;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.StateEventListener;
import com.antheminc.oss.nimbus.domain.model.state.internal.BaseStateEventListener;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultCommandExecutorGateway extends BaseCommandExecutorStrategies implements CommandExecutorGateway {
	
	@SuppressWarnings("rawtypes")
	private final Map<String, CommandExecutor> executors;
	
	private CommandPathVariableResolver pathVariableResolver;
	
	private ExecutionContextPathVariableResolver eCtxPathVariableResolver;
	
	private ExecutionContextLoader loader;
	
	private DomainConfigBuilder domainConfigBuilder;
	
	private static final ThreadLocal<String> cmdScopeInThread = new ThreadLocal<>();
	
	public DefaultCommandExecutorGateway(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		
		this.executors = new HashMap<>();
	}
	
	@PostConstruct
	public void initDependencies() {
		this.loader = getBeanResolver().get(ExecutionContextLoader.class);
		this.pathVariableResolver = getBeanResolver().get(CommandPathVariableResolver.class);
		this.eCtxPathVariableResolver = getBeanResolver().get(ExecutionContextPathVariableResolver.class);
		this.domainConfigBuilder = getBeanResolver().get(DomainConfigBuilder.class);
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
			MultiOutput mOut = executeInternal(eCtx, cmdMsg);
			
			if(lockId!=null)
				return createFlattenedOutput(mOut);
			
			return mOut;
		} finally {
			if(lockId!=null) {
				eCtx.getRootModel().getExecutionRuntime().onStopRootCommandExecution(cmdMsg.getCommand());
				cmdScopeInThread.set(null);
			}
		}
	}
	
	private MultiOutput createFlattenedOutput(MultiOutput in) {
		Map<Object, Output<?>> uniqueValues = new LinkedHashMap<>();
		
		flattenOutput(in, uniqueValues);
		
		if(uniqueValues.isEmpty())
			return in;
		
		MultiOutput mOut = new MultiOutput(in.getInputCommandUri(), in.getContext(), in.getAction(), in.getBehaviors());
		
		for(Object value : uniqueValues.keySet()) {
			Output<?> out = uniqueValues.get(value);
			Output<?> newOut = new Output<>(out.getInputCommandUri(), out.getContext(), out.getAction(), out.getBehaviors(), value);
			mOut.template().add(newOut);
			
		}
		
		return mOut;
	}
	
	private void flattenOutput(Output<?> in, Map<Object, Output<?>> uniqueValues) {
		if(in.getValue() != null) {
			uniqueValues.put(in.getValue(), in);
			return;
		}
		
		// multiple
		if(!MultiOutput.class.isInstance(in)) 
			return;
		
		MultiOutput mIn = MultiOutput.class.cast(in);
		if(CollectionUtils.isEmpty(mIn.getOutputs())) {
			return;
		}
					
		for(Output<?> inner : mIn.getOutputs()) {
			flattenOutput(inner, uniqueValues);
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
	
	@Override
	public List<MultiOutput> executeConfig(ExecutionContext eCtx, Param<?> cmdParam, List<Execution.Config> execConfigs) {
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
				
				// TODO Rakesh - Review with soham
				// - e.g. needed to replace e.g. <!page=y!> path variable with the value available in request params (only available in eCtx at this point)
					// can be used for any other values not available in commandParam ??
				String eCtxResolvedConfigUri = eCtxPathVariableResolver.resolve(eCtx, cmdParam, completeConfigUri);
			
				String resolvedConfigUri = pathVariableResolver.resolve(cmdParam, eCtxResolvedConfigUri);
					
				Command configExecCmd = CommandBuilder.withUri(resolvedConfigUri).getCommand();
				
				// TODO decide on which commands should get the payload
				CommandMessage configCmdMsg = new CommandMessage(configExecCmd, resolvePayload(cmdMsg, configExecCmd, isPayloadUsed));
				
				// execute & add output to mOutput
				MultiOutput configOutput = executeConfig(eCtx.getCommandMessage().getCommand(), configCmdMsg);
				
				configExecOutputs.add(configOutput);
				// addMultiOutput(mOutput,configOutput);
			}
		});	
		return configExecOutputs;
	}
	
	private MultiOutput executeConfig(Command inputCmd, CommandMessage configCmdMsg) {
		final String inputDomainRootAlias = inputCmd.buildAlias(Type.DomainAlias);
	
		
		String configDomainAlias = configCmdMsg.getCommand().getRootDomainAlias();
		ModelConfig<?> configDomainModelConfig = domainConfigBuilder.getRootDomainOrThrowEx(configDomainAlias);
		
		
		String configDomainRootAlias = configCmdMsg.getCommand().buildAlias(Type.DomainAlias);
		boolean	matched = StringUtils.equals(inputDomainRootAlias, configDomainRootAlias);
		
		if(!matched && configDomainModelConfig.isMapped()) {
			String mapsToConfigDomainAlias = configDomainModelConfig.findIfMapped().getMapsToConfig().getAlias();
			
			String mappedConfigPlatformUri = configCmdMsg.getCommand().buildAlias(Type.PlatformMarker);
			configDomainRootAlias = mappedConfigPlatformUri + "/" + mapsToConfigDomainAlias;
			
			matched = StringUtils.equals(inputDomainRootAlias, configDomainRootAlias);
		} 
		
		
		if(matched)
			return execute(configCmdMsg);
		
		try {
			return Executors.newSingleThreadExecutor().submit(()->execute(configCmdMsg)).get();
			
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to execute config command in asyn-wait thread for configCmdMsg: "+configCmdMsg+" originating from inputCmd: "+inputCmd, ex);
		}
	}	
	
	private void buildAndExecuteColExecConfig(ExecutionContext eCtx, Param<?> cmdParam, Config ec) {
		List<Execution.Config> colExecConfigs = new ArrayList<>();
		String colPath = ParamPathExpressionParser.stripPrefixSuffix(ec.col());
		
		Param<?> p = findColParamByPath(cmdParam, colPath);
		
		if(!p.isCollection() && !p.getConfig().getType().isArray())
			throw new InvalidConfigException("The param "+colPath+" must be a collection or an array but found to be a non collection/non array from command: "+cmdParam);
			
		if(p.isCollection()) {
			for(int i=0; i < p.findIfCollection().size(); i++) {
				String url = StringUtils.replace(ec.url(),Constants.MARKER_COL_PARAM.code,colPath+Constants.SEPARATOR_URI.code+i);
				colExecConfigs.add(buildExecConfig(url));
			}
		}
		else if(p.getConfig().getType().isArray()) {
			Object[] arrayParamState = (Object[])p.getState();
			
			if(arrayParamState == null)
				throw new InvalidStateException("The state of param "+p+" must not be null, command: "+cmdParam);
			
			int size = ArrayUtils.getLength(arrayParamState);
			for(int i=0; i < size; i++) {
				String url = StringUtils.replace(ec.url(), Constants.MARKER_COL_PARAM_EXPR.code, String.valueOf(arrayParamState[i]));
				colExecConfigs.add(buildExecConfig(url));
			}
		}
		
		executeConfig(eCtx, cmdParam, colExecConfigs);
		
	}

	private Param<?> findColParamByPath(Param<?> cmdParam, String colPath) {
		Param<?> p = cmdParam.findParamByPath(colPath);
		if(p == null)
			p = cmdParam.getParentModel().findParamByPath(colPath);
		
		if(p == null)
			throw new InvalidConfigException("The param "+colPath+" not found from command: "+cmdParam);
		
		return p;
			
	}

	private Config buildExecConfig(String url) {
		return new Execution.Config() {
			
			public String url() {
				return url;
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
