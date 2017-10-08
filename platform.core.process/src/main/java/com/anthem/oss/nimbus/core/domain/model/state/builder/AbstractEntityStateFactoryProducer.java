/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListElemParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultListElemParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultListModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultListParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultParamState;

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
			return new DefaultListElemParamState<>(parentModel, mpConfig, provider, elemId);
		}
		
		@Override
		public <P> DefaultParamState<P> instantiateParam(Param<?> mapsToParam, Model<?> parentModel, ParamConfig<P> paramConfig, EntityStateAspectHandlers aspectHandlers) {
			if(paramConfig.getType().isCollection())
				return new DefaultListParamState(parentModel, paramConfig, aspectHandlers);
			
			return new DefaultParamState<>(parentModel, paramConfig, aspectHandlers);
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
			return new MappedDefaultListElemParamState<>(parentModel, mpConfig, provider, elemId);
		}
		
		@Override
		public <P> DefaultParamState<P> instantiateParam(Param<?> mapsToParam, Model<?> parentModel, ParamConfig<P> paramConfig, EntityStateAspectHandlers aspectHandlers) {
			if(paramConfig.getType().isCollection()) {
				return new MappedDefaultListParamState(mapsToParam.findIfCollection(), parentModel, paramConfig, aspectHandlers);
			}
				
			return new MappedDefaultParamState<>(mapsToParam, parentModel, paramConfig, aspectHandlers);
		}

	}
}
