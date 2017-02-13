/**
 * 
 */
package com.anthem.oss.nimbus.core.util;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Soham Chakravarti
 *
 */
public class JustLogit {

	private final Logger log;

	
	public JustLogit(Class<?> clazz) {
		log = LoggerFactory.getLogger(clazz);
	}

	public JustLogit(String name) {
		log = LoggerFactory.getLogger(name);
	}
	
	
	public void trace(Supplier<String> msg) {
		if(log.isTraceEnabled()) 
			log.trace(msg.get());
	}

	public void debug(Supplier<String> msg) {
		if(log.isDebugEnabled()) 
			log.debug(msg.get());
	}

	public void warn(Supplier<String> msg) {
		if(log.isWarnEnabled()) 
			log.warn(msg.get());
	}

	
	public void info(Supplier<String> msg) {
		if(log.isInfoEnabled()) 
			log.info(msg.get());
	}

	public void error(Supplier<String> msg) {
		if(log.isErrorEnabled()) 
			log.error(msg.get());
	}
	
}
