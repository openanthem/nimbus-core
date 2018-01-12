/**
 *
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
package com.anthem.nimbus.platform.web;

import java.time.LocalDate;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.anthem.nimbus.platform.spec.serializer.CustomLocalDateSerializer;

@EnableDiscoveryClient
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, BatchAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class })
@EnableConfigurationProperties
//TODO - more packages not to be added. this is a temporary fix to load the seviceclient beans.
@ComponentScan(basePackages={
	"com.anthem.cm.ltss.extension.conf", 
	"com.anthem.nimbus.platform.client.extension.petclinic",
	"com.anthem.platform.client.extension.cm.service", 
	"com.anthem.nimbus.platform.web", 
	"com.anthem.platform.scheduler.extension", 
	"com.anthem.platform.scheduler.test"
})
public class Application {

	public static void main(String[] args) {
		
	//	SpringApplication.run(Application.class, args);
		GenericApplicationContext context = (GenericApplicationContext)SpringApplication.run(Application.class, args); 
		((DefaultListableBeanFactory)context.getBeanFactory()).setSerializationId("platform-web:1");
		System.out.println("**** Platform-web ***** Started !!");
	}

	@Bean
	public Jackson2ObjectMapperBuilder jacksonBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.serializerByType(LocalDate.class, new CustomLocalDateSerializer());
		return builder;
	}

}
