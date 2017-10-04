/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model;

import java.util.Optional;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.InvalidStateException;
import com.anthem.oss.nimbus.core.domain.model.state.internal.StateContextEntity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

/**
 * @author Soham Chakravarti
 *
 */
public class StateContextJSONFilter extends SimpleBeanPropertyFilter {

	private static final StateContextEntity K_SHELL_INSTANCE = new StateContextEntity();
	
	@Override
	public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
	throws Exception {
		if(!writer.getName().equals("contextModel")) {
			super.serializeAsField(pojo, jgen, provider, writer);
			return;
		}
		
		if(pojo==null)
			return;
		
		if(!(pojo instanceof Param)) 
			throw new InvalidStateException("contextModel is expected of type Param but found of type: "+pojo.getClass());
		
		Param<?> p = (Param<?>)pojo;
		if(Optional.ofNullable(p.getContextModel()).map(Model::getState).filter(ctx->!ctx.equals(K_SHELL_INSTANCE)).isPresent())
			super.serializeAsField(pojo, jgen, provider, writer);
	}

}
