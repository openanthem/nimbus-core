/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.support;

import java.util.function.Supplier;

import org.owasp.esapi.codecs.HTMLEntityCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class JustLogit {

	private final Logger log;
	
	private static HTMLEntityCodec htmlCodec = new HTMLEntityCodec(); 
	
	final char[] IMMUNE_HTML = { ',', '.', '-', '_', ' ', '*','[',']', '(', ')', '$', '=', '+', '\n','/',':', '?', '&', '\"', '{','}', '#', '!', '@', '\'' };
	
	public JustLogit() {
		this.log = LoggerFactory.getLogger(this.getClass());
	}
	
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
	
	public void trace(Supplier<String> msg, Throwable t) {
		if(log.isTraceEnabled()) 
			log.trace(nuetralizeLog(msg),t);
	}

	public void debug(Supplier<String> msg) {
		if(log.isDebugEnabled()) 
			log.debug(nuetralizeLog(msg));
	}
	
	public void debug(Supplier<String> msg, Throwable t) {
		if(log.isDebugEnabled()) 
			log.debug(nuetralizeLog(msg), t);
	}

	public void warn(Supplier<String> msg) {
		if(log.isWarnEnabled()) 
			log.warn(nuetralizeLog(msg));
	}
	
	public void warn(Supplier<String> msg, Throwable t) {
		if(log.isWarnEnabled()) 
			log.warn(nuetralizeLog(msg), t);
	}
	
	public void info(Supplier<String> msg) {
		if(log.isInfoEnabled()) 
			log.info(nuetralizeLog(msg));
	}
	
	public void info(Supplier<String> msg, Throwable t) {
		if(log.isInfoEnabled()) 
			log.info(nuetralizeLog(msg), t);
	}

	public void error(Supplier<String> msg) {
		if(log.isErrorEnabled()) 
			log.error(nuetralizeLog(msg));
	}
	
	public void error(Supplier<String> msg, Throwable t) {
		if(log.isErrorEnabled()) 
			log.error(nuetralizeLog(msg), t);
	}
	
	private String nuetralizeLog(Supplier<String> msg) {
		if(msg.get() != null) {
			return htmlCodec.encode( IMMUNE_HTML, msg.get());
		}
		
		return null;	                     
	}
	
}
