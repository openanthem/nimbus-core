/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Cache;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="ex_client", includeListeners={ListenerType.persistence})
@Repo(value=Database.rep_ws, cache=Cache.rep_device)
@Getter @Setter @ToString(callSuper=true)
public class ExClient extends AbstractEntity.IdString {

	private static final long serialVersionUID = 1L;
	
	private Client client;
}
