/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.exec;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.PlatformProcessDAO;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;
import com.anthem.nimbus.platform.spec.model.dsl.Constants;
import com.anthem.nimbus.platform.spec.model.dsl.binder.PageHolder;
import com.anthem.nimbus.platform.spec.model.dsl.binder.PageNode;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;
import com.anthem.nimbus.platform.spec.model.view.dsl.config.ViewParamConfig;
import com.anthem.nimbus.platform.utils.converter.NavigationStateHelper;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default._nav$execute")
public class DefaultActionExecutorNav extends AbstractProcessTaskExecutor {
	
	@Autowired
	PlatformProcessDAO platformProcessDAO;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired 
	NavigationStateHelper navigationStateHelper;
	
	public static final String BREADCRUMB_SEPARATOR_P = "breadCrumb$"; // to separate the breadCrumb path from normal payload

	@Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		QuadModel<?, ?> q = findQuad(cmdMsg);
		String navigationDirection = cmdMsg.getRawPayload();
		return (R)findNavigationPage(q, navigationDirection);
	}
	
	protected String findNavigationPage(QuadModel<?, ?> quadModel, String navigationDirection) {
		PageHolder pageHolder = (PageHolder)quadModel.getFlow().findStateByPath("/navigationState/pageHolder").getState();
		String currPage = pageHolder.getCurrentPageId();
		PageNode nextPage = null;	
		if(navigationDirection != null && navigationDirection.equalsIgnoreCase("back")){
			nextPage = pageHolder.getPreviousPage(currPage);
		}else if(navigationDirection != null && navigationDirection.startsWith(BREADCRUMB_SEPARATOR_P)){ // Ex: payload = breadCrumb$/b1/b11
			String breadCrumbPath = StringUtils.substringAfter(navigationDirection, BREADCRUMB_SEPARATOR_P);
			nextPage = findNavigationPageForBreadCrumb(quadModel,breadCrumbPath);
		}else {
			nextPage = pageHolder.getNextPage(currPage);
		}
		pageHolder.setCurrentPageId(nextPage.getId());
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
		ViewParamConfig<?> currentPage = (ViewParamConfig<?>) quadModel.getView().getConfig().findParamByPath(Constants.SEPARATOR_URI.code + nextPage.getPageId());
		navigationStateHelper.displayBreadCrumbForPage(quadModel, currentPage);
		quadModel.getFlow().findStateByPath("/navigationState/pageHolder").setState(pageHolder);
		return nextPage.getPageId();
	}
	
	/**
	 * 
	 * @param quadModel
	 * @param breadCrumbPath
	 * @return
	 */
	private PageNode findNavigationPageForBreadCrumb(QuadModel<?, ?> quadModel, String breadCrumbPath) {
		PageHolder pageHolder = (PageHolder)quadModel.getFlow().findStateByPath("/navigationState/pageHolder").getState();
		for(PageNode pageNode : pageHolder.getPageReferences()){			
			ViewParamConfig<?> currentPage = (ViewParamConfig<?>) quadModel.getView().getConfig().findParamByPath(Constants.SEPARATOR_URI.code + pageNode.getPageId());
			String associatedBreadCrumb = navigationStateHelper.getAssociatedBreadCrumbForPage(currentPage);
			if(associatedBreadCrumb != null && associatedBreadCrumb.startsWith(breadCrumbPath)){
				return pageNode;
			}	
		}
		return null;
	}
	
	
}