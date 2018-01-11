package com.antheminc.oss.nimbus.test.sample.domain.model.core;

import com.antheminc.oss.nimbus.core.domain.definition.Model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Model
@Getter @Setter
public class SampleCoreNestedEntity {
	
	private String nested_attr_String;
}
