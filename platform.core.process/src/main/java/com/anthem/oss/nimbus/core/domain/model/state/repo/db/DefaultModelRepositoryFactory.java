/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.config.DomainConfig;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigAPI;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;


/**
 * Hierarchy based algorithm used to determine custom or platform default implementation
 * 
 * @author Soham Chakravarti
 */
@Component("default.modelRepositoryFactory")
public class DefaultModelRepositoryFactory implements ModelRepositoryFactory, ApplicationContextAware {

	@Autowired DomainConfigAPI domainConfigApi;
	
	private ApplicationContext ctx;
	
	ModelPersistenceHandler persistenceHandler;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}
	
	/**
	 * Finds {@linkplain DomainConfig} for given {@linkplain Command#getRootDomainAlias()}. <br>
	 * Default input {@linkplain Action#_new} is used to determine database type. <br>
	 * Checks if the input model config is mapped and obtains mapped model's referred class, otherwise uses input model's referred class. <br>
	 */
	@Override
	public ModelRepository get(Command cmd) {
		String beanAlias = getBeanAlias(cmd);
		if(!ctx.containsBean(beanAlias)) {
			throw new InvalidConfigException("Found repo that is not configured as a bean: "+beanAlias);
		}
		
		ModelRepository repo = ctx.getBean(beanAlias, ModelRepository.class);
		
		return repo;
	}
	
	@Override
	public ModelRepository get(Class<?> domainEntity) {
		return null;
	}

//	@Override
//	public ModelPersistenceHandler getHandler(Command cmd) {
//		String beanAlias = getBeanAlias(cmd)+"_handler";
//		if(!ctx.containsBean(beanAlias)) {
//			throw new InvalidConfigException("Found repo that is not configured as a bean: "+beanAlias);
//		}
//		
//		ModelPersistenceHandler handler = ctx.getBean(beanAlias, ModelPersistenceHandler.class);
//		
//		return handler;
//	}
	
	@Override
	public ModelPersistenceHandler getHandler(Repo repo) {
		//String beanAlias = getBeanAlias(cmd)+"_handler";
		String beanAlias = repo.value().name()+"_handler";
		if(!ctx.containsBean(beanAlias)) {
			throw new InvalidConfigException("Found repo that is not configured as a bean: "+beanAlias);
		}
		
		ModelPersistenceHandler handler = ctx.getBean(beanAlias, ModelPersistenceHandler.class);
		
		return handler;
	}
	
	private String getBeanAlias(Command cmd) {
		DomainConfig dc = domainConfigApi.getDomain(cmd.getRootDomainAlias());
		ActionExecuteConfig<?, ?> aec = dc.templateActionConfigs().find(Action._new);
		ModelConfig<?> mConfig = aec.getInput().getModel();
		
		Class<?> coreClass = mConfig.isMapped() ? mConfig.getMapsTo().value() : mConfig.getReferredClass();
		Repo repoType = AnnotationUtils.findAnnotation(coreClass, Repo.class);
		
		String beanAlias = repoType.value().name();
		
		return beanAlias;
	}

	

}
