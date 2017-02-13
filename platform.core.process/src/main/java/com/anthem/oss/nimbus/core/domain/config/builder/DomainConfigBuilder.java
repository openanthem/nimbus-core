/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.config.builder;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.config.DomainConfig;
import com.anthem.oss.nimbus.core.domain.definition.ConfigNature;
import com.anthem.oss.nimbus.core.domain.model.config.DefaultModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.DefaultParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.anthem.oss.nimbus.core.domain.model.config.builder.AbstractModelConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.config.builder.ModelConfigVistor;
import com.anthem.oss.nimbus.core.util.GenericUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */ 
@Component
@ConfigurationProperties(exceptionIfInvalid=true, prefix="domain.model")
public class DomainConfigBuilder extends AbstractModelConfigBuilder {

	@Autowired @Getter @Setter ExecutionInputConfigHandler execInputHandler;
	@Autowired @Getter @Setter ExecutionOutputConfigHandler execOutputHandler;
	
	@Getter @Setter private Map<String, String> typeClassMappings = new HashMap<>();

	@PostConstruct
	public void loadMappings() {
		typeClassMappings.put(LocalDate.class.getName(), "date");
	}
	
	/**
	 * Load available configuration for the domain entities
	 * */
	public <T> ModelConfig<T> load(Class<T> clazz, DomainConfig dc, ModelConfigVistor visitedModels) {
		logit.trace(()->"Loading config from class: "+clazz);
		
		/* 1. Skip if class was already visited, otherwise handle model */
		ModelConfig<T> mConfig = (visitedModels.contains(clazz)) ? (ModelConfig<T>)visitedModels.get(clazz) : buildModel(clazz, visitedModels);
		
		/* 2. Add {Action} execution input config */
		execInputHandler.loadClassConfigs(dc, mConfig);
		
		
		/* 3. Add {Action} execution output config */
		execOutputHandler.loadClassConfigs(dc, mConfig);
		
		/* 3. Add {Action} validation $config */
		
		/* mark model visited */
		//dc.markVisited(clazz, mConfig);
		
		return mConfig;
	}
	
	@Override
	public <T> ModelConfig<T> buildModel(Class<T> clazz, ModelConfigVistor visitedModels) {
		ModelConfig<T> mConfig = createModel(clazz, visitedModels);
		
		//look if the model is marked with MapsTo
		if(mConfig.isMapped()) {
			
			//load mapped class config
			if(!visitedModels.contains(mConfig.findIfMapped().getMapsTo().getReferredClass())) {
				buildModel(mConfig.findIfMapped().getMapsTo().getReferredClass(), visitedModels);
			}
		}
		
		List<Field> fields = FieldUtils.getAllFieldsList(clazz);
		if(fields==null) return mConfig;
		
		fields.forEach((f) -> mConfig.templateParams().add(buildParam(mConfig, f, visitedModels)));
		
		visitedModels.set(clazz, mConfig);
		return mConfig;
	}

	@Override
	public <T> ParamConfig<?> buildParam(ModelConfig<T> mConfig, Field f, ModelConfigVistor visitedModels) {
		
		/* handle ignore field */
		if(AnnotatedElementUtils.isAnnotated(f, ConfigNature.Ignore.class)) return null;
		if("serialVersionUID".equals(f.getName())) return null;

		final DefaultParamConfig<?> pConfig = createParam(mConfig, f, visitedModels);
		

		// handle type
		ParamType type = buildParamType(mConfig, pConfig, f, visitedModels);
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
	
	
	@SuppressWarnings("unchecked")
	protected <T, P> ParamType buildParamType(ModelConfig<T> mConfig, ParamConfig<P> pConfig, Field f, ModelConfigVistor visitedModels) {
		
		final ParamType.CollectionType colType = determineCollectionType(f.getType());	

		final Class<P> determinedType = (Class<P>)GenericUtils.resolveGeneric(mConfig.getReferredClass(), f.getDeclaringClass(), f.getType(), f.getGenericType());
		
		if(colType!=null) { //handle collections first
			//create nested collection type
			ParamType.NestedCollection<P> colModelType = createNestedCollectionType(colType);
			
			//create collection config model
			DefaultModelConfig<List<P>> colModelConfig = createCollectionModel(colModelType.getReferredClass(), pConfig);
			colModelType.setModel(colModelConfig);
			 
			//create collection element param config
			DefaultParamConfig<P> colElemParamConfig = createParamCollectionElement(mConfig, f, pConfig, colModelConfig, visitedModels, determinedType);
			colModelType.setElementConfig(colElemParamConfig);

			//create collection element type (and element model config)
			ParamType colElemType = createParamType(determinedType, colModelConfig, visitedModels);
			colElemParamConfig.setType(colElemType);
			
			return colModelType;
			
		} else {
			ParamType type = createParamType(determinedType, mConfig, visitedModels);
			return type;
		}
	}
	
	@Override
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
