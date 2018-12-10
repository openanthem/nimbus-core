/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.antheminc.oss.nimbus.app.extension.config.DefaultCoreConfigMarker;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.test.domain.model.state.extension.TestConflictingEventOverrideHandler;
import com.antheminc.oss.nimbus.test.domain.model.state.extension.TestEventAllowOverrideHandler;
import com.antheminc.oss.nimbus.test.domain.model.state.extension.TestEventMandatoryOverrideHandler;
import com.antheminc.oss.nimbus.test.domain.model.state.extension.TestEventNoOverrideHandler;
import com.antheminc.oss.nimbus.test.domain.model.state.extension.TestOnStateChangeNoOverrideHandler;
import com.antheminc.oss.nimbus.test.domain.model.state.extension.TestRedundantEventOverrideHandler;
import com.antheminc.oss.nimbus.test.domain.session.TestSessionProvider;

/**
 * @author Soham Chakravarti
 *
 */
@SpringBootApplication(scanBasePackageClasses=DefaultCoreConfigMarker.class)
public class FrameworkIntegrationTestScenariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrameworkIntegrationTestScenariosApplication.class, args);
	}
	
	@Bean
	public SessionProvider sessionProvider(BeanResolverStrategy beanResolver){
		return new TestSessionProvider();
	}
	
	@Bean
	TestEventNoOverrideHandler testEventNoOverrideHandler(BeanResolverStrategy beanResolver) {
		return new TestEventNoOverrideHandler(beanResolver);
	}
	
	@Bean
	TestEventAllowOverrideHandler testEventAllowOverrideHandler(BeanResolverStrategy beanResolver) {
		return new TestEventAllowOverrideHandler(beanResolver);
	}
	
	@Bean
	TestConflictingEventOverrideHandler testConflictingEventOverrideHandler(BeanResolverStrategy beanResolver) {
		return new TestConflictingEventOverrideHandler(beanResolver);
	}
	
	@Bean
	TestEventMandatoryOverrideHandler testEventMandatoryOverrideHandler(BeanResolverStrategy beanResolver) {
		return new TestEventMandatoryOverrideHandler(beanResolver);
	}
	
	@Bean
	TestOnStateChangeNoOverrideHandler testOnStateChangeNoOverrideHandler(BeanResolverStrategy beanResolver) {
		return new TestOnStateChangeNoOverrideHandler(beanResolver);
	}
	
	@Bean
	TestRedundantEventOverrideHandler TestRedundantEventOverrideHandler(BeanResolverStrategy beanResolver) {
		return new TestRedundantEventOverrideHandler(beanResolver);
	}
	
}
