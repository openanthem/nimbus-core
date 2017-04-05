/**
 * 
 */
package com.anthem.oss.nimbus.core.integration.sa;

import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiContext.ServiceActivatorContext;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultRuleBasedResponseHandler implements ServiceActivatorHandler<Object> {

	
	@Override
	public Object handle(ServiceActivatorContext srvcActCtx) throws ServiceActivatorException {
		System.out.println("DefaultRuleBasedResponseHandler called.");
		return fireAllRules(srvcActCtx);
	}
	
	
	private Object fireAllRules(final ServiceActivatorContext srvcCtx)  {
//		StatefulKnowledgeSession session=null;
//		
//	    KnowledgeBase knowledgeBase = RulesHelper.findKnowledgeBaseByDeploymentId("1"); //TODO get the deploymentId instead of hard coding ??
//		try {
//			session = knowledgeBase.newStatefulKnowledgeSession();
//			
//			session.insert(srvcCtx);
//			session.insert(srvcCtx.getProcessEngineContext().getCommandMsg().getModel());
//			
//			RulesAgendaFilter filter = new RulesAgendaFilter();
//			filter.addSuffic(srvcCtx.getDefinition().getResponseHandler());
//			
//			filter.setAccept(true);
//			
//			session.fireAllRules(filter);
//			
//			WorkingMemoryEntryPoint memory = session.getWorkingMemoryEntryPoint(session.getEntryPointId()); /* retrieve the objects added in drl file using insert() */
//			if(memory != null){
//				Collection<Object> objs = new ArrayList<Object>(memory.getObjects());
//						
//				Object obj =	objs.stream()
//						.filter((arg0) -> {
//							//if(srvcCtx.getProcessEngineContext().getCommandMsg().getCommand().getDerivedOutputType().isInstance(arg0)){
//							//TODO check the derived output type and if not found, check for page ??
//							if(arg0 instanceof Page) {
//								return true;
//							}
//							return false;
//						})
//						.findFirst()
//						.get();
//				
//				return obj;
//			}
			return null;
//		}
//		catch(Exception e){
//			throw new ServiceActivatorException(e);
//		}
//		finally{
//			if(session != null)
//				session.dispose();
//		}
	}

}
