/**
 * 
 */
package com.antheminc.oss.nimbus.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.execution.SessionProvider;
import com.antheminc.oss.nimbus.core.domain.command.execution.SpringSessionProvider;

/**
 * @author Rakesh Patel
 *
 */
@Configuration
@Profile({"!test"})
public class SessionProviderConfig {

	@Bean
	public SessionProvider sessionProvider(BeanResolverStrategy beanResolver){
		return new SpringSessionProvider();
	}
	
}
