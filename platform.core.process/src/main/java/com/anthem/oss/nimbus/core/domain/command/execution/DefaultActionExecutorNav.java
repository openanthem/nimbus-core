/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiDAO;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.builder.PageNavigationInitializer;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.entity.process.PageNavigation;
import com.anthem.oss.nimbus.core.entity.process.PageNode;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default._nav$execute")
public class DefaultActionExecutorNav extends AbstractProcessTaskExecutor {
	
	@Autowired
	ActivitiDAO platformProcessDAO;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired 
	PageNavigationInitializer navigationStateHelper;
	
	public static final String BREADCRUMB_SEPARATOR_P = "breadCrumb$"; // to separate the breadCrumb path from normal payload

	@SuppressWarnings("unchecked")
	@Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		QuadModel<?, ?> q = findQuad(cmdMsg);
		String navigationDirection = cmdMsg.getRawPayload();
		return (R)findNavigationPage(q, navigationDirection);
	}
	
	protected String findNavigationPage(QuadModel<?, ?> quadModel, String navigationDirection) {
		Param<PageNavigation> param = quadModel.getFlow().findParamByPath("/pageNavigation");
		PageNavigation nav = param.getState();
		
		String currPageId = nav.getCurrentPageId();
		PageNode nextPage = null;	
		if(navigationDirection != null && navigationDirection.equalsIgnoreCase("back")){
			nextPage = nav.getPreviousPage(currPageId);
		}else if(navigationDirection != null && navigationDirection.startsWith(BREADCRUMB_SEPARATOR_P)){ // Ex: payload = breadCrumb$/b1/b11
			String breadCrumbPath = StringUtils.substringAfter(navigationDirection, BREADCRUMB_SEPARATOR_P);
			nextPage = findNavigationPageForBreadCrumb(quadModel,breadCrumbPath);
		}else {
			nextPage = nav.getNextPage(currPageId);
		}
		nav.setCurrentPageId(nextPage.getId());
		String executionId = (String)quadModel.getFlow().findStateByPath("/processExecutionId").getState();
		List<Task> openedTasks = taskService.createTaskQuery().processInstanceId(executionId).list();
		if(openedTasks != null && openedTasks.size() >0){
			for(Task openedTask: openedTasks){
				if(!nextPage.getPageId().equals(openedTask.getTaskDefinitionKey())){
					platformProcessDAO.updatePageTaskForExecution(openedTask.getId(),openedTask.getExecutionId(),openedTask.getTaskDefinitionKey(),
							nextPage.getPageId(), nextPage.getPageName());
				}
			}
		}

		return nextPage.getPageId();
	}
	
	/**
	 * 
	 * @param quadModel
	 * @param breadCrumbPath
	 * @return
	 */
	private PageNode findNavigationPageForBreadCrumb(QuadModel<?, ?> quadModel, String breadCrumbPath) {
		Param<PageNavigation> param = quadModel.getFlow().findParamByPath("/pageNavigation");
		PageNavigation nav = param.getState();
		
		for(PageNode pageNode : nav.getPageNodes()){			
			ParamConfig<?> currentPage = quadModel.getView().getConfig().findParamByPath(Constants.SEPARATOR_URI.code + pageNode.getPageId());
			String associatedBreadCrumb = navigationStateHelper.getAssociatedBreadCrumbForPage(currentPage);
			if(associatedBreadCrumb != null && associatedBreadCrumb.startsWith(breadCrumbPath)){
				return pageNode;
			}	
		}
		return null;
	}
	
	
}