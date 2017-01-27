/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.access;

import java.util.HashSet;
import java.util.Set;

import com.anthem.nimbus.platform.spec.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public class PlatformAccessEntity extends AbstractModel.IdLong implements AccessEntity {
	
	private static final long serialVersionUID = 1L;
	
    
	private String type;
	
	private String code;
	
	private String name;
	
	private String domainUri;
	
	private Set<PlatformAccessEntity> availableAccesses;
	
	private Set<Permission> availablePermissions;
	

	public PlatformAccessEntity() { }
	
	public PlatformAccessEntity(String type) { 
		setType(type);
	}
	

	@Override
	public Set<Permission> getPermissions() {
		return getAvailablePermissions();
	}
	
	
	/**
	 * 
	 * @param nested
	 */
	public void addNestedAccess(PlatformAccessEntity nested) {
		if(getAvailableAccesses() == null) {
			setAvailableAccesses(new HashSet<>());
		}
		getAvailableAccesses().add(nested);
	}
	
	/**
	 * 
	 * @param p
	 */
	public void addAvailablePermission(Permission p) {
		if(getAvailablePermissions() == null) {
			setAvailablePermissions(new HashSet<>());
		}
		getAvailablePermissions().add(p);
	}
	
	
	
	public static class Platform extends PlatformAccessEntity {
		
		private static final long serialVersionUID = 1L;
		

		public Platform() {
			setType("PLATFORM");
		}
	}

	
	
	public static class Application extends PlatformAccessEntity {

		private static final long serialVersionUID = 1L;
		

		public Application() {
			setType("APPLICATION");
		}
	}
	
	
	
	public static class Module extends PlatformAccessEntity {

		private static final long serialVersionUID = 1L;

		
		public Module() {
			setType("MODULE");
		}
	}
	
	
	
	public static class Feature extends PlatformAccessEntity {
		
		private static final long serialVersionUID = 1L;

		
		public Feature() {
			setType("FEATURE");
		}
	}
		
}
