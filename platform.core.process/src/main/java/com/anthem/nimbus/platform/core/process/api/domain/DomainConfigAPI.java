/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.domain;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.dsl.Constants;
import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.anthem.nimbus.platform.spec.model.dsl.config.ActionExecuteConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.DomainConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.exception.InvalidConfigException;
import com.anthem.nimbus.platform.spec.model.util.CollectionsTemplate;
import com.anthem.nimbus.platform.spec.model.util.JustLogit;
import com.anthem.nimbus.platform.spec.model.util.ModelsTemplate;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.ViewDomain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(exceptionIfInvalid=true, prefix="domain.model")
public class DomainConfigAPI {
	
	private static boolean skip = false;
	
	protected JustLogit logit = new JustLogit(this.getClass());
	
	@Autowired @Getter @Setter
	private ModelConfigBuilder modelConfigBuilder;
	
	@Getter @Setter
	private List<String> basePackages = new ArrayList<String>();
	
	@Getter @Setter
	private List<DomainConfig> configs;
	
	@Getter @Setter 
	private ModelConfigVistor visitedModels;

	
	@Getter
	private final transient CollectionsTemplate<List<DomainConfig>, DomainConfig> templateConfigs = CollectionsTemplate.array(()->getConfigs(), (c)->setConfigs(c));
	
	public DomainConfig getDomain(String rootAlias) {
		DomainConfig dc = templateConfigs.find(rootAlias);
		return dc;
	}
	
	/**
	 * Scan and find all classes that are annotated with given base packages or provided input filter
	 * 
	 */
	@PostConstruct
	public void load() {
		if(!DomainConfigAPI.skip) {
			DomainConfigAPI.skip = true;
			logit.trace(()->"Start-> Load model config...");
			logit.trace(()->"Configured model basePackages: "+getBasePackages());
			
			List<String> rootBasePackages = ModelConfigVistor.determineRootPackages(getBasePackages());
			
			if(getVisitedModels()==null) setVisitedModels(new ModelConfigVistor());
			
			rootBasePackages.forEach((basePkg) -> handlePackage(basePkg, getVisitedModels()));
			
			//==domainConfigs.forEach((dc)->modelConfigHandler.applyDefaults());
			logit.trace(()->"End-> Load model config...");
		}
	}
	
	public void handlePackage(String basePackage, ModelConfigVistor visitedModels) {
		handlePackage(
				basePackage, CoreDomain.class, 
				(clazz)->AnnotationUtils.findAnnotation(clazz, CoreDomain.class).value(), 
				visitedModels);
		
		handlePackage(
				basePackage, ViewDomain.class, 
				(clazz)->Constants.PREFIX_FLOW.code + AnnotationUtils.findAnnotation(clazz, ViewDomain.class).value(), 
				visitedModels);
	}
	 
	public <T> void handlePackage(String basePackage, Class<? extends Annotation> annotationClass, Function<Class<T>, String> aliasCb, ModelConfigVistor visitedModels) {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(annotationClass));
		
		for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
			String classNm = bd.getBeanClassName();
			
			Class<T> clazz = ModelsTemplate.loadClass(classNm);
			handleDomainConfig(clazz, aliasCb, visitedModels);
		}
	}
	
	
	private <T> DomainConfig handleDomainConfig(Class<T> clazz, Function<Class<T>, String> aliasCb, ModelConfigVistor visitedModels) {
		String alias = aliasCb.apply(clazz);
		logit.trace(()->"Processing domain with alias: "+alias);
		
		
		DomainConfig dc = templateConfigs.getOrAdd(alias, ()->new DomainConfig(alias));
		modelConfigBuilder.load(clazz, dc, visitedModels);
		
		return dc;
	}
	
	public ActionExecuteConfig<?, ?> getActionExecuteConfig(Command cmd) {
		DomainConfig dc = getDomain(cmd.getRootDomainAlias());
		logit.debug(()->"[build] DomainConfig for domainRoot: "+cmd.getRootDomainAlias()+" -> dc: "+ dc);
		
		if(dc==null) throw new InvalidConfigException("DomainConfig not found for domainRoot: "+cmd.getRootDomainAlias()+" with Command: "+cmd);
		
		ActionExecuteConfig<?, ?> aec = dc.templateActionConfigs().find(cmd.getAction());
		logit.debug(()->"[build] ActionExecuteConfig for action: "+cmd.getAction()+" -> aec: "+ aec);
		
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
	public void applyDefaults(DomainConfig dc) {
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
	
	protected <T> ModelConfig<T> handleInputDefaults(DomainConfig dc, ModelConfig<T> otherModelConfig, ActionExecuteConfig<?, ?> aec) {
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
