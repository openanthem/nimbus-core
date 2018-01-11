package com.antheminc.oss.nimbus.core.domain.model.state.internal;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.core.domain.definition.Converters.ParamConverter;



/**
 * @author Rakesh Patel
 *
 */
public class IdParamConverter implements ParamConverter<Long, String> {

	public static final String PREFIX = "ANT";
	
	@Override
	public String serialize(Long input) {
		if(input == null) return null;
		
		StringBuilder output = new StringBuilder().append(PREFIX).append(input);
		return output.toString();
	}

	@Override
	public Long deserialize(String input) {
		if(input == null) return null;
		
		String output = StringUtils.stripStart(input, PREFIX);
		if(StringUtils.isEmpty(output)) return null;
		
		return (long)Long.valueOf(output);
		
		
	}

}
