package com.anthem.oss.nimbus.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anthem.oss.nimbus.core.domain.model.state.internal.DobToAgeConverter;
import com.anthem.oss.nimbus.core.domain.model.state.internal.IdParamConverter;
import com.anthem.oss.nimbus.core.domain.model.state.internal.StaticCodeValueBasedCodeToLabelConverter;

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
