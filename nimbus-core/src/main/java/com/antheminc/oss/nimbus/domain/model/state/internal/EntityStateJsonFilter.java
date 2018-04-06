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
package com.antheminc.oss.nimbus.domain.model.state.internal;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListElemParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public class EntityStateJsonFilter extends AbstractEntityStateJsonFilter {

	@Override
	protected boolean omitConditionalParam(Param<?> param, String fieldName) {
		// enabled: true
		if(StringUtils.equals(fieldName, "enabled") && param.isEnabled())
			return true;
		
		// visible: true
		if(StringUtils.equals(fieldName, "visible") && param.isVisible())
			return true;
		
		// collection: false
		if(StringUtils.equals(fieldName, "collection") && !param.isCollection())
			return true;
		
		// collectionElem: false
		if(StringUtils.equals(fieldName, "collectionElem") && !param.isCollectionElem())
			return true;
		
		// nested: false
		if(StringUtils.equals(fieldName, "nested") && !param.isNested())
			return true;
		
		// activeValidationGroups: []
		if(StringUtils.equals(fieldName, "activeValidationGroups") && ArrayUtils.isEmpty(param.getActiveValidationGroups()))
			return true;
		
		// type: {name: "string", collection: false, nested: false}
		if(StringUtils.equals(fieldName, "type") && 
				StringUtils.equals(param.getType().getName(), "string") && !param.getType().isNested())
			return true;
		
		// leafState - serialize only when its leaf OR collection and not when its nested
		if(StringUtils.equals(fieldName, "leafState")) {
			
			if(param.isLeaf())
				return false; // include for leaf
			
			// include when collection and has @Grid annotation
			else if(param.isCollectionElem() && hasGrid(param.findIfCollectionElem()))
				return false;
			
			else
				return true;
		}
		return false;
	}
	
	private boolean hasGrid(ListElemParam<?> elemParam) {
		Param<?> colParam = elemParam.getParentModel().getAssociatedParam();
		if(colParam.getConfig().getUiStyles()==null)
			return false;
		
		return colParam.getConfig().getUiStyles().getAnnotation().annotationType()==Grid.class;
	}
	
	@Override
	protected boolean omitConditionalModel(Model<?> model, String fieldName) {
		return false;
	}
}