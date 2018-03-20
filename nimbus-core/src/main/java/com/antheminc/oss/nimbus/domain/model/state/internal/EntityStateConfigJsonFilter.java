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

import com.antheminc.oss.nimbus.UnsupportedScenarioException;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

/**
 * @author Soham Chakravarti
 *
 */
public class EntityStateConfigJsonFilter extends SimpleBeanPropertyFilter {

	@Override
	public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
	throws Exception {
		
		if (include(writer)) {
			
			if(EntityState.class.isInstance(pojo) && writer.getName().equals("config")) {
				EntityState<?> es = getEntityState(pojo);
				
				Model<?> rootDomain = es.getRootDomain();
				if(rootDomain != null 
						&& es != rootDomain.getAssociatedParam())
					return;
			} 

			writer.serializeAsField(pojo, jgen, provider);
			return;
	        		
		} else if (!jgen.canOmitFields()) { 
	    	writer.serializeAsOmittedField(pojo, jgen, provider);
	    }
	}
	
	private EntityState<?> getEntityState(Object o) {
		if(EntityState.class.isInstance(o)) 
			return EntityState.class.cast(o);
		
		throw new UnsupportedScenarioException("Unsupported type, expected "+EntityState.class.getSimpleName()
				+" but found JsonGenerator.getCurrentValue(): "+ o);
	}

}