/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
import com.anthem.nimbus.platform.spec.model.dsl.Domain;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo.Database;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuru
 *
 */
@Domain(value="flowstate")
@Repo(Database.rep_mongodb)
@Getter @Setter
public class FlowState extends AbstractModel.IdString implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	private transient RulesEngineState coreRulesEngineState;
	
	@Transient
	private transient RulesEngineState viewRulesEngineState;
	
	private String processExecutionId;
	
	private NavigationState navigationState;
	

	/**
	 * 
	 * @param processConfiguration
	 * @param model
	 */
	public void init(ProcessConfiguration processConfiguration, QuadModel<?, ?> quadModel){
		if(processConfiguration.getCoreRulesContainer() != null){
			coreRulesEngineState = new RulesEngineState(processConfiguration.getCoreRulesContainer(), quadModel.getCore());
		}
		
		if(processConfiguration.getViewRulesContainer() != null){
			viewRulesEngineState = new RulesEngineState(processConfiguration.getViewRulesContainer(), quadModel.getView());
		} 
		if(quadModel.getFlow().findModelByPath("/navigationState").getState()== null){
			navigationState = new NavigationState();
			PageHolder pageHolder = new PageHolder();
			navigationState.setPageHolder(pageHolder);
			quadModel.getFlow().findModelByPath("/navigationState").setState(navigationState);
			quadModel.getFlow().findParamByPath("/navigationState/pageHolder").setState(pageHolder);
		}
	}
	
	/**
	 * 
	 * @param model
	 */
	public void fireAllRules(QuadModel<?, ?> quadModel){
		if(coreRulesEngineState != null){
			coreRulesEngineState.fireAllRules(quadModel.getCore());
		}
		
		if(viewRulesEngineState != null){
			viewRulesEngineState.fireAllRules(quadModel.getView());
		}
	}
	
	/**
	 * 
	 */
	public void dispose(){
		if(coreRulesEngineState != null){
			coreRulesEngineState.dispose();
		}
		
		if(viewRulesEngineState != null){
			viewRulesEngineState.dispose();
		}
	}
	
}
