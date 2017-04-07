/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.builder;

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

import com.anthem.oss.nimbus.core.domain.config.DefaultDomainConfig;
import com.anthem.oss.nimbus.core.domain.config.builder.ExecutionInputConfigHandler;
import com.anthem.oss.nimbus.core.domain.config.builder.ExecutionOutputConfigHandler;
import com.anthem.oss.nimbus.core.domain.definition.ConfigNature;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.anthem.oss.nimbus.core.domain.model.config.internal.DefaultModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.internal.DefaultParamConfig;
import com.anthem.oss.nimbus.core.util.GenericUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */ 

@ConfigurationProperties(exceptionIfInvalid=true, prefix="domain.model")
public class EntityConfigBuilder extends AbstractEntityConfigBuilder {

	@Getter @Setter ExecutionInputConfigHandler execInputHandler;
	@Getter @Setter ExecutionOutputConfigHandler execOutputHandler;
	
	@Getter @Setter private Map<String, String> typeClassMappings = new HashMap<>();

	public EntityConfigBuilder(ExecutionInputConfigHandler execInputHandler,
			ExecutionOutputConfigHandler execOutputHandler) {
		this.execInputHandler = execInputHandler;
		this.execOutputHandler = execOutputHandler;
	}
	
	@PostConstruct
	public void loadMappings() {
		typeClassMappings.put(LocalDate.class.getName(), "date");
	}
	
	/**
	 * Load available configuration for the domain entities
	 * */
	public <T> ModelConfig<T> load(Class<T> clazz, DefaultDomainConfig dc, EntityConfigVistor visitedModels) {
		logit.trace(()->"Loading config from class: "+clazz);
		
		/* 1. Skip if class was already visited, otherwise handle model */
		ModelConfig<T> mConfig = buildModel(clazz, visitedModels);
		
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
	public <T> ModelConfig<T> buildModel(Class<T> clazz, EntityConfigVistor visitedModels) {
		// skip if already built
		if(visitedModels.contains(clazz)) 
			return (ModelConfig<T>)visitedModels.get(clazz);
		
		ModelConfig<T> mConfig = createModel(clazz, visitedModels);
		
		//look if the model is marked with MapsTo
		if(mConfig.isMapped()) {
			
			//ensure mapped class config is already loaded
			buildModel(mConfig.findIfMapped().getMapsTo().getReferredClass(), visitedModels);
		}
		
		List<Field> fields = FieldUtils.getAllFieldsList(clazz);
		if(fields==null) return mConfig;
		
		fields.stream()
			.filter((f)-> !f.isSynthetic())
			.forEach((f) -> mConfig.templateParams().add(buildParam(mConfig, f, visitedModels)));
		
		visitedModels.set(clazz, mConfig);
		return mConfig;
	}

	@Override
	public <T> ParamConfig<?> buildParam(ModelConfig<T> mConfig, Field f, EntityConfigVistor visitedModels) {
		
		logit.trace(()->"Building Param for config class: "+mConfig.getReferredClass()+ " field : "+f.getName());
		
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
	@Override
	protected <T, P> ParamType buildParamType(ModelConfig<T> mConfig, ParamConfig<P> pConfig, Field f, EntityConfigVistor visitedModels) {
		final Class<P> determinedType = (Class<P>)GenericUtils.resolveGeneric(mConfig.getReferredClass(), f);
		
		final ParamType.CollectionType colType = determineCollectionType(f.getType());	
		//MapsTo.Path mapsToPath = AnnotationUtils.findAnnotation(f, MapsTo.Path.class);
		return buildParamType(mConfig, pConfig, colType, determinedType, /*mapsToPath, */visitedModels);
	}
	
	@Override
	protected <T, P> ParamType buildParamType(ModelConfig<T> mConfig, ParamConfig<P> pConfig, ParamType.CollectionType colType, Class<?> pDirectOrColElemType, /*MapsTo.Path mapsToPath, */EntityConfigVistor visitedModels) {
		if(ParamType.CollectionType.array==colType && isPrimitive(pDirectOrColElemType)) { // handle primitive array first
			ParamType type = createParamType(true, pDirectOrColElemType, mConfig, visitedModels);
			return type;
			
		} else if(colType!=null) { //handle collections second
			//create nested collection type
			ParamType.NestedCollection<P> colModelType = createNestedCollectionType(colType);
			
			//create collection config model
			DefaultModelConfig<List<P>> colModelConfig = createCollectionModel(colModelType.getReferredClass(), pConfig);
			colModelType.setModel(colModelConfig);
			 
			//create collection element param config
			DefaultParamConfig<P> colElemParamConfig = createParamCollectionElement(mConfig, /*mapsToPath, */pConfig, colModelConfig, visitedModels, pDirectOrColElemType);
			colModelType.setElementConfig(colElemParamConfig);

			//create collection element type (and element model config)
			ParamType colElemType = createParamType(false, pDirectOrColElemType, colModelConfig, visitedModels);
			colElemParamConfig.setType(colElemType);
			
			return colModelType;
			
		} else {
			ParamType type = createParamType(true, pDirectOrColElemType, mConfig, visitedModels);
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
