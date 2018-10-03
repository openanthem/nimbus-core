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
package com.antheminc.oss.nimbus.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.domain.defn.ConfigNature.Ignore;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.SearchNature.StartsWith;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class for all domain model.
 * Implements {@link Persistable} for the audit fields {@link CreatedBy} and {@link CreatedDate} since the {@link Id} field is manually assigned.
 * 
 * @author Soham Chakravarti
 */
@Model
@Getter
public abstract class AbstractEntity<ID extends Serializable> implements Serializable, Persistable<ID> {
	private static final long serialVersionUID = 1L;
	
	@Ignore
	private final String _class = this.getClass().getName(); 

	public static abstract class IdLong extends AbstractEntity<Long> {
		private static final long serialVersionUID = 1L;
		
		@Id @Getter @Setter 
		private Long id;
	}
	
    public static abstract class IdString extends AbstractEntity<String> {
		private static final long serialVersionUID = 1L;

		@Id @Getter @Setter @StartsWith
		private String id;
	}
	
	
    public abstract ID getId();

    @Setter @CreatedBy
	private String createdBy;
	
    @Setter @CreatedDate
	private ZonedDateTime createdDate;
	
    @Setter @LastModifiedBy
	private String lastModifiedBy;
	
    @Setter @LastModifiedDate
	private ZonedDateTime lastModifiedDate;
	
    //@Version
	@Setter private long version;
	
	
	@JsonIgnore
	public <T extends AbstractEntityBehavior<M, ID>, M extends AbstractEntity<ID>> T newBehaviorInstance(Class<T> clazz) {
		try {
			return ConstructorUtils.invokeExactConstructor(clazz, this);
		} 
		catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to instantiate class of type: " + clazz, ex);
		}
	}
	
	@JsonIgnore
	@Override
	public boolean isNew() {
		return this.lastModifiedDate == null;
	}
	
}
