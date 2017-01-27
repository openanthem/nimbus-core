package com.anthem.nimbus.platform.spec.model.view.dsl.config;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Message {
	
	private String content;
	
	public enum Type {
		success,
		info,
		warning,
		danger
	}
	
	private Type type;
}
