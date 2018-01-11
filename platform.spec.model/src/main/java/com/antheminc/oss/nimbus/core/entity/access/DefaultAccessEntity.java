/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.access;

import java.util.HashSet;
import java.util.Set;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Cache;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="defaultAccessEntity", includeListeners={ListenerType.persistence})
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@Getter @Setter @ToString(callSuper=true)
public class DefaultAccessEntity extends AbstractEntity.IdString implements AccessEntity {
	
	private static final long serialVersionUID = 1L;
	
    
	private String type;
	
	private String code;
	
	private String name;
	
	private String domainUri;
	
	private Set<DefaultAccessEntity> availableAccesses;
	
	private Set<Permission> availablePermissions;
	

	public DefaultAccessEntity() { }
	
	public DefaultAccessEntity(String type) { 
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
	public void addNestedAccess(DefaultAccessEntity nested) {
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
	
	
	
	public static class Platform extends DefaultAccessEntity {
		
		private static final long serialVersionUID = 1L;
		

		public Platform() {
			setType("PLATFORM");
		}
	}

	
	
	public static class Application extends DefaultAccessEntity {

		private static final long serialVersionUID = 1L;
		

		public Application() {
			setType("APPLICATION");
		}
	}
	
	
	
	public static class Module extends DefaultAccessEntity {

		private static final long serialVersionUID = 1L;

		
		public Module() {
			setType("MODULE");
		}
	}
	
	
	
	public static class Feature extends DefaultAccessEntity {
		
		private static final long serialVersionUID = 1L;

		
		public Feature() {
			setType("FEATURE");
		}
	}
		
}
