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

import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

/**
 * @author Soham Chakravarti
 *
 */
public abstract class AbstractEntityStateJsonFilter extends SimpleBeanPropertyFilter {

	protected static final String FIELD_NAME_CONFIG = "config"; 
	
	@Override
	public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
	throws Exception {
		
		if (include(writer)) {
			
			boolean shouldOmmit = omitConditional(pojo, writer.getName());
			if(shouldOmmit)
				return;

			writer.serializeAsField(pojo, jgen, provider);
			return;
	        		
		} else if (!jgen.canOmitFields()) { 
	    	writer.serializeAsOmittedField(pojo, jgen, provider);
	    }
	}

	protected final boolean omitConditional(Object pojo, String fieldName) {
		if(Param.class.isInstance(pojo)) {
			Param<?> param = Param.class.cast(pojo);
			
			if(FIELD_NAME_CONFIG.equals(fieldName)) {
				Model<?> rootDomain = param.getRootDomain();
				if(rootDomain != null 
						&& param != rootDomain.getAssociatedParam())
					return true;
				else
					return false;
			} 
			
			return omitConditionalParam(param, fieldName);
			
		} else if(Model.class.isInstance(pojo)) {
			Model<?> model = Model.class.cast(pojo);
			
			if(FIELD_NAME_CONFIG.equalsIgnoreCase(fieldName))
				return true;
			
			return omitConditionalModel(model, fieldName);
		}		
		
		return false;
	}
	
	protected abstract boolean omitConditionalParam(Param<?> param, String fieldName);
	
	protected abstract boolean omitConditionalModel(Model<?> model, String fieldName);
}
