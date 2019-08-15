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
package com.antheminc.oss.nimbus.domain.support;

import java.lang.annotation.Annotation;
import java.util.Collections;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;

import com.querydsl.apt.AbstractQuerydslProcessor;
import com.querydsl.apt.Configuration;
import com.querydsl.apt.DefaultConfiguration;
import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntities;
import com.querydsl.core.annotations.QuerySupertype;
import com.querydsl.core.annotations.QueryTransient;

/**
 * @author Sandeep Mantha
 * @author Soham Chakravarti
 */
@SupportedAnnotationTypes({ 
	"com.mysema.query.annotations.*", 
	"com.antheminc.oss.nimbus.domain.defn.*" })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DomainQuerydslProcessor extends AbstractQuerydslProcessor {

	private String annClassName = "com.antheminc.oss.nimbus.domain.defn.Domain";
	
	@SuppressWarnings("unchecked")
	private Class<? extends Annotation> lazyLoadClass() {
		try {
			return (Class<? extends Annotation>)Class.forName(annClassName);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Failed to load annotation for definition entities: "+annClassName, ex);
		}
	}
	
	@Override
	protected Configuration createConfiguration(RoundEnvironment roundEnv) {

		processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());

		
		DefaultConfiguration configuration = new DefaultConfiguration(roundEnv, processingEnv.getOptions(),
				Collections.<String> emptySet(), QueryEntities.class, lazyLoadClass(), QuerySupertype.class,
				QueryEmbeddable.class, QueryEmbedded.class, QueryTransient.class);

		configuration.setUnknownAsEmbedded(true);

		return configuration;
	}
}