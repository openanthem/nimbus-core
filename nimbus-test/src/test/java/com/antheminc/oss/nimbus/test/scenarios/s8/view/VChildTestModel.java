package com.antheminc.oss.nimbus.test.scenarios.s8.view;

import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.test.scenarios.s8.core.ChildTestModel;

import lombok.Getter;
import lombok.Setter;

//@Model
@Getter
@Setter
@MapsTo.Type(ChildTestModel.class)
public class VChildTestModel {
	private String para3;
}
