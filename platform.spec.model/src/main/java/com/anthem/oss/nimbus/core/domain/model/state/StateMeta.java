/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.anthem.nimbus.platform.spec.model.dsl.binder.FlowState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.domain.model.config.MappedDefaultModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.util.ClassLoadUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter 
public class StateMeta<T> {

	final private ModelConfig<T> config;
	final private Supplier<T> stateGetter;
	final private Consumer<T> stateSetter;
	
	public StateMeta(ModelConfig<T> config) {
		this(config, (c)->ClassLoadUtils.newInstance(c.getReferredClass()));
	}
	
	public StateMeta(ModelConfig<T> config, Function<ModelConfig<T>, T> stateCreator) {
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
	
	public StateMeta(ModelConfig<T> config, Supplier<T> stateGetter, Consumer<T> stateSetter) {
		Objects.requireNonNull(config, ()->"Config must not be null.");
		Objects.requireNonNull(stateGetter, ()->"State Getter must not be null.");
		Objects.requireNonNull(stateSetter, ()->"State Setter must not be null.");
		
		this.config = config;
		this.stateGetter = stateGetter;
		this.stateSetter = stateSetter;
	}
	
	@Getter @Setter
	public static class Flow<C> extends StateMeta<FlowState> {
		final private StateMeta<C> core;
		
		public Flow(StateMeta<C> core, ModelConfig<FlowState> config) {
			super(config);
			
			Objects.requireNonNull(core, ()->"Core StateAndConfigMeta must not be null.");
			this.core = core;
		}
		
		public Flow(StateMeta<C> core, ModelConfig<FlowState> config, Function<ModelConfig<FlowState>, FlowState> stateCreator) {
			super(config, stateCreator);
			
			Objects.requireNonNull(core, ()->"Core StateAndConfigMeta must not be null.");
			this.core = core;
		}
		
		public Flow(StateMeta<C> core, ModelConfig<FlowState> config, Supplier<FlowState> stateGetter, Consumer<FlowState> stateSetter) {
			super(config, stateGetter, stateSetter);
			
			Objects.requireNonNull(core, ()->"Core StateAndConfigMeta must not be null.");
			this.core = core;
		}
		
	}
	
	@Getter @Setter
	public static class View<V, C> extends StateMeta<V> {
		final private Flow<C> flow;
		final private StateMeta<C> core;
		final private MappedDefaultModelConfig<V, C> config; 
		
		public View(Flow<C> flow, MappedDefaultModelConfig<V, C> viewConfig) {
			super(viewConfig);
			this.config = viewConfig;
			
			Objects.requireNonNull(flow, ()->"Flow StateAndConfigMeta must not be null.");
			this.flow = flow;
			this.core = flow.getCore();
		}
		
		public View(Flow<C> flow, MappedDefaultModelConfig<V, C> viewConfig, Function<ModelConfig<V>, V> viewStateCreator) {
			super(viewConfig, viewStateCreator);
			this.config = viewConfig;
			
			Objects.requireNonNull(flow, ()->"Flow StateAndConfigMeta must not be null.");
			this.flow = flow;
			this.core = flow.getCore();
		}
		
		public View(Flow<C> flow, MappedDefaultModelConfig<V, C> viewConfig, Supplier<V> stateGetter, Consumer<V> stateSetter) {
			super(viewConfig, stateGetter, stateSetter);
			this.config = viewConfig;
			
			Objects.requireNonNull(flow, ()->"Flow StateAndConfigMeta must not be null.");
			this.flow = flow;
			this.core = flow.getCore();	
		}
		
		public static <V, C> View<V, C> getInstance(ModelConfig<C> coreConfig, MappedDefaultModelConfig<V, C> viewConfig, ModelConfig<FlowState> flowConfig,
				Function<ModelConfig<C>, C> coreStateCreator, Function<ModelConfig<V>, V> viewStateCreator, Function<ModelConfig<FlowState>, FlowState> flowStateCreator) {
			
			StateMeta<C> core = new StateMeta<>(coreConfig, coreStateCreator);
			
			Flow<C> flow = (flowStateCreator==null) ? new Flow<>(core, flowConfig) : new Flow<>(core, flowConfig, flowStateCreator);
				
			View<V, C> view = (viewStateCreator==null) ? new View<>(flow, viewConfig) : new View<>(flow, viewConfig, viewStateCreator);
			return view;
		}
		
		public ExecutionState.Config<V, C> getExecutionConfig() {
			return new ExecutionState.Config<>(getCore().getConfig(), getConfig(), getFlow().getConfig());
		}
	}
	

}
