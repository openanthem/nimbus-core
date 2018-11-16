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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import org.apache.commons.lang.StringUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

/**
 <p>Default StateEventHandler for <tt>ViewConfig.Grid</tt> that resolves
 * dynamic values for postButtonUrl attribute of Grid with the relative param values
 * 
 * @author Swetha Vemuri
 * @since 1.1.9
 *
 */
public class GridStateEventHandler implements OnStateLoadHandler<Grid> {
	
	private static String RESOLVE_URL_ATTR_NAME = "postButtonUri";

	protected final CommandPathVariableResolver pathVariableResolver;
	
	public GridStateEventHandler(BeanResolverStrategy beanResolver) {
		this.pathVariableResolver = beanResolver.get(CommandPathVariableResolver.class);
	}

	@Override
	public void onStateLoad(Grid configuredAnnotation, Param<?> param) {
		if (configuredAnnotation == null)
			return;
		
		if(! param.getConfig().getUiStyles().getAttributes().containsKey(RESOLVE_URL_ATTR_NAME))
			return;
		
	//	if(StringUtils.isEmpty((String) param.getConfig().getUiStyles().getAttributes().get("")))
	//		return;
			
		String resolvedUrl = pathVariableResolver.resolve(param, configuredAnnotation.postButtonUri());
		String path = param.findParamByPath(resolvedUrl) != null ? param.findParamByPath(resolvedUrl).getPath() : null;
		param.getConfig().getUiStyles().getAttributes().replace(RESOLVE_URL_ATTR_NAME, path);		
	}
}
