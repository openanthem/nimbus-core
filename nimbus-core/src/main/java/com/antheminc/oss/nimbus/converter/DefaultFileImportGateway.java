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
package com.antheminc.oss.nimbus.converter;

import java.util.Collection;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;

import lombok.Getter;

/**
 * @author Sandeep Mantha
 * @author Tony Lopez
 *
 */
@Getter
public class DefaultFileImportGateway implements FileImportGateway {

	private final Collection<Importer> fileImporters;
	
	public DefaultFileImportGateway(BeanResolverStrategy beanResolver) {
		this.fileImporters = beanResolver.getMultiple(Importer.class);
	}

	@Override
	public Importer getFileImporter(String extension) {
		for(Importer fileImporter: getFileImporters()) {
			if (fileImporter.supports(extension)) {
				return fileImporter;
			}
		}
		return null;
	}
}
