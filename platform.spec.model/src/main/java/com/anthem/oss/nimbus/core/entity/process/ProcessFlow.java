/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.process;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;

import com.anthem.nimbus.platform.spec.model.dsl.binder.ProcessConfiguration;
import com.anthem.nimbus.platform.spec.model.dsl.binder.RulesEngineRuntime;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value="flow")
@Repo(Database.rep_mongodb)
@Getter @Setter
public class ProcessFlow extends AbstractEntity.IdString implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	private transient RulesEngineRuntime coreRulesEngineState;
	
	@Transient
	private transient RulesEngineRuntime viewRulesEngineState;
	
	private String processExecutionId;
	
	private PageNavigation navigationState;
	

	/**
	 * 
	 * @param processConfiguration
	 * @param model
	 */
	public void init(ProcessConfiguration processConfiguration, QuadModel<?, ?> quadModel){
		if(processConfiguration.getCoreRulesContainer() != null){
			coreRulesEngineState = new RulesEngineRuntime(processConfiguration.getCoreRulesContainer(), quadModel.getCore());
		}
		
		if(processConfiguration.getViewRulesContainer() != null){
			viewRulesEngineState = new RulesEngineRuntime(processConfiguration.getViewRulesContainer(), quadModel.getView());
		} 
		if(quadModel.getFlow().findModelByPath("/navigationState").getState()== null){
			navigationState = new PageNavigation();
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
