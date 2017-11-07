/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

import com.anthem.oss.nimbus.core.domain.definition.ConfigNature.Ignore;
import com.anthem.oss.nimbus.core.entity.access.AccessEntity;
import com.anthem.oss.nimbus.core.entity.access.Role;
import com.anthem.oss.nimbus.core.entity.person.Address;
import com.anthem.oss.nimbus.core.entity.person.Name;
import com.anthem.oss.nimbus.core.entity.person.Person;
import com.anthem.oss.nimbus.core.entity.person.Phone;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public abstract class AbstractUser<R extends Role>
		extends Person<String, Address.IdString, Phone.IdString, Name.IdString> {
	

	private static final long serialVersionUID = 1L;
	
	@Id 
	private String id;

	
	private String loginId;
	
}