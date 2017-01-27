/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.domain;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Constraint;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.anthem.nimbus.platform.spec.model.dsl.ConfigNature;
import com.anthem.nimbus.platform.spec.model.dsl.Model;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.dsl.config.AbstractParamConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.AnnotationConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.CoreModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.DomainConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamType;
import com.anthem.nimbus.platform.spec.model.util.GenericUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Soham Chakravarti
 *
 */ 
@Component
@Slf4j
@ConfigurationProperties(exceptionIfInvalid=true, prefix="domain.model")
public class ModelConfigBuilder {

	@Autowired @Getter @Setter ExecutionInputConfigHandler execInputHandler;
	@Autowired @Getter @Setter ExecutionOutputConfigHandler execOutputHandler;
	
	@Getter @Setter private Map<String, String> typeClassMappings = new HashMap<>();
	
	@Autowired @Getter @Setter ModelConfigFactory factory;
	
	@PostConstruct
	public void loadMappings() {
		typeClassMappings.put(LocalDate.class.getName(), "date");
	}
	
	/**
	 * Load available configuration for the domain entities
	 * */
	public <T> ModelConfig<T> load(Class<T> clazz, DomainConfig dc, ModelConfigVistor visitedModels) {
		if(log.isTraceEnabled()) {
			log.trace("Loading config from class: "+clazz);
		}
		
		
		/* 1. Skip if class was already visited, otherwise handle model */
		ModelConfig<T> mConfig = (visitedModels.contains(clazz)) ? visitedModels.get(clazz) : handleModel(clazz, visitedModels);
		
		/* 2. Add {Action} execution input config */
		execInputHandler.loadClassConfigs(dc, mConfig);
		
		
		/* 3. Add {Action} execution output config */
		execOutputHandler.loadClassConfigs(dc, mConfig);
		
		/* 3. Add {Action} validation $config */
		
		/* mark model visited */
		//dc.markVisited(clazz, mConfig);
		
		return mConfig;
	}
	
	public <T> ModelConfig<T> handleModel(Class<T> clazz, ModelConfigVistor visitedModels) {
		/* handle @MapsTo */
		//TODO-DONE 2. Look for class and path provided in this annotation. Load config for this first. Pass reference to MapsTo class to ParamHandler. 
		//TODO 3. In clone methods, check if the SAME instance is already cloned, if so - then lookup the corresponding cloned instance and set the same reference instead of cloning again. This will retain the intended object reference
		
		//TODO-DONE 6. Create ModelConfigView wrapper class similar to ParamConfigView wrapper if the Model is annotated with @ViewComponent in any of the annotation inheritance
		CoreModelConfig<T> mConfig = factory.createModel(clazz, visitedModels);
		
		//handle repo
		Repo rep = AnnotationUtils.findAnnotation(clazz, Repo.class);
		mConfig.setRepo(rep);
		
		//look if the model is marked with MapsTo
		if(mConfig.isMapped()) {
			
			//load mapped class config
			if(!visitedModels.contains(mConfig.getMapsTo().value())) {
				handleModel(mConfig.getMapsTo().value(), visitedModels);
			}
		}
		
		List<Field> fields = FieldUtils.getAllFieldsList(clazz);
		if(fields==null) return mConfig;
		
		fields.forEach((f) -> mConfig.templateParams().add(handleParamConfig(mConfig, f, visitedModels)));
		
		visitedModels.set(clazz, mConfig);
		return mConfig;
	}

	protected <T> ParamConfig<?> handleParamConfig(ModelConfig<T> mConfig, Field f, ModelConfigVistor visitedModels) {
		
		/* handle ignore field */
		if(AnnotatedElementUtils.isAnnotated(f, ConfigNature.Ignore.class)) return null;
		if("serialVersionUID".equals(f.getName())) return null;

		final AbstractParamConfig<?> pConfig = factory.createParam(mConfig, f, visitedModels);
		
		/* if param isMapped AND isLeaf, then skip:: factory created instance would have type as null if this condition is valid */
		if(pConfig.getType()!=null) {
			return pConfig;
		}
		
		String value ="";
		if(AnnotatedElementUtils.isAnnotated(f, Model.Param.Text.class)) {
			Model.Param.Text aVal = AnnotationUtils.getAnnotation(f, Model.Param.Text.class);
			
			//Model.Param.String srcValues = ModelsTemplate.newInstance(aVal.value());
			value = aVal.label();
		}
		//not mapped Param
		ParamConfig.Desc desc = new ParamConfig.Desc();
		desc.setHelp(pConfig.getCode());
		desc.setHint(pConfig.getCode());
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(value)){
			desc.setLabel(value);
		}
		else{
			desc.setLabel(pConfig.getCode());
		}
		
