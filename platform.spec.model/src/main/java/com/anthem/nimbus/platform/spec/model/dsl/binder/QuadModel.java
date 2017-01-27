/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;

import org.drools.base.RuleNameEqualsAgendaFilter;
import org.drools.runtime.rule.AgendaFilter;

import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.command.CommandElement;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.view.dsl.config.ViewModelConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @RequiredArgsConstructor
public class QuadModel<V, C> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	

	private final CommandElement key;
	
	private final StateAndConfig.Model<C, ModelConfig<C>> core;
	
	private final StateAndConfig.Model<V, ViewModelConfig<V, C>> view;
	
	private final StateAndConfig.Model<FlowState, ModelConfig<FlowState>> flow;
	
	@JsonIgnore private transient QuadScopedEventPublisher eventPublisher;
	
	
	public StateAndConfig.Model<?, ?> resolveStateAndConfig(Command cmd) {
		return cmd.isView() ? view : core;
	}
	
	public void loadProcessState(ProcessConfiguration processConfiguration){
		flow.getState().init(processConfiguration, this);
	}
	
	
	/**
	 * 
	 */
	public void fireAllRules(){
		fireAllRules(null);
	}
	
	/**
	 * 
	 * @param agendaFilter
	 */
	public void fireAllRules(AgendaFilter agendaFilter){
		flow.getState().fireAllRules(this,agendaFilter);
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		if(flow.getState() != null){
			flow.getState().dispose();
		}
		super.finalize();
	}
		
}
