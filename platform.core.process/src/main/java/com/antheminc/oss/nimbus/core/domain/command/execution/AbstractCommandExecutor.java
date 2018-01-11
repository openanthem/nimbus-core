/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution;

import java.beans.PropertyDescriptor;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandElement.Type;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.antheminc.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.model.config.EntityConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.core.domain.model.state.repo.ModelRepositoryFactory;
import com.antheminc.oss.nimbus.core.utils.JavaBeanHandler;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractCommandExecutor<R> extends BaseCommandExecutorStrategies implements CommandExecutor<R> {

	private final QuadModelBuilder quadModelBuilder; 

	private final DomainConfigBuilder domainConfigBuilder;

	private final JavaBeanHandler javaBeanHandler;
	
	private final ModelRepositoryFactory repositoryFactory;
	private final CommandMessageConverter converter;
	
	public AbstractCommandExecutor(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		
		this.repositoryFactory = beanResolver.get(ModelRepositoryFactory.class);
		this.quadModelBuilder = beanResolver.get(QuadModelBuilder.class);
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
		this.javaBeanHandler = beanResolver.get(JavaBeanHandler.class);
		this.converter = beanResolver.get(CommandMessageConverter.class);
	}
	
	@Override
	final public Output<R> execute(Input input) {
		// TODO pre/post/error event generation
		
		return executeInternal(input);
	}
	
	protected abstract Output<R> executeInternal(Input input);


	protected ModelConfig<?> getRootDomainConfig(ExecutionContext eCtx) {
		return getDomainConfigBuilder().getRootDomainOrThrowEx(eCtx.getCommandMessage().getCommand().getRootDomainAlias());
	}

	protected EntityConfig<?> findConfigByCommand(ExecutionContext eCtx) {
		Command cmd = eCtx.getCommandMessage().getCommand();
		
		ModelConfig<?> domainConfig = getRootDomainConfig(eCtx);
		if(cmd.isRootDomainOnly()) 
			return domainConfig;
		
		String path = cmd.buildAlias(cmd.getElement(Type.DomainAlias).get().next());
		
		ParamConfig<?> nestedParamConfig = domainConfig.findParamByPath(path);
		return nestedParamConfig;
	}
	
	protected String resolveEntityAliasByRepo(ModelConfig<?> mConfig) {
		String alias = mConfig.getRepo() != null && StringUtils.isNotBlank(mConfig.getRepo().alias()) 
						? mConfig.getRepo().alias() 
								: mConfig.getAlias();
		return alias;
	}
	
	protected <T> T instantiateEntity(ExecutionContext eCtx, ModelConfig<T> mConfig) {
		/*
		if(eCtx.getCommandMessage().hasPayload())
			return converter.convert(mConfig.getReferredClass(), eCtx.getCommandMessage());
		*/
		Repo repo = mConfig.getRepo();
		
		return (repo!=null && repo.value()!=Repo.Database.rep_none) ? getRepositoryFactory().get(repo)._new(mConfig) : javaBeanHandler.instantiate(mConfig.getReferredClass());
	}
	
	public interface RepoDBCallback<T> {
		public T whenRootDomainHasRepo();
		public T whenMappedRootDomainHasRepo(ModelConfig<?> mapsToConfig); 
	}
	
	protected <T> T determineByRepoDatabase(ModelConfig<?> rootDomainConfig, RepoDBCallback<T> cb) {
		Repo repo = rootDomainConfig.getRepo();
		
		if(Repo.Database.exists(repo)) {
			return cb.whenRootDomainHasRepo();
			
		} else if(rootDomainConfig.isMapped()) {
			ModelConfig<?> mapsToConfig = rootDomainConfig.findIfMapped().getMapsTo();
			Repo mapsToRepo = mapsToConfig.getRepo();
			
			if(Repo.Database.exists(mapsToRepo))
				return cb.whenMappedRootDomainHasRepo(mapsToConfig);
		}
		return null;
	}
	
	protected String getRootDomainRefIdByRepoDatabase(ModelConfig<?> rootDomainConfig, ExecutionEntity<?, ?> e) {
		Object refId = determineByRepoDatabase(rootDomainConfig, new RepoDBCallback<Object>() {
			@Override
			public Object whenRootDomainHasRepo() {
				if(rootDomainConfig.isMapped())  // has core
					return getRefId(rootDomainConfig, rootDomainConfig.getIdParam(), e.getView());  //return q.getView().findParamByPath("/id").getState();
				else
					return getRefId(rootDomainConfig, rootDomainConfig.getIdParam(), e.getCore());  //return q.getCore().findParamByPath("/id").getState();
			}
			
			@Override
			public Object whenMappedRootDomainHasRepo(ModelConfig<?> mapsToConfig) {
				return getRefId(mapsToConfig, mapsToConfig.getIdParam(), e.getCore()); //return q.getCore().findParamByPath("/id").getState();
			}
		});
		
		return Optional.ofNullable(refId)
				.map(String::valueOf)
				.map(StringUtils::trimToNull)
				.orElse(null);
	}
	
	protected ModelConfig<?> getRootConfigByRepoDatabase(ModelConfig<?> rootDomainConfig) {
		return determineByRepoDatabase(rootDomainConfig, new RepoDBCallback<ModelConfig<?>>() {
			@Override
			public ModelConfig<?> whenRootDomainHasRepo() {
				return rootDomainConfig;
			}
			
			@Override
			public ModelConfig<?> whenMappedRootDomainHasRepo(ModelConfig<?> mapsToConfig) {
				return mapsToConfig;
			}
		});
	}

	protected Object getRefId(ModelConfig<?> parentModelConfig, ParamConfig<?> pConfig, Object entity) {
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(parentModelConfig.getReferredClass(), pConfig.getCode());
		
		Object refId = getJavaBeanHandler().getValue(pd, entity);
		return refId;
	}

/*	
	protected <T,R,H extends FunctionHandler<T,R>> R doExecuteFunctionHandler(CommandMessage cmdMsg,Class<H> handlerClass) {
		QuadModel<?, ?> q = findQuad(cmdMsg);
		
		//TODO: Load action parameter based on Command
		Param<T> actionParameter = null;
		ExecutionContext executionContext = new ExecutionContext(cmdMsg,q);
		
		H processHandler = getHandler(cmdMsg, handlerClass);
		return processHandler.execute(executionContext, actionParameter);
	}	
	
	protected <T extends FunctionHandler<?, ?>> T getHandler(CommandMessage commandMessage, Class<T> handlerClass){
		String functionName = constructFunctionHandlerKey(commandMessage);
		return getHandler(functionName, handlerClass);
	}	
	
	protected <T extends FunctionHandler<?, ?>> T getHandler(String functionName, Class<T> handlerClass){
		return hierarchyMatchBeanLoader.findMatchingBean(handlerClass, functionName);
	}		
	
	
	private String constructFunctionHandlerKey(CommandMessage cmdMsg){
		StringBuilder key = new StringBuilder();
		String functionName = cmdMsg.getCommand().getFirstParameterValue(Constants.KEY_FUNCTION.code);
		String absoluteUri = cmdMsg.getCommand().getAbsoluteUri();
		absoluteUri = absoluteUri.replaceAll(Constants.SEPARATOR_URI.code, "\\.");
		key.append(absoluteUri).append(".").append(cmdMsg.getCommand().getAction().toString())
		   .append(cmdMsg.getCommand().getCurrentBehavior().name())
		   .append(Constants.REQUEST_PARAMETER_MARKER.code).append(Constants.KEY_FUNCTION.code).append("=").append(functionName);
		return key.toString();
	}
*/
}
