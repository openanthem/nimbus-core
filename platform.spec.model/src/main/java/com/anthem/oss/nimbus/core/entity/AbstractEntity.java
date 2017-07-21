/**
 * 
 */
package com.anthem.oss.nimbus.core.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.definition.ConfigNature.Ignore;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class for all domain model.
 * 
 * @author Soham Chakravarti
 */
@Model
@Getter
public abstract class AbstractEntity<ID extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;

	public static abstract class IdLong extends AbstractEntity<Long> {
		private static final long serialVersionUID = 1L;
		
		@Id @Getter @Setter 
		private Long id;
	}
	
    public static abstract class IdString extends AbstractEntity<String> {
		private static final long serialVersionUID = 1L;

		@Id @Getter @Setter
		private String id;
	}
	
	
    public abstract ID getId();


	@Ignore @CreatedBy
	private Long createdBy;
	
    @Ignore @CreatedDate
	private LocalDateTime createdDateTime;
	
    @Ignore @LastModifiedBy
	private Long lastModifiedBy;
	
    @Setter @LastModifiedDate
	private LocalDateTime lastModifiedDate;
	
    //@Version
	@Setter private long version;
	
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	@JsonIgnore
	public <T extends AbstractEntityBehavior<M, ID>, M extends AbstractEntity<ID>> T newBehaviorInstance(Class<T> clazz) {
		try {
			T t = ConstructorUtils.invokeExactConstructor(clazz, this);
			return t;
		} 
		catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to instantiate class of type: " + clazz, ex);
		}
	}
	
}
