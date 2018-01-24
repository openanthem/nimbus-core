package com.antheminc.oss.nimbus;
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
//package com.anthem.oss.nimbus.core;
//
//import java.util.Collections;
//
//import javax.annotation.processing.RoundEnvironment;
//import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.annotation.processing.SupportedSourceVersion;
//import javax.lang.model.SourceVersion;
//import javax.tools.Diagnostic;
//
//import com.anthem.oss.nimbus.core.domain.definition.Domain;
//import com.querydsl.apt.AbstractQuerydslProcessor;
//import com.querydsl.apt.Configuration;
//import com.querydsl.apt.DefaultConfiguration;
//import com.querydsl.core.annotations.QueryEmbeddable;
//import com.querydsl.core.annotations.QueryEmbedded;
//import com.querydsl.core.annotations.QueryEntities;
//import com.querydsl.core.annotations.QuerySupertype;
//import com.querydsl.core.annotations.QueryTransient;
///**
// * @author Sandeep Mantha
// *
// */
//@SupportedAnnotationTypes({ "com.mysema.query.annotations.*", "com.anthem.oss.nimbus.core.domain.definition.*","org.springframework.data.mongodb.core.mapping.*" })
//@SupportedSourceVersion(SourceVersion.RELEASE_6)
//public class DomainAnnotationProcessor extends AbstractQuerydslProcessor {
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.mysema.query.apt.AbstractQuerydslProcessor#createConfiguration(javax.annotation.processing.RoundEnvironment)
//	 */
//	@Override
//	protected Configuration createConfiguration(RoundEnvironment roundEnv) {
//
//		processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());
//
//		DefaultConfiguration configuration = new DefaultConfiguration(roundEnv, processingEnv.getOptions(),
//				Collections.<String> emptySet(), QueryEntities.class,Domain.class, QuerySupertype.class,
//				QueryEmbeddable.class, QueryEmbedded.class, QueryTransient.class);
//		configuration.setUnknownAsEmbedded(true);
//
//		return configuration;
//	}
//}
