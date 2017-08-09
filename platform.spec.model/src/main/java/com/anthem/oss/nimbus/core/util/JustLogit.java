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
			log.trace(nuetralizeLog(msg));
	}

	public void debug(Supplier<String> msg) {
		if(log.isDebugEnabled()) 
			log.debug(nuetralizeLog(msg));
	}

	public void warn(Supplier<String> msg) {
		if(log.isWarnEnabled()) 
			log.warn(nuetralizeLog(msg));
	}

	
	public void info(Supplier<String> msg) {
		if(log.isInfoEnabled()) 
			log.info(nuetralizeLog(msg));
	}

	public void error(Supplier<String> msg) {
		if(log.isErrorEnabled()) 
			log.error(nuetralizeLog(msg));
	}
	
	private String nuetralizeLog(Supplier<String> msg) {
		if(msg.get() != null) {
			return SecurityUtils.scanObjectForSecureLogging(msg.get(), SecurityUtils.SECURE);
		}
			return msg.get();		                     
	}
	
}
