/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandBuilder;
import com.antheminc.oss.nimbus.core.domain.command.CommandMessage;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.antheminc.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.antheminc.oss.nimbus.core.domain.command.execution.fn.AbstractFunctionHandler;
import com.antheminc.oss.nimbus.core.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.RulesConfig;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.RulesRuntime;
import com.antheminc.oss.nimbus.core.rules.RulesEngineFactory;
import com.antheminc.oss.nimbus.core.rules.RulesEngineFactoryProducer;

/**
 * @author Rakesh Patel
 *
 */
public class SetByRuleFunctionalHandler <T, R> extends AbstractFunctionHandler<T, R> {
	
	@Autowired RulesEngineFactoryProducer rulesEngineFactoryProducer;
	
	@Autowired CommandExecutorGateway executorGateway;
	
	@Override
	public R execute(ExecutionContext eCtx, Param<T> actionParameter) {
		ModelConfig<?> mConfig = getDomainConfigBuilder().getRootDomainOrThrowEx(eCtx.getCommandMessage().getCommand().getRootDomainAlias());
		
		Class<?> clazz = mConfig.getReferredClass();
			
		RulesEngineFactory reFactory = rulesEngineFactoryProducer.getFactory(clazz);
		
		Assert.notNull(reFactory, "Rule engine factory is null for the config "+mConfig);
		
		RulesConfig rConfig = reFactory.createConfig(eCtx.getCommandMessage().getCommand().getFirstParameterValue("rule"));
				
		Assert.notNull(rConfig, "Rule config is null for the rule: "+eCtx.getCommandMessage().getCommand().getFirstParameterValue("rule"));
		
		RulesRuntime rRuntime = reFactory.createRuntime(rConfig);
		rRuntime.start();
		
		List<Param<T>> params = new ArrayList<>();
		params.add(actionParameter);
		
		String[] associatedParamUris = eCtx.getCommandMessage().getCommand().getParameterValue("associatedParam");
		
		if(!ArrayUtils.isEmpty(associatedParamUris)) {
			
			Arrays.asList(associatedParamUris).forEach((associatedParamUri) -> {
				associatedParamUri = eCtx.getCommandMessage().getCommand().getRelativeUri(associatedParamUri);
				Command command = CommandBuilder.withUri(associatedParamUri).getCommand();
				CommandMessage newCommandMessage = new CommandMessage(command, eCtx.getCommandMessage().hasPayload() ?  eCtx.getCommandMessage().getRawPayload() :null);
				MultiOutput response = executorGateway.execute(newCommandMessage);
				
				Param<T> associatedParam = (Param<T>)response.getSingleResult();
				params.add(associatedParam);
			});
		}
		
		rRuntime.fireRules(params.toArray(new Param[params.size()]));
		
		
		rRuntime.shutdown();
		
		return null;
	}
}
