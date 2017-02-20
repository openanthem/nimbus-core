/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.internal.MappedDefaultModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;
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
	public static class FlowMeta<C> extends StateMeta<ProcessFlow> {
		final private StateMeta<C> core;
		
		public FlowMeta(StateMeta<C> core, ModelConfig<ProcessFlow> config) {
			super(config);
			
			Objects.requireNonNull(core, ()->"Core StateAndConfigMeta must not be null.");
			this.core = core;
		}
		
		public FlowMeta(StateMeta<C> core, ModelConfig<ProcessFlow> config, Function<ModelConfig<ProcessFlow>, ProcessFlow> stateCreator) {
			super(config, stateCreator);
			
			Objects.requireNonNull(core, ()->"Core StateAndConfigMeta must not be null.");
			this.core = core;
		}
		
		public FlowMeta(StateMeta<C> core, ModelConfig<ProcessFlow> config, Supplier<ProcessFlow> stateGetter, Consumer<ProcessFlow> stateSetter) {
			super(config, stateGetter, stateSetter);
			
			Objects.requireNonNull(core, ()->"Core StateAndConfigMeta must not be null.");
			this.core = core;
		}
		
	}
	
	@Getter @Setter
	public static class View<V, C> extends StateMeta<V> {
		final private FlowMeta<C> flow;
		final private StateMeta<C> core;
		final private MappedDefaultModelConfig<V, C> config; 
		
		public View(FlowMeta<C> flow, MappedDefaultModelConfig<V, C> viewConfig) {
			super(viewConfig);
			this.config = viewConfig;
			
			Objects.requireNonNull(flow, ()->"Flow StateAndConfigMeta must not be null.");
			this.flow = flow;
			this.core = flow.getCore();
		}
		
		public View(FlowMeta<C> flow, MappedDefaultModelConfig<V, C> viewConfig, Function<ModelConfig<V>, V> viewStateCreator) {
			super(viewConfig, viewStateCreator);
			this.config = viewConfig;
			
			Objects.requireNonNull(flow, ()->"Flow StateAndConfigMeta must not be null.");
			this.flow = flow;
			this.core = flow.getCore();
		}
		
		public View(FlowMeta<C> flow, MappedDefaultModelConfig<V, C> viewConfig, Supplier<V> stateGetter, Consumer<V> stateSetter) {
			super(viewConfig, stateGetter, stateSetter);
			this.config = viewConfig;
			
			Objects.requireNonNull(flow, ()->"Flow StateAndConfigMeta must not be null.");
			this.flow = flow;
			this.core = flow.getCore();	
		}
		
		public static <V, C> View<V, C> getInstance(ModelConfig<C> coreConfig, MappedDefaultModelConfig<V, C> viewConfig, ModelConfig<ProcessFlow> flowConfig,
				Function<ModelConfig<C>, C> coreStateCreator, Function<ModelConfig<V>, V> viewStateCreator, Function<ModelConfig<ProcessFlow>, ProcessFlow> flowStateCreator) {
			
			StateMeta<C> core = new StateMeta<>(coreConfig, coreStateCreator);
			
			FlowMeta<C> flow = (flowStateCreator==null) ? new FlowMeta<>(core, flowConfig) : new FlowMeta<>(core, flowConfig, flowStateCreator);
				
			View<V, C> view = (viewStateCreator==null) ? new View<>(flow, viewConfig) : new View<>(flow, viewConfig, viewStateCreator);
			return view;
		}
		
		public ExecutionEntity.ExConfig<V, C> getExecutionConfig() {
			return new ExecutionEntity.ExConfig<>(getCore().getConfig(), getConfig(), getFlow().getConfig());
		}
	}
	

}
