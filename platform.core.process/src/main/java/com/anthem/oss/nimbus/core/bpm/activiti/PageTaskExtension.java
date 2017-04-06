package com.anthem.oss.nimbus.core.bpm.activiti;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.entity.process.PageNode;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.core.utils.ProcessBeanResolver;


public class PageTaskExtension extends AbstractUserTaskExtension {

	@Override
	public void execute(UserTask userTask, DelegateExecution execution) {
	    ActivitiContext aCtx = (ActivitiContext) execution.getVariable(ActivitiGateway.PROCESS_ENGINE_GTWY_KEY);
	    QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(aCtx.getProcessEngineContext().getCommandMsg().getCommand());
	    ActivitiExpressionManager platformExpressionManager = (ActivitiExpressionManager) ProcessBeanResolver.appContext.getBean(ActivitiExpressionManager.class);
	    String userTaskIntizializer = getExtensionValue(userTask,"pageInitialization");
	    if(StringUtils.isNotBlank(userTaskIntizializer)) {	
	    	Expression pageInitExpression = platformExpressionManager.createExpression(userTaskIntizializer);
	    	pageInitExpression.getValue(execution);
	    }
	    
	    @SuppressWarnings("unchecked")
	    ListParam<PageNode> param = (ListParam<PageNode>)quadModel.getFlow().findParamByPath("/pageNavigation/pageNodes").findIfCollection();
	    PageNode pageNode = new PageNode();
	    pageNode.setId(userTask.getId());
	    pageNode.setPageId(userTask.getId());
	    pageNode.setPageName(userTask.getName());
	    param.add(pageNode);		
	}

}
