package com.anthem.oss.nimbus.core.domain.command.execution;

import org.activiti.engine.impl.rules.RulesAgendaFilter;
import org.activiti.engine.impl.rules.RulesHelper;
import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.bpm.ModelInitializer;
import com.anthem.oss.nimbus.core.bpm.TaskRouter;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultRuleBasedTaskRouter implements TaskRouter, ModelInitializer {

	@Override
	public void route(QuadModel<?, ?> taskModel, String assignmentCriteria) {
		fireAllRules(taskModel, assignmentCriteria);
	}
	
	@Override
	public void initialize(QuadModel<?, ?> taskModel, String initializeCriteria) {
		fireAllRules(taskModel, initializeCriteria);
	}
	
	
	private void fireAllRules(QuadModel<?, ?> taskModel, String assignmentCriteria)  {
		StatefulKnowledgeSession session=null;
		
	    KnowledgeBase knowledgeBase = RulesHelper.findKnowledgeBaseByDeploymentId("1"); //TODO get the deploymentId instead of hard coding ??
		try {
			session = knowledgeBase.newStatefulKnowledgeSession();
			session.insert(taskModel.getCore()); // Assumption: Task would be created against a core model
			
			RulesAgendaFilter filter = new RulesAgendaFilter();
			//filter.addSuffic(AnnotationUtils.findAnnotation(task.getState().getClass(), CoreDomain.class).value()+"_routing");
			
			String[] criteriaRules = assignmentCriteria.split(",");
			for(String ruleName: criteriaRules) {
				filter.addSuffic(ruleName);
			}
			filter.setAccept(true);
			
			session.fireAllRules(filter);
		}
		
		finally{
			if(session != null)
				session.dispose();
		}
	}


	

}
