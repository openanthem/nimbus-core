package com.antheminc.oss.nimbus.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.antheminc.oss.nimbus.core.domain.model.state.internal.DobToAgeConverter;
import com.antheminc.oss.nimbus.core.domain.model.state.internal.IdParamConverter;
import com.antheminc.oss.nimbus.core.domain.model.state.internal.StaticCodeValueBasedCodeToLabelConverter;

/**
 * @author Sandeep Mantha
 *
 */

@Configuration
public class DefaultModelConfiguration {

	@Bean
	public IdParamConverter idParamConverter(){
		return new IdParamConverter();
	}
	
	@Bean
	public DobToAgeConverter dobToAgeConverter(){
		return new DobToAgeConverter();
	}
	
	@Bean
	public StaticCodeValueBasedCodeToLabelConverter staticCodeValueBasedCodeToLabelConverter(){
		return new StaticCodeValueBasedCodeToLabelConverter();
	}
}
