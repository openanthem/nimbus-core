package com.anthem.nimbus.platform.utils.converter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultParamState;

/**
 * @Author Cheikh Niass on 10/11/16.
 */
@Component
public class NavigationStateHelper {
	
	public static final String ACTIVATE_ATTRIBUTE = "active";
	public static final String VISIBLE_ATTRIBUTE = "visible";
	public static final String BREADCRUMB_ATTRIBUTE = "breadCrumb";
	
	/**
	 * 
	 * @param quadModel
	 */
	public void init(QuadModel<?, ?> quadModel){
		initializeBreadCrumbs(quadModel);
	}
	
	
	/**
	 * 
	 * @param quadModel
	 */
	public void initializeBreadCrumbs(QuadModel<?, ?> quadModel){
		List<? extends ParamConfig<?>> params = quadModel.getView().getConfig().getParams();
		for(ParamConfig<?> param : params){
			updateBreadCrumbState(quadModel, param, VISIBLE_ATTRIBUTE, Boolean.FALSE);
			updateBreadCrumbState(quadModel, param, ACTIVATE_ATTRIBUTE, Boolean.FALSE);
		}		
	}
	
	/**
	 * 
	 * @param quadModel
	 * @param currentPage
	 */
	public void activateBreadCrumbForPage(QuadModel<?, ?> quadModel, ParamConfig<?> currentPage){
		updateBreadCrumbState(quadModel,currentPage,VISIBLE_ATTRIBUTE,Boolean.TRUE);
	}
	
	
	/**
	 * 
	 * @param quadModel
	 * @param currentPage
	 */
	public void displayBreadCrumbForPage(QuadModel<?, ?> quadModel, ParamConfig<?> currentPage){
		hideAllBreadCrumbs(quadModel);
		updateBreadCrumbState(quadModel,currentPage,ACTIVATE_ATTRIBUTE,Boolean.TRUE);
	}
	
	/**
	 * 
	 * @param currentPage
	 * @return
	 */
	public String getAssociatedBreadCrumbForPage(ParamConfig<?> currentPage){
		if(currentPage.getUiStyles() == null)
			return null;
		String associatedBreadCrumb = (String)currentPage.getUiStyles().getAttributes().get(BREADCRUMB_ATTRIBUTE);
		return (associatedBreadCrumb != null && !associatedBreadCrumb.equals("none")) ? associatedBreadCrumb : null;
	}
	
	/**
	 * 
	 * @param quadModel
	 */
	private void hideAllBreadCrumbs(QuadModel<?, ?> quadModel){
		List<? extends ParamConfig<?>> params = quadModel.getView().getConfig().getParams();
		for(ParamConfig<?> param : params){
			updateBreadCrumbState(quadModel, param, ACTIVATE_ATTRIBUTE, Boolean.FALSE);
		}			
	}

	
	/**
	 * 
	 * @param quadModel
	 * @param currentPage
	 * @param attribute
	 */
    private void updateBreadCrumbState(QuadModel<?, ?> quadModel, ParamConfig<?> currentPage, String attribute, Boolean state) {
    	if(currentPage == null){
    		return;
    	}
        String breadCrumbPath = getAssociatedBreadCrumbForPage(currentPage);
        if(breadCrumbPath == null)
        	return;
        String[] breadCrumbs = breadCrumbPath.split(Constants.SEPARATOR_URI.code);
        DefaultParamState<?> currentSAC = null;
        boolean rootNode = true;
        for(String breadCrumb: breadCrumbs){
        	if(StringUtils.isEmpty(breadCrumb)|| breadCrumbPath.equals("none"))
        		continue;
        	StringBuilder statePath = new StringBuilder();
        	statePath.append(Constants.SEPARATOR_URI.code).append(breadCrumb).append("#").append(attribute);

        	if(rootNode){

        		quadModel.getView().findStateByPath(statePath.toString()).setState(state);
        	}else{
        		currentSAC.findStateByPath(statePath.toString()).setState(state);
        	}
            currentSAC = (DefaultParamState<?>)quadModel.getView().findStateByPath(Constants.SEPARATOR_URI.code+breadCrumb);
        	rootNode = false;
        }
    }
}

