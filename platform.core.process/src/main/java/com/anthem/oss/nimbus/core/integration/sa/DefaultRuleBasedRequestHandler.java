/**
 * 
 */
package com.anthem.oss.nimbus.core.integration.sa;

import org.activiti.engine.impl.rules.RulesAgendaFilter;
import org.activiti.engine.impl.rules.RulesHelper;
import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiContext.ServiceActivatorContext;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultRuleBasedRequestHandler implements ServiceActivatorHandler<Object> {

	
	@Override
	public Object handle(ServiceActivatorContext srvcActCtx) throws ServiceActivatorException {
		System.out.println("DefaultRuleBasedRequestHandler called.");
		return fireAllRules(srvcActCtx);
	}
	
	
	private Object fireAllRules(ServiceActivatorContext srvcCtx)  {
		StatefulKnowledgeSession session=null;
		
	    KnowledgeBase knowledgeBase = RulesHelper.findKnowledgeBaseByDeploymentId("1"); //TODO get the deploymentId instead of hard coding ??
		try {
			session = knowledgeBase.newStatefulKnowledgeSession();
			
			session.insert(srvcCtx);
			session.insert(srvcCtx.getProcessEngineContext().getCommandMsg().getRawPayload());
			
			RulesAgendaFilter filter = new RulesAgendaFilter();
			filter.addSuffic(srvcCtx.getDefinition().getRequestHandler());
			filter.setAccept(true);
			
			//session.fireAllRules(filter);
			
//			WorkingMemoryEntryPoint memory = session.getWorkingMemoryEntryPoint(session.getEntryPointId()); /* retrieve the objects added in drl file using insert() */
//			
//			if(memory != null){
//				return new ArrayList<>(memory.getObjects());
//			}
			return null;
		}
		catch(Exception e){
			throw new ServiceActivatorException(e);
		}
		finally{
			if(session != null)
				session.dispose();
		}
	}

}
