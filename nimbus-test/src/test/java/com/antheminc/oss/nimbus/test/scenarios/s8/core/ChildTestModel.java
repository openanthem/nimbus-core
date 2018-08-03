package com.antheminc.oss.nimbus.test.scenarios.s8.core;

import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Model
@Getter @Setter
public class ChildTestModel extends IdLong {
	private static final long serialVersionUID = 1L;
	private String para3;
	private String para4;
	private String para5;
}
