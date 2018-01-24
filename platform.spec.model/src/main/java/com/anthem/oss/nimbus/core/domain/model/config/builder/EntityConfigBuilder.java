/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anthem.oss.nimbus.core.domain.model.config.builder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.ConfigNature;
import com.antheminc.oss.nimbus.domain.defn.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamType;
import com.antheminc.oss.nimbus.domain.model.config.internal.DefaultModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.internal.DefaultParamConfig;
import com.antheminc.oss.nimbus.support.pojo.GenericUtils;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */ 
@Getter 
@RefreshScope
public class EntityConfigBuilder extends AbstractEntityConfigBuilder {

	private final Map<String, String> typeClassMappings;
	
	public EntityConfigBuilder(BeanResolverStrategy beanResolver, Map<String, String> typeClassMappings) {
		super(beanResolver);
		
		this.typeClassMappings = typeClassMappings;
	}
	

	public <T> ModelConfig<T> load(Class<T> clazz, EntityConfigVisitor visitedModels) {
		ModelConfig<T> mConfig = buildModel(clazz, visitedModels);
		return mConfig;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> ModelConfig<T> buildModel(Class<T> clazz, EntityConfigVisitor visitedModels) {
		logit.trace(()->"building model for class: "+clazz);
		
		// skip if already built
		if(visitedModels.contains(clazz)) 
			return (ModelConfig<T>)visitedModels.get(clazz);
		
		
		DefaultModelConfig<T> mConfig = createModel(clazz, visitedModels);
		visitedModels.set(clazz, mConfig);

		
		//look if the model is marked with MapsTo
		if(mConfig.isMapped()) {
			
			//ensure mapped class config is already loaded
			buildModel(mConfig.findIfMapped().getMapsTo().getReferredClass(), visitedModels);
		}
		
		List<Field> fields = FieldUtils.getAllFieldsList(clazz);
		if(fields==null) return mConfig;
		
		fields.stream()
			.filter((f)-> !f.isSynthetic())
			.forEach((f)->{
				ParamConfig<?> p = buildParam(mConfig, f, visitedModels);
				mConfig.templateParams().add(p);
				
				if(AnnotatedElementUtils.isAnnotated(f, Id.class)) {
					// default id
					mConfig.setIdParam(p);
					
				} else if(AnnotatedElementUtils.isAnnotated(f, Version.class)) {
					// default version
					mConfig.setVersionParam(p);
				}				
			});
		
		if(Repo.Database.isPersistable(mConfig.getRepo()) && mConfig.getIdParam()==null) {
			throw new InvalidConfigException("Persistable Entity: "+mConfig.getReferredClass()+" must be configured with @Id param which has Repo: "+mConfig.getRepo());
		}
		
		return mConfig;
	}

	@Override
	public <T> ParamConfig<?> buildParam(ModelConfig<T> mConfig, Field f, EntityConfigVisitor visitedModels) {
		
		logit.trace(()->"Building Param for config class: "+mConfig.getReferredClass()+ " field : "+f.getName());
		
		/* handle ignore field */
		if(AnnotatedElementUtils.isAnnotated(f, ConfigNature.Ignore.class)) return null;
		if("serialVersionUID".equals(f.getName())) return null;

		final DefaultParamConfig<?> pConfig = createParam(mConfig, f, visitedModels);
		
		// handle type
		ParamType type = buildParamType(mConfig, pConfig, f, visitedModels);
		pConfig.setType(type);
		
		return pConfig;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T, P> ParamType buildParamType(ModelConfig<T> mConfig, ParamConfig<P> pConfig, Field f, EntityConfigVisitor visitedModels) {
		Class<P> determinedType = (Class<P>)GenericUtils.resolveGeneric(mConfig.getReferredClass(), f);
		
		ParamType.CollectionType colType = determineCollectionType(f.getType());	
		
		return buildParamType(mConfig, pConfig, colType, determinedType, visitedModels);
	}
	
	@Override
	protected <T, P> ParamType buildParamType(ModelConfig<T> mConfig, ParamConfig<P> pConfig, ParamType.CollectionType colType, Class<?> pDirectOrColElemType, /*MapsTo.Path mapsToPath, */EntityConfigVisitor visitedModels) {
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
			DefaultParamConfig<P> colElemParamConfig = (DefaultParamConfig<P>)createParamCollectionElement(mConfig, /*mapsToPath, */pConfig, colModelConfig, visitedModels, pDirectOrColElemType);
			colModelType.setElementConfig(colElemParamConfig);

			//create collection element type (and element model config)
			ParamType colElemType = createParamType(false, pDirectOrColElemType, colModelConfig, visitedModels);
			colElemParamConfig.setType(colElemType);
			
			return colModelType;
			
		} else {
			ParamType type = createParamType(false, pDirectOrColElemType, mConfig, visitedModels);
			return type;
		}
	}
	
	@Override
	public String lookUpTypeClassMapping(Class<?> clazz) {
		if(getTypeClassMappings()==null) return null;
		
		String mapping = getTypeClassMappings().get(clazz.getName());
		return mapping;
	}
	

}
