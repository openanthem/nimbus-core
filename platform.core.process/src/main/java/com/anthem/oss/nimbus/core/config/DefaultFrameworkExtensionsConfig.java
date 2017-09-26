/**
 * 
 */
package com.anthem.oss.nimbus.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.model.state.notification.ActivateConditionalNotificationHandler;

/**
 * @author Soham Chakravarti
 *
 */
@Configuration
public class DefaultFrameworkExtensionsConfig {

	@Bean
	public ActivateConditionalNotificationHandler extensionActivateConditionalHandler(BeanResolverStrategy beanResolver) {
		return new ActivateConditionalNotificationHandler(beanResolver);
	}
}
