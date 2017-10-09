/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.audit;

import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class AuditEntry extends AbstractEntity.IdString {

	private static final long serialVersionUID = 1L;

	private String entityId;
	
	private String property;
	
	private Object oldValue;
	
	private Object newValue;
	
	public AuditEntry() { }
	
	public AuditEntry(String entityId, String property, Object newValue) {
		this(entityId, property, null, newValue);
	}
	
	public AuditEntry(String entityId, String property, Object oldValue, Object newValue) {
		setEntityId(entityId);
		setProperty(property);
		setOldValue(oldValue);
		setNewValue(newValue);
	}
	
	
}
