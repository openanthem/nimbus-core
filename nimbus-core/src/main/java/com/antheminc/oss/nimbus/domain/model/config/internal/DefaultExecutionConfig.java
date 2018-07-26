/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.config.internal;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import com.antheminc.oss.nimbus.domain.model.config.ExecutionConfig;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultExecutionConfig implements ExecutionConfig {
	
	private List<Annotation> execConfigs;

	public void addAll(Annotation[] configAnnotations) {
		if(execConfigs == null) {
			execConfigs = new ArrayList<Annotation>();
		}
		execConfigs.addAll(Arrays.asList(configAnnotations));
		
		
	}
	
	public void sort() {
		if(CollectionUtils.isEmpty(execConfigs))
			return;
		
		Collections.sort(execConfigs, (o1, o2) -> {
			Integer order1 = (Integer) AnnotationUtils.getAnnotationAttributes(o1).get("order");
			Integer order2 = (Integer) AnnotationUtils.getAnnotationAttributes(o2).get("order");

			return order1.compareTo(order2);
		});
	}
	
	@Override
	public List<Annotation> get() {
		return execConfigs;
	}

}
