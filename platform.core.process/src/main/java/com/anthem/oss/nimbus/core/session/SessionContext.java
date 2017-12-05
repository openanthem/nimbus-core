/**
 * 
 */
package com.antheminc.oss.nimbus.core.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jayant Chaudhuri
 * Platform context for storing variables as key value pairs.
 */
public class SessionContext implements Serializable{

	private static final long serialVersionUID = 1L;

	private Map<String,Object> context = new HashMap<String,Object>();
	

	public void setAttribute(String key, Object value){
		context.put(key, value);
	};

	public Object getAttribute(String key){
		return context.get(key);
	};
	
}
