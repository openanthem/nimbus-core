/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.config.builder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.config.DefaultDomainConfig;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.builder.EntityConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.config.builder.EntityConfigVistor;
import com.anthem.oss.nimbus.core.util.ClassLoadUtils;
import com.anthem.oss.nimbus.core.util.CollectionsTemplate;
import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Component
@ConfigurationProperties(exceptionIfInvalid=true, prefix="domain.model")
public class DomainConfigBuilder {
	
	private static boolean skip = false;
	
	protected JustLogit logit = new JustLogit(this.getClass());
	
	@Autowired @Getter @Setter
	private EntityConfigBuilder modelConfigBuilder;
	
	@Getter @Setter
	private List<String> basePackages = new ArrayList<String>();
	
	@Getter @Setter
	private List<DefaultDomainConfig> configs;
	
	@Getter @Setter 
	private EntityConfigVistor visitedModels;

	
	@Getter
	private final transient CollectionsTemplate<List<DefaultDomainConfig>, DefaultDomainConfig> templateConfigs = CollectionsTemplate.array(()->getConfigs(), (c)->setConfigs(c));
	
	public DefaultDomainConfig getDomain(String rootAlias) {
		DefaultDomainConfig dc = templateConfigs.find(rootAlias);
		return dc;
	}
	
	/**
	 * Scan and find all classes that are annotated with given base packages or provided input filter
	 * 
	 */
	@PostConstruct
	public void load() {
		if(!DomainConfigBuilder.skip) {
			DomainConfigBuilder.skip = true;
			
			reload();
		}
	}
	
	public void reload() {
		logit.trace(()->"Start-> Load model config...");
		logit.trace(()->"Configured model basePackages: "+getBasePackages());
		
		// reset
		setVisitedModels(null);
		setConfigs(null);
		
		List<String> rootBasePackages = EntityConfigVistor.determineRootPackages(getBasePackages());
		
		if(getVisitedModels()==null) setVisitedModels(new EntityConfigVistor());
		
		rootBasePackages.forEach((basePkg) -> handlePackage(basePkg, getVisitedModels()));
		
		//==domainConfigs.forEach((dc)->modelConfigHandler.applyDefaults());
		logit.trace(()->"End-> Load model config...");
		
	}
	
	public <T> void handlePackage(String basePackage, EntityConfigVistor visitedModels) {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Domain.class));
		
		for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
			String classNm = bd.getBeanClassName();
			
			Class<T> clazz = ClassLoadUtils.loadClass(classNm);
			Domain domain = AnnotationUtils.findAnnotation(clazz, Domain.class);
			
			handleDomainConfig(clazz, domain, visitedModels);
		}
	}
	
	
	private <T> DefaultDomainConfig handleDomainConfig(Class<T> clazz, Domain domain, EntityConfigVistor visitedModels) {
		logit.trace(()->"Processing domain: "+domain);
		
		DefaultDomainConfig dc = templateConfigs.getOrAdd(domain.value(), ()->new DefaultDomainConfig(domain));
		modelConfigBuilder.load(clazz, dc, visitedModels);
		
		return dc;
	}
	
	public ActionExecuteConfig<?, ?> getActionExecuteConfig(Command cmd) {
		DefaultDomainConfig dc = getDomain(cmd.getRootDomainAlias());
		logit.debug(()->"[build] DomainConfig for domainRoot: "+cmd.getRootDomainAlias());
		
		if(dc==null) throw new InvalidConfigException("DomainConfig not found for domainRoot: "+cmd.getRootDomainAlias()+" with Command: "+cmd);
		
		ActionExecuteConfig<?, ?> aec = dc.templateActionConfigs().find(cmd.getAction());
		logit.debug(()->"[build] ActionExecuteConfig for action: "+cmd.getAction());
		
		if(aec==null) throw new InvalidConfigException("ActionExecuteConfig not found for action: "+cmd.getAction()+" with Command: "+cmd);
		
		return aec;
	}
	
	/***
	 * 1. for all actions configured, find missing input or output and if there any defined default for it, throw ex if none
	 * 2. look for default (id OR id & version) in the same ModelConfig associated with the action config
	 * 3. if not found, check if _get is configured and if its Output has defaults (id Or id and versions)
	 * 4. if not found, use static defaults
	 * 
	 * @param dc
	 */
	public void applyDefaults(DefaultDomainConfig dc) {
		for(ActionExecuteConfig<?, ?> aec : dc.getActionExecuteConfigs()) {
			//00
			if(!aec.hasInput() && !aec.hasOutput()) {	//eg: _delete
				
			}
			if(!aec.hasInput()) {
				//1.
				if(ArrayUtils.contains(Execution.InputParam.ID_DEFAULT, aec.getAction())) {
					//2.
					//if(aec.get)
				} else {
					throw throwDefaultNotFoundEx(aec, Execution.Input.class);
				}
			}
			if(!aec.hasOutput()) {
				
			}
		}
		
		//==modelConfigHandler.findDefaultId(aec_get.getOutput().getModel());
	}
	
	protected <T> ModelConfig<T> handleInputDefaults(DefaultDomainConfig dc, ModelConfig<T> otherModelConfig, ActionExecuteConfig<?, ?> aec) {
		if(!ArrayUtils.contains(Execution.InputParam.ID_DEFAULT, aec.getAction())) { 	//1.
			throw throwDefaultNotFoundEx(aec, Execution.Input.class);
		} 
		
		/*
		if(otherModelConfig!=null && otherModelConfig.getIdParam()!=null) {	//2. id
			ModelConfig defaultMConfig = ActionExecuteConfig.oneParamModel(dc.getAlias(), otherModelConfig.getReferredClass(), otherModelConfig.getIdParam());
			return defaultMConfig;
		}
		
		ModelConfig getOutputModelConfig = findGetOutputModel(dc);	//3.
		if(getOutputModelConfig!=null && getOutputModelConfig.getIdParam()!=null) {
			ModelConfig defaultMConfig = ActionExecuteConfig.oneParamModel(dc.getAlias(), getOutputModelConfig.getReferredClass(), getOutputModelConfig.getIdParam());
			return defaultMConfig;
		}
		*/
		
		///==ActionExecuteConfig.default_id(dc.getAlias(), referringClazz)
		return null;
	}
	
	private InvalidConfigException throwDefaultNotFoundEx(ActionExecuteConfig<?, ?> aec, Class<?> type) {
		return new InvalidConfigException("Found "+aec.getAction()+ " with no "+type+" configured and defaults existing in the platform."
				+"\n Algorithm followed:"
				+"\n 1. For all action executions configured, missing input or output is searched. If any, check if default should be applied if available in "+Execution.class
				+"\n	"+Execution.InputParam.ID_DEFAULT+ " -or- "+Execution.OutputParam.ID_DEFAULT
				+"\n 2. Search for default (id OR id & version) in the other (i.e., use Input's if Output is missing) ModelConfig associated with the action config"
				+"\n 3. If not found, check if _get is configured and if its Output has defaults (id Or id and versions)"
				+"\n 4. If not found, use static defaults defined in platform"
				+"\n Throws exception if no defaults are found (usually with _search configurations)");
	}

}
