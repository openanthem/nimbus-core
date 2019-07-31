/**
 *  Copyright 2016-2019 the original author or authors.
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

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.converter.excel.ExcelFileImporter;
import com.antheminc.oss.nimbus.converter.excel.UnivocityExcelToCSVConverter;
import com.antheminc.oss.nimbus.converter.tabular.TabularDataFileImporter;
import com.antheminc.oss.nimbus.converter.tabular.UnivocityCsvParser;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.DefaultFileImportGateway;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Tony Lopez
 *
 */
@Configuration
public class DefaultFrameworkImportConfig {

	@Bean
	public DefaultFileImportGateway defaultFileImportGateway(BeanResolverStrategy beanResolver) {
		return new DefaultFileImportGateway(beanResolver);
	}
	
	@Bean
	public ExcelFileImporter excelFileImporter(TabularDataFileImporter tabularDataFileImporter, UnivocityExcelToCSVConverter excelToCsvConverter) {
		return new ExcelFileImporter(excelToCsvConverter, tabularDataFileImporter);
	}
	
	@Bean
	public UnivocityCsvParser univocityCsvParser(DomainConfigBuilder domainConfigBuilder) {
		return new UnivocityCsvParser(domainConfigBuilder);
	}
	
	@Bean
	public TabularDataFileImporter tabularDataFileImporter(DomainConfigBuilder domainConfigBuilder, CommandExecutorGateway commandGateway, ObjectMapper om, UnivocityCsvParser univocityCsvParser,  ModelRepositoryFactory modelRepositoryFactory) {
		return new TabularDataFileImporter(commandGateway, domainConfigBuilder, om, univocityCsvParser, modelRepositoryFactory);
	}
}
