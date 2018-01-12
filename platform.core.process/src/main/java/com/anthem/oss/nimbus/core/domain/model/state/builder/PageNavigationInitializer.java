/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultParamState;

/**
 * @Author Cheikh Niass on 10/11/16.
 */
public class PageNavigationInitializer {
	
	public static final String ACTIVATE_ATTRIBUTE = "active";
	public static final String VISIBLE_ATTRIBUTE = "visible";
	public static final String BREADCRUMB_ATTRIBUTE = "breadCrumb";
	
	public void init(QuadModel<?, ?> quadModel){
		initializeBreadCrumbs(quadModel);
	}
	
	public void initializeBreadCrumbs(QuadModel<?, ?> quadModel){
		List<? extends ParamConfig<?>> params = quadModel.getView().getConfig().getParams();
		for(ParamConfig<?> param : params){
			updateBreadCrumbState(quadModel, param, VISIBLE_ATTRIBUTE, Boolean.FALSE);
			updateBreadCrumbState(quadModel, param, ACTIVATE_ATTRIBUTE, Boolean.FALSE);
		}		
	}
	
	public void activateBreadCrumbForPage(QuadModel<?, ?> quadModel, ParamConfig<?> currentPage){
		updateBreadCrumbState(quadModel,currentPage,VISIBLE_ATTRIBUTE,Boolean.TRUE);
	}
	
	
	public void displayBreadCrumbForPage(QuadModel<?, ?> quadModel, ParamConfig<?> currentPage){
		hideAllBreadCrumbs(quadModel);
		updateBreadCrumbState(quadModel,currentPage,ACTIVATE_ATTRIBUTE,Boolean.TRUE);
	}
	
	public String getAssociatedBreadCrumbForPage(ParamConfig<?> currentPage){
		if(currentPage.getUiStyles() == null)
			return null;
		String associatedBreadCrumb = (String)currentPage.getUiStyles().getAttributes().get(BREADCRUMB_ATTRIBUTE);
		return (associatedBreadCrumb != null && !associatedBreadCrumb.equals("none")) ? associatedBreadCrumb : null;
	}
	
	private void hideAllBreadCrumbs(QuadModel<?, ?> quadModel){
		List<? extends ParamConfig<?>> params = quadModel.getView().getConfig().getParams();
		for(ParamConfig<?> param : params){
			updateBreadCrumbState(quadModel, param, ACTIVATE_ATTRIBUTE, Boolean.FALSE);
		}			
	}

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

        		quadModel.getView().findParamByPath(statePath.toString()).setState(state);
        	}else{
        		currentSAC.findParamByPath(statePath.toString()).setState(state);
        	}
            currentSAC = (DefaultParamState<?>)quadModel.getView().findStateByPath(Constants.SEPARATOR_URI.code+breadCrumb);
        	rootNode = false;
        }
    }
}

