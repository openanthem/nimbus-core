/**
 * 
 */
package com.anthem.oss.nimbus.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.anthem.oss.nimbus.core.config.DefaultCoreConfiguration;

/**
 * @author Soham Chakravarti
 *
 */
@SpringBootApplication(scanBasePackageClasses=DefaultCoreConfiguration.class)
public class TestFrameworkIntegrationScenariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestFrameworkIntegrationScenariosApplication.class, args);
	}
}
