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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public abstract class AbstractUser<R extends Role<E, T>, E extends Role.Entry<T>, T extends AccessEntity>
		extends Person<String, Address.IdString, Phone.IdString, Name.IdString> {
	

	private static final long serialVersionUID = 1L;
	
	
	@Id @Getter @Setter(value=AccessLevel.PROTECTED) 
	private String id;

	
	private String loginId;

	@Ignore private Set<R> grantedRoles;
	
	
	@Override
	public Set<Address.IdString> getAddresses() {
		return super.getAddresses();
	}

	@Override
	public void setAddresses(Set<Address.IdString> addresses) {
		super.setAddresses(addresses);
	}

	@Override
	public Set<Phone.IdString> getContactPhones() {
		return super.getContactPhones();
	}

	@Override
	public void setContactPhones(Set<Phone.IdString> contactPhones) {
		super.setContactPhones(contactPhones);
	}
	

	@Override
	public Name.IdString getName() {
		return super.getName();
	}

	@Override
	public void setName(Name.IdString name) {
		super.setName(name);
	}
	
	
	/**
	 * 
	 * @param role
	 */
	public void addGrantedRoles(R role) {
		if(getGrantedRoles() == null)
			setGrantedRoles(new HashSet<>());
		
		getGrantedRoles().add(role);
	}
	
}