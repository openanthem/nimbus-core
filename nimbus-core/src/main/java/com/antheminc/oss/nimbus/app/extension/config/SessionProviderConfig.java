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
package com.antheminc.oss.nimbus.app.extension.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.SessionProvider;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.SpringSessionProvider;

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
