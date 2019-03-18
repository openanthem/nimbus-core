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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@AllArgsConstructor
public class DefaultExecutionConfig implements ExecutionConfig {

	@Getter @Setter
	private Context context;
	private List<Annotation> execConfigs;

	public DefaultExecutionConfig() {
		this.context = new Context();
		this.execConfigs = new ArrayList<>();
	}
	
	public void add(Annotation configAnnotation) {
		execConfigs.add(configAnnotation);
	}

	public void addAll(Annotation[] configAnnotations) {
		execConfigs.addAll(Arrays.asList(configAnnotations));
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

			return order1.compareTo(order2);
		});
	}
}
