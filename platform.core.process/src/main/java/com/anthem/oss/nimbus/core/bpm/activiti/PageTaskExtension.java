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

/**
 * Page Task is a type of human task. The framework requires a business process to be specific 
 * through a bpmn construct. If the business process requires the application to navigate users 
 * through a series of pages, then a Page Task represents a page.
 * It is a human task which is waiting for users to provide information through a page so that the 
 * business process can progress. The ID of the human task represnts the id of the page configured
 * in the system.
 * 
 * @author Jayant Chaudhuri
 *
 */
public class PageTaskExtension extends AbstractUserTaskExtension {
	
	public static final String PAGE_NODE_PATH = "/pageNavigation/pageNodes";
	public static final String PAGE_INIT_KEY = "pageInitialization";

	/**
	 * The responsibility of this method is to set the ID of the user task i.e. Page ID within the Navigation
	 * state of the model
	 */
	@Override
	public void execute(UserTask userTask, DelegateExecution execution) {
	    ActivitiContext aCtx = (ActivitiContext) execution.getVariable(ActivitiGateway.PROCESS_ENGINE_GTWY_KEY);
	    QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(aCtx.getProcessEngineContext().getCommandMsg().getCommand());
	    ActivitiExpressionManager platformExpressionManager = (ActivitiExpressionManager) ProcessBeanResolver.appContext.getBean(ActivitiExpressionManager.class);
	    String userTaskIntizializer = getExtensionValue(userTask,PAGE_INIT_KEY);
	    if(StringUtils.isNotBlank(userTaskIntizializer)) {	
	    	Expression pageInitExpression = platformExpressionManager.createExpression(userTaskIntizializer);
	    	pageInitExpression.getValue(execution);
	    }
	    
	    @SuppressWarnings("unchecked")
	    ListParam<PageNode> param = (ListParam<PageNode>)quadModel.getFlow().findParamByPath(PAGE_NODE_PATH).findIfCollection();
	    PageNode pageNode = new PageNode();
	    pageNode.setId(userTask.getId());
	    pageNode.setPageId(userTask.getId());
	    pageNode.setPageName(userTask.getName());
	    param.add(pageNode);		
	}

}
