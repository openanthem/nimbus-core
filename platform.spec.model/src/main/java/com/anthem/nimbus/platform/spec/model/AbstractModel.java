/**
 * 
 */
package com.anthem.nimbus.platform.spec.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import com.anthem.nimbus.platform.spec.model.dsl.ConfigNature;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for all domain model.
 * 
 * @author Soham Chakravarti
 */
@Getter
public abstract class AbstractModel<ID extends Serializable> implements Model {

	private static final long serialVersionUID = 1L;

	
	
	public static abstract class IdLong extends AbstractModel<Long> {
		
		private static final long serialVersionUID = 1L;
		
		@Id @Getter @Setter 
		private Long id;
	}
	
	
	
    public static abstract class IdString extends AbstractModel<String> {
		
		private static final long serialVersionUID = 1L;

		@Id @Getter @Setter
		private String id;
	}
	
	
    public abstract ID getId();


	@ConfigNature.Ignore @CreatedBy
	private Long createdBy;
	
    @ConfigNature.Ignore @CreatedDate
	private LocalDateTime createdDateTime;
	
    @ConfigNature.Ignore @LastModifiedBy
	private Long lastModifiedBy;
	
    @ConfigNature.Ignore @LastModifiedDate
	private LocalDateTime lastModifiedDate;
	
    @Version
	private long version;
	
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	@JsonIgnore
	public <T extends AbstractModelBehavior<M, ID>, M extends AbstractModel<ID>> T newBehaviorInstance(Class<T> clazz) {
		try {
			T t = ConstructorUtils.invokeExactConstructor(clazz, this);
			return t;
		} 
		catch (Exception ex) {
			throw new PlatformRuntimeException("Failed to instantiate class of type: " + clazz, ex);
		}
	}
	
}
