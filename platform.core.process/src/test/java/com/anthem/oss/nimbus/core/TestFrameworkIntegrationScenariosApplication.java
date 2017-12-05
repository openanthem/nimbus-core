/**
 * 
 */
package com.antheminc.oss.nimbus.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.antheminc.oss.nimbus.core.config.DefaultCoreConfiguration;
import com.antheminc.oss.nimbus.core.domain.command.execution.SessionProvider;

/**
 * @author Soham Chakravarti
 *
 */
@SpringBootApplication(scanBasePackageClasses=DefaultCoreConfiguration.class)
public class TestFrameworkIntegrationScenariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestFrameworkIntegrationScenariosApplication.class, args);
	}
	
	@Bean
	public SessionProvider sessionProvider(BeanResolverStrategy beanResolver){
		return new SessionProvider() {
			@Override
			public String getSessionId() {
				return "";
			}
		};
	}
}
