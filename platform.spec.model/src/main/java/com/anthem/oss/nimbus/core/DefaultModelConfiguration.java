package com.anthem.oss.nimbus.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anthem.oss.nimbus.core.domain.model.state.internal.DobToAgeConverter;
import com.anthem.oss.nimbus.core.domain.model.state.internal.IdParamConverter;

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
}
