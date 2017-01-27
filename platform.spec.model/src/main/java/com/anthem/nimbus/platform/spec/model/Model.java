/**
 * 
 */
package com.anthem.nimbus.platform.spec.model;


import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * @author Soham Chakravarti
 *
 */
public interface Model extends Serializable {

	

	public interface Core extends Serializable {}
	
	
	
	@Getter @Setter @ToString
	@CoreDomain("default-id")
	public static class DefaultID implements Model {
		
		private static final long serialVersionUID = 1L;
		
		@Id
		private String id;
	}
	
    
	
	@Getter @Setter @ToString(callSuper=true)
	@CoreDomain("default-id-version")
	public static class DefaultIDAndVersion extends DefaultID {
		
		private static final long serialVersionUID = 1L;
		
		@Version
		private Long version;
	}

}
