package com.anthem.oss.nimbus.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.test.context.ActiveProfiles;
/**
 * @author Soham Chakravarti
 *
 */
@SpringBootApplication
@ConfigurationProperties
@ActiveProfiles("test")
public class TestCoreWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestCoreWebApplication.class, args);
	}
}
