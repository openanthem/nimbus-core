/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.anthem.oss.nimbus.core.entity.access;

import java.util.HashSet;
import java.util.Set;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Cache;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;

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
