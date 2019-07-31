/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.model.state.builder.internal;

import java.util.List;

import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultListElemParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultListElemParamState.LeafElemState;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultListModelState;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultListParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultModelState;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultListElemParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultListElemParamState.MappedLeafElemState;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultListModelState;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultListParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultModelState;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultParamState.MappedLeafState;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public abstract class AbstractEntityStateFactoryProducer {

	private UnMappedEntityStateFactory unmapped = new UnMappedEntityStateFactory();
	private MappedEntityStateFactory mapped = new MappedEntityStateFactory();
	
	public static abstract class AbstractEntityStateFactory {
		public abstract <T> DefaultModelState<T> instantiateModel(Param<T> associatedParam, ModelConfig<T> config, EntityStateAspectHandlers provider, Model<?> mapsToSAC);
	
		public abstract <T> DefaultListModelState<T> instantiateColModel(ListParam associatedParam, ModelConfig<List<T>> config, EntityStateAspectHandlers provider, DefaultListElemParamState.Creator<T> elemCreator);
		
		public abstract <E> DefaultListElemParamState<E> instantiateColElemParam(EntityStateAspectHandlers provider, DefaultListModelState<E> parentModel, ParamConfig<E> mpConfig, String elemId);
		
		public abstract <P> DefaultParamState<P> instantiateParam(Param<?> mapsToParam, Model<?> parentModel, ParamConfig<P> paramConfig, EntityStateAspectHandlers aspectHandlers);
	}
	
	public AbstractEntityStateFactory getFactory(Param<?> param) {
		return param.isMapped() ? mapped : unmapped;
	}
	
	public AbstractEntityStateFactory getFactory(Model<?> model) {
		return model.isMapped() ? mapped : unmapped;
	}
	
	public AbstractEntityStateFactory getFactory(ParamConfig<?> paramConfig) {
		return paramConfig.isMapped() ? mapped : unmapped;
	}
	
	/**
	 * UnMapped Factory for Entity State 
	 */
	public static class UnMappedEntityStateFactory extends AbstractEntityStateFactory {
	
		@Override
		public <T> DefaultModelState<T> instantiateModel(Param<T> associatedParam, ModelConfig<T> config, EntityStateAspectHandlers provider, Model<?> mapsToSAC) {
			return new DefaultModelState<>(associatedParam, config, provider);
		}
		
		@Override
		public <T> DefaultListModelState<T> instantiateColModel(ListParam associatedParam, ModelConfig<List<T>> config, EntityStateAspectHandlers provider, DefaultListElemParamState.Creator<T> elemCreator) {
			return new DefaultListModelState<>(associatedParam, config, provider, elemCreator);					
		}
		
		@Override
		public <E> DefaultListElemParamState<E> instantiateColElemParam(EntityStateAspectHandlers provider, DefaultListModelState<E> parentModel, ParamConfig<E> mpConfig, String elemId) {
			return mpConfig.isLeaf()
					? new LeafElemState<>(parentModel, mpConfig, provider, elemId)
							: new DefaultListElemParamState<>(parentModel, mpConfig, provider, elemId);
		}
		
		@Override
		public <P> DefaultParamState<P> instantiateParam(Param<?> mapsToParam, Model<?> parentModel, ParamConfig<P> paramConfig, EntityStateAspectHandlers aspectHandlers) {
			if(paramConfig.getType().isCollection())
				return paramConfig.getType().findIfCollection().isLeafElements() 
							? new DefaultListParamState.LeafState(parentModel, paramConfig, aspectHandlers) 
									:  new DefaultListParamState(parentModel, paramConfig, aspectHandlers);
			
			return paramConfig.isLeaf() 
					? new DefaultParamState.LeafState<>(parentModel, paramConfig, aspectHandlers) 
							: new DefaultParamState<>(parentModel, paramConfig, aspectHandlers);
		}
	}

	/**
	 * Mapped Factory for Entity State 
	 */
	public static class MappedEntityStateFactory extends AbstractEntityStateFactory {
		
		@Override
		public <T> DefaultModelState<T> instantiateModel(Param<T> associatedParam, ModelConfig<T> config, EntityStateAspectHandlers provider, Model<?> mapsToSAC) {
			return new MappedDefaultModelState<>(mapsToSAC, associatedParam, config, provider);
		}
		
		@Override
		public <T> DefaultListModelState<T> instantiateColModel(ListParam associatedParam, ModelConfig<List<T>> config, EntityStateAspectHandlers provider, DefaultListElemParamState.Creator<T> elemCreator) {
			return new MappedDefaultListModelState<>(associatedParam.findIfMapped().getMapsTo().getType().findIfCollection().getModel(), associatedParam, config, provider, elemCreator);					
		}
		
		@Override
		public <E> DefaultListElemParamState<E> instantiateColElemParam(EntityStateAspectHandlers provider, DefaultListModelState<E> parentModel, ParamConfig<E> mpConfig, String elemId) {
			return mpConfig.isLeaf()
					? new MappedLeafElemState<>(parentModel, mpConfig, provider, elemId)
							: new MappedDefaultListElemParamState<>(parentModel, mpConfig, provider, elemId);
		}
		
		@Override
		public <P> DefaultParamState<P> instantiateParam(Param<?> mapsToParam, Model<?> parentModel, ParamConfig<P> paramConfig, EntityStateAspectHandlers aspectHandlers) {
			if(paramConfig.getType().isCollection())
				return paramConfig.getType().findIfCollection().isLeafElements() 
							? new MappedDefaultListParamState.LeafState(mapsToParam.findIfCollection(), parentModel, paramConfig, aspectHandlers)
									: new MappedDefaultListParamState(mapsToParam.findIfCollection(), parentModel, paramConfig, aspectHandlers);
			
				
			return paramConfig.isLeaf() 
					? new MappedLeafState<>(mapsToParam, parentModel, paramConfig, aspectHandlers) 
							: new MappedDefaultParamState<>(mapsToParam, parentModel, paramConfig, aspectHandlers);
		}

	}
}
