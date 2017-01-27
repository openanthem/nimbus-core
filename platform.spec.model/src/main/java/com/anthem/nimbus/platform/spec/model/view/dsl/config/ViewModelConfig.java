/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.view.dsl.config;

import java.io.Serializable;

import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ParamStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.SimpleState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.State;
import com.anthem.nimbus.platform.spec.model.dsl.config.CoreModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"label", "message", "core"})
public class ViewModelConfig<V, C> extends CoreModelConfig<V> implements ModelConfig<V>, Serializable {

	private static final long serialVersionUID = 1L;
	

	@JsonIgnore final private transient CoreModelConfig<C> core;
	
	
	private String label;
	
	private Message message;
	
	private Boolean active = Boolean.TRUE;
	
	private Boolean visible = Boolean.TRUE;	
	
	
	@JsonIgnore
	private transient State<String> labelState;// = new SimpleState<String>(()->getLabel(), (v)->setLabel(v));
	
	@JsonIgnore
	private transient State<Message> messageState;// = new SimpleState<String>(()->getMessage(), (v)->setMessage(v));

	@JsonIgnore
	private transient State<Boolean> activeState;
	
	@JsonIgnore
	private transient State<Boolean> visibleState;	
	

	
	@Getter @Setter
	public static class Flow<V, C> extends ViewModelConfig<V, C> {
		
		private static final long serialVersionUID = 1L;
		
		private String currentPage;
		
		@JsonIgnore
		private transient State<String> currentPageState;// = new SimpleState<String>(()->getCurrentPage(), (v)->setCurrentPage(v));
		
		
		public Flow(CoreModelConfig<C> core, Class<V> referredClass, MapsTo.Model mapsTo) {
			super(core, referredClass, mapsTo);
		}
		
		
		@Override
		public void visit(ParamStateAndConfig<?> param, StateAndConfigSupportProvider provider) {
			super.visit(param, provider);
			
			this.currentPageState = new SimpleState<>(param, ()->getCurrentPage(), (v)->currentPage=v, provider);
		}
	}
	
	
	public ViewModelConfig(CoreModelConfig<C> core, Class<V> referredClass, MapsTo.Model mapsTo) {
		super(referredClass, mapsTo);
		this.core = core;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ModelConfig<?> resolveBackingConfig() {
		return (core!=null) ? core : this;
	}
	
	/**
	 * 
	 */
	@Override
	public void visit(ParamStateAndConfig<?> param, StateAndConfigSupportProvider provider) {
		if(core!=null) {
			core.visit(param, provider);
		}
		
		super.visit(param, provider);
		this.labelState = new SimpleState<>(param, ()->getLabel(), (v)->label=v, provider);
		this.messageState = new SimpleState<>(param, ()->getMessage(), (v)->message=v, provider);
		this.activeState = new SimpleState<>(param, ()->getActive(), (v)->active=v, provider);
		this.visibleState = new SimpleState<>(param, ()->getVisible(), (v)->visible=v, provider);
	}
	

	@Override
	public boolean isView() {
		return true;
	}
	
	
}
