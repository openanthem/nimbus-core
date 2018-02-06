/**
 * 
 */
package com.antheminc.oss.nimbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.antheminc.oss.nimbus.app.extension.config.DefaultCoreConfiguration;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.session.HttpSessionProvider;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;

/**
 * @author Soham Chakravarti
 *
 */
@SpringBootApplication(scanBasePackageClasses=DefaultCoreConfiguration.class)
public class FrameworkTestScenariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrameworkTestScenariosApplication.class, args);
	}
	
	@Bean
	public SessionProvider sessionProvider(BeanResolverStrategy beanResolver){
		return new HttpSessionProvider() {
			
			@Override
			public String getSessionId() {
				return "";
			}
		};
	}
}