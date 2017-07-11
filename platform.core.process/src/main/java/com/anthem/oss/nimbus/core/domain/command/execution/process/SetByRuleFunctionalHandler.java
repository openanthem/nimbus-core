/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.anthem.oss.nimbus.core.domain.command.execution.AbstractFunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.RulesConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.RulesRuntime;
import com.anthem.oss.nimbus.core.rules.RulesEngineFactory;
import com.anthem.oss.nimbus.core.rules.RulesEngineFactoryProducer;

/**
 * @author Rakesh Patel
 *
 */
public class SetByRuleFunctionalHandler <T, R> extends AbstractFunctionHandler<T, R> {
	
	@Autowired RulesEngineFactoryProducer rulesEngineFactoryProducer;
	
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
		
		rRuntime.fireRules(actionParameter);
		
		rRuntime.shutdown();
		
		return null;
	}
}
