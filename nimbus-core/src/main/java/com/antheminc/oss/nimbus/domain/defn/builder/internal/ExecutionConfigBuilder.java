/**
 * 
 */
package com.antheminc.oss.nimbus.domain.defn.builder.internal;

import java.lang.annotation.Annotation;

import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.defn.Execution;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;

/**
 * @author Rakesh Patel
 *
 */
public class ExecutionConfigBuilder {
	
	public static Config buildExecConfig(String url) {
		return buildExecConfig(Config.TRUE, url, Config.COL, Event.DEFAULT_ORDER_NUMBER);
	}
	
	public static Config buildExecConfig(String url, int order) {
		return buildExecConfig(Config.TRUE, url, Config.COL, order);
	}
	
	public static Config buildExecConfig(String when, String url) {
		return buildExecConfig(when, url, Config.COL, Event.DEFAULT_ORDER_NUMBER);
	}
	
	public static Config buildExecConfig(String when, String url, int order) {
		return buildExecConfig(when, url, Config.COL, order);
	}
	
	public static Config buildExecConfig(String when, String url, String col, int order) {
		return new Execution.Config() {
			public String url() {
				return url;
			}
			public String when() {
				return when;
			}
			public String col() {
				return col;
			}
			public int order() {
				return order;
			}
			public Class<? extends Annotation> annotationType() {
		        return Execution.Config.class;
		    }
		};
	}
}
