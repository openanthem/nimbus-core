package com.anthem.oss.nimbus.core.domain.model.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.anthem.oss.nimbus.core.domain.model.state.internal.IdParamConverter;

/**
 * @author Sandeep Mantha
 *
 */

@Configuration
public class DefaultModelConfig {

	@Bean
	public IdParamConverter idParamConverter(){
		return new IdParamConverter();
	}
}
