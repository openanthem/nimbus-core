package com.antheminc.oss.nimbus.test.scenarios.s0.core;

import com.antheminc.oss.nimbus.domain.defn.Model;

import lombok.Getter;
import lombok.Setter;

@Model
@Getter @Setter
public class InnerTestModel {

	private String inner_attr1;
	
	private String inner_attr2;
	
}