		pConfig.setDesc(desc);

		List<AnnotationConfig> vConfig = handleValidation(f);
		pConfig.setValidations(vConfig);
		
		ParamType type = handleParamType(mConfig, pConfig, f, visitedModels);
		pConfig.setType(type);
		
		/*
		//default id
		if(AnnotatedElementUtils.isAnnotated(f, Id.class)) {
			mConfig.setIdParam(pConfig);
		} 
		//default version
		if(AnnotatedElementUtils.isAnnotated(f, Version.class)) {
			mConfig.setVersionParam(pConfig);
		}
		*/
		return pConfig;
	}
	
	/**
	 * 3. For each field, detect all validators - at field and write-method validators
	 * 		Validator Detection logic: read a)package names b)Annotation markers c)Specific classes
	 * 			1. javax.validation
	 * 			2. org.hibernate.validator.constraints
	 */
	public List<AnnotationConfig> handleValidation(AnnotatedElement aElem) {
		return AnnotationConfigHandler.handle(aElem, Constraint.class);
	}
	
	private static ParamType.CollectionType determineCollectionType(Class<?> clazz) {
		if(clazz.isArray()	||	//Array
				Collection.class.isAssignableFrom(clazz)) { //Collection
			return ParamType.CollectionType.list;
			
		} else if(Page.class.isAssignableFrom(clazz)) {	//Page
			return ParamType.CollectionType.page;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected <T, P> ParamType handleParamType(ModelConfig<T> mConfig, ParamConfig pConfig, Field f, ModelConfigVistor visitedModels) {
		
		final ParamType.CollectionType collection = determineCollectionType(f.getType());	

		final Class<P> determinedType = (Class<P>)GenericUtils.resolveGeneric(mConfig.getReferredClass(), f.getDeclaringClass(), f.getType(), f.getGenericType());
		
		final ParamType pType;
		if(ClassUtils.isPrimitiveOrWrapper(determinedType) || String.class==determinedType) {	//Primitives bare or with wrapper & String
			String name = ClassUtils.getShortNameAsProperty(ClassUtils.resolvePrimitiveIfNecessary(determinedType));
			pType = new ParamType.Field(name, f.getType());

		} else if(lookUpTypeClassMapping(determinedType)!=null) { //custom mapping overrides
			String name = lookUpTypeClassMapping(determinedType);
			pType = new ParamType.Field(name, f.getType());
			
		} else if(AnnotationUtils.findAnnotation(determinedType, Model.class)!=null ||	//Classes annotated with @Model 
					com.anthem.nimbus.platform.spec.model.Model.class.isAssignableFrom(determinedType)) {	//Classes implementing Model interface

			String name = ClassUtils.getShortName(determinedType);
			
			final ParamType.Nested<P> model; 
			pType = model = new ParamType.Nested<>(name, f.getType());
			
			final ModelConfig<P> nmConfig; 
			if(mConfig.getReferredClass()==determinedType) { //nested reference to itself
				nmConfig = (ModelConfig<P>)mConfig;
				
			} else if(visitedModels.contains(determinedType)) { //any nested model in hierarchy pointing back to any of its parents
				nmConfig = visitedModels.get(determinedType);
				
			} else {
				nmConfig = handleModel(determinedType, visitedModels);
			}
			
			model.setModel(nmConfig);
			
		} else { //All others: Treat as field type instead of complex object that requires config traversal
			pType = new ParamType.Field(ClassUtils.getShortName(determinedType), f.getType());
			
		}
		
		pType.setCollection(collection);
		return pType;
	}
	
	public String lookUpTypeClassMapping(Class<?> clazz) {
		if(getTypeClassMappings()==null) return null;
		
		String mapping = getTypeClassMappings().get(clazz.getName());
		return mapping;
	}
	
	
	/**
	 * Apply defaults after available domain configs have been loaded. Defaults would be applied for all actions which don't have an explicit configuration provided.
	 * */
	public void applyDefaults() {
		
	}
}
