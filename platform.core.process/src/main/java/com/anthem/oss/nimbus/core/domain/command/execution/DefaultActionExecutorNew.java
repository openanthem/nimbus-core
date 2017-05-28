/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.anthem.oss.nimbus.core.utils.JavaBeanHandler;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorNew extends AbstractCommandExecutor<Param<?>> {

	private final DomainConfigBuilder domainConfigBuilder;
	private final QuadModelBuilder quadModelBuilder; 
	private final JavaBeanHandler javaBeanHandler;
	
	public DefaultActionExecutorNew(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		
		this.quadModelBuilder = beanResolver.get(QuadModelBuilder.class);
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
		this.javaBeanHandler = beanResolver.get(JavaBeanHandler.class);
	}
	
	/**
	 * 1. If the command is domain root only, then create new instance <br>
	 * <tab>	1.1. Check if payload contains json for initial object to be inserted; convert if available
	 * <tab>	1.2. Else, create new instance and call rep to persist
	 * <tab>	1.3. Update command with domain root refId	
	 * 2. Else, use the payload of command message json to convert & instantiate desired object <br>
	 * <tab>	2.1. Traverse object model path using command domain uri <br>
	 * <tab>	2.2. Set newly instantiated object and return  	
	 */
	@Override
	public Output<Param<?>> executeInternal(Input input) {
		
		handleNewDomainRoot(input.getContext());
	
		// TODO input JSON to create initial object
		// TODO update refId to Command
		
		Output<Param<?>> output = Output.instantiate(input);
		
		Command cmd = input.getContext().getCommandMessage().getCommand();
		String path = cmd.buildUri(cmd.getElement(Type.DomainAlias).get());
		
		Param<?> p = output.getContext().getRootModel().findParamByPath(path);
		output.setValue(p);
		
		return output;
	}
	
	private void handleNewDomainRoot(ExecutionContext eCtx) {
		if(eCtx.getQuadModel()!=null)
			return;
		
		// create new entity instance for core & view
		ModelConfig<?> rootDomainConfig = domainConfigBuilder.getRootDomainOrThrowEx(eCtx.getCommandMessage().getCommand().getRootDomainAlias());
		Object entity = instantiateEntity(rootDomainConfig);
		
		Object mapsToEntity = rootDomainConfig.isMapped() ? instantiateEntity(rootDomainConfig.findIfMapped().getMapsTo()) : null;
		
		// create quad-model
		ExecutionEntity<?, ?> e = ExecutionEntity.resolveAndInstantiate(entity, mapsToEntity);
		
		QuadModel<?, ?> q = quadModelBuilder.build(eCtx.getCommandMessage().getCommand(), e);
		
		// set to context
		eCtx.setQuadModel(q);
	}
	
	private <T> T instantiateEntity(ModelConfig<T> mConfig) {
		Repo repo = mConfig.getRepo();
		
		return (repo!=null && repo.value()!=Repo.Database.rep_none) ? getRepositoryFactory().get(repo)._new(mConfig) : javaBeanHandler.instantiate(mConfig.getReferredClass());
	}
	
}
