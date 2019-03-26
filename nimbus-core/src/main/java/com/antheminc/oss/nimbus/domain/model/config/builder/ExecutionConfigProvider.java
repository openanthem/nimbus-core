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
package com.antheminc.oss.nimbus.domain.model.config.builder;

import java.lang.annotation.Annotation;

import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;

/**
 * <p>An interpreter which takes a given annotation and maps {@link Execution}
 * typed annotations into their equivalent {@link Config} annotations. See
 * implementations for how specific annotations are handled.
 * 
 * @author Rakesh Patel
 * @author Tony Lopez
 *
 */
public interface ExecutionConfigProvider<A extends Annotation> {

	/**
	 * <p>Get the main {@link Config} related statements to execute from
	 * {@code configAnnotation}.
	 * @param configAnnotation the annotation from which to retrieve
	 *            {@link Config} statements from
	 * @param eCtx the execution context
	 * @return the final {@link Config} statements
	 */
	public Config getMain(A configAnnotation, ExecutionContext eCtx);

	/**
	 * <p>Get the exception {@link Config} related statements to execute from
	 * {@code configAnnotation}.
	 * @param configAnnotation the annotation from which to retrieve
	 *            {@link Config} statements from
	 * @param eCtx the execution context
	 * @return the final {@link Config} statements
	 */
	public Config getException(A configAnnotation, ExecutionContext eCtx);

}
