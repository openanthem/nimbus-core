/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.anthem.nimbus.platform.spec.model.dsl.config.MappedDomainModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.util.ModelsTemplate;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter 
public class StateAndConfigMeta<T> {

	final private ModelConfig<T> config;
	final private Supplier<T> stateGetter;
	final private Consumer<T> stateSetter;
	
	public StateAndConfigMeta(ModelConfig<T> config) {
		this(config, (c)->ModelsTemplate.newInstance(c.getReferredClass()));
	}
	
	public StateAndConfigMeta(ModelConfig<T> config, Function<ModelConfig<T>, T> stateCreator) {
		Objects.requireNonNull(config, ()->"Config must not be null.");
		this.config = config;
		
		Holder<T> h = new Holder<>();
		
		this.stateGetter = () -> {
			if(h.getState() != null) {
				return h.getState();
			}
			
			T coreState = stateCreator.apply(config);
			h.setState(coreState);
			return coreState;
		};
		
		this.stateSetter = s->h.setState(s);
	}
	
	public StateAndConfigMeta(ModelConfig<T> config, Supplier<T> stateGetter, Consumer<T> stateSetter) {
		Objects.requireNonNull(config, ()->"Config must not be null.");
		Objects.requireNonNull(stateGetter, ()->"State Getter must not be null.");
		Objects.requireNonNull(stateSetter, ()->"State Setter must not be null.");
		
		this.config = config;
		this.stateGetter = stateGetter;
		this.stateSetter = stateSetter;
	}
	
	@Getter @Setter
	public static class Flow<C> extends StateAndConfigMeta<FlowState> {
		final private StateAndConfigMeta<C> core;
		
		public Flow(StateAndConfigMeta<C> core, ModelConfig<FlowState> config) {
			super(config);
			
			Objects.requireNonNull(core, ()->"Core StateAndConfigMeta must not be null.");
			this.core = core;
		}
		
		public Flow(StateAndConfigMeta<C> core, ModelConfig<FlowState> config, Function<ModelConfig<FlowState>, FlowState> stateCreator) {
			super(config, stateCreator);
			
			Objects.requireNonNull(core, ()->"Core StateAndConfigMeta must not be null.");
			this.core = core;
		}
		
		public Flow(StateAndConfigMeta<C> core, ModelConfig<FlowState> config, Supplier<FlowState> stateGetter, Consumer<FlowState> stateSetter) {
			super(config, stateGetter, stateSetter);
			
			Objects.requireNonNull(core, ()->"Core StateAndConfigMeta must not be null.");
			this.core = core;
		}
		
	}
	
	@Getter @Setter
	public static class View<V, C> extends StateAndConfigMeta<V> {
		final private Flow<C> flow;
		final private StateAndConfigMeta<C> core;
		final private MappedDomainModelConfig<V, C> config; 
		
		public View(Flow<C> flow, MappedDomainModelConfig<V, C> viewConfig) {
			super(viewConfig);
			this.config = viewConfig;
			
			Objects.requireNonNull(flow, ()->"Flow StateAndConfigMeta must not be null.");
			this.flow = flow;
			this.core = flow.getCore();
		}
		
		public View(Flow<C> flow, MappedDomainModelConfig<V, C> viewConfig, Function<ModelConfig<V>, V> viewStateCreator) {
			super(viewConfig, viewStateCreator);
			this.config = viewConfig;
			
			Objects.requireNonNull(flow, ()->"Flow StateAndConfigMeta must not be null.");
			this.flow = flow;
			this.core = flow.getCore();
		}
		
		public View(Flow<C> flow, MappedDomainModelConfig<V, C> viewConfig, Supplier<V> stateGetter, Consumer<V> stateSetter) {
			super(viewConfig, stateGetter, stateSetter);
			this.config = viewConfig;
			
			Objects.requireNonNull(flow, ()->"Flow StateAndConfigMeta must not be null.");
			this.flow = flow;
			this.core = flow.getCore();	
		}
		
		public static <V, C> View<V, C> getInstance(ModelConfig<C> coreConfig, MappedDomainModelConfig<V, C> viewConfig, ModelConfig<FlowState> flowConfig,
				Function<ModelConfig<C>, C> coreStateCreator, Function<ModelConfig<V>, V> viewStateCreator, Function<ModelConfig<FlowState>, FlowState> flowStateCreator) {
			
			StateAndConfigMeta<C> core = new StateAndConfigMeta<>(coreConfig, coreStateCreator);
			
			Flow<C> flow = (flowStateCreator==null) ? new Flow<>(core, flowConfig) : new Flow<>(core, flowConfig, flowStateCreator);
				
			View<V, C> view = (viewStateCreator==null) ? new View<>(flow, viewConfig) : new View<>(flow, viewConfig, viewStateCreator);
			return view;
		}
		
		public ExecutionState.Config<V, C> getExecutionConfig() {
			return new ExecutionState.Config<>(getCore().getConfig(), getConfig(), getFlow().getConfig());
		}
	}
	

}
