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
package com.antheminc.oss.nimbus.domain.model.config.internal;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import com.antheminc.oss.nimbus.domain.defn.Execution.Let;
import com.antheminc.oss.nimbus.domain.model.config.ExecutionConfig;

/**
 * @author Rakesh Patel
 * @author Tony Lopez
 *
 */
public class DefaultExecutionConfig implements ExecutionConfig {

	private List<Annotation> execConfigs;

	public void add(Annotation configAnnotation) {
		getExecConfigs().add(configAnnotation);
	}

	public void addAll(Annotation[] configAnnotations) {
		getExecConfigs().addAll(Arrays.asList(configAnnotations));
	}

	@Override
	public List<Annotation> get() {
		return execConfigs;
	}

	public void sort() {
		if (CollectionUtils.isEmpty(execConfigs))
			return;

		Collections.sort(execConfigs, (o1, o2) -> {
			Integer order1 = (Integer) AnnotationUtils.getAnnotationAttributes(o1).get("order");
			Integer order2 = (Integer) AnnotationUtils.getAnnotationAttributes(o2).get("order");

			int orderDifference = order1.compareTo(order2);
			
			// ensure Lets are evaluated first if order is the same
			if (orderDifference == 0) {
				if (o1 instanceof Let && !(o2 instanceof Let)) {
					return -1;
				} else if (o2 instanceof Let && !(o1 instanceof Let)) {
					return 1;
				}
				return orderDifference;
			}
			
			return orderDifference;
		});
	}

	private List<Annotation> getExecConfigs() {
		if (execConfigs == null) {
			execConfigs = new ArrayList<Annotation>();
		}
		return execConfigs;
	}
}
