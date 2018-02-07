/**
 * 
 */
package com.antheminc.oss.nimbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.antheminc.oss.nimbus.app.extension.config.DefaultCoreConfiguration;

/**
 * @author Soham Chakravarti
 *
 */
@SpringBootApplication(scanBasePackageClasses=DefaultCoreConfiguration.class)
public class FrameworkTestScenariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrameworkTestScenariosApplication.class, args);
	}

}