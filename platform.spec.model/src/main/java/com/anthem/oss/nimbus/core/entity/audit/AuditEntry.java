/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.audit;

import com.antheminc.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class AuditEntry extends AbstractEntity.IdString {

	private static final long serialVersionUID = 1L;

	private String domainRootAlias;
	
	private String domainRootRefId;
	
	private String propertyPath;
	
	private String propertyType;
	
	private Object oldValue;
	
	private Object newValue;
	
}
