/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.client;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Cache;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="ext_client")
@Repo(value=Database.rep_ws, cache=Cache.rep_device)
@Getter @Setter @ToString(callSuper=true)
public class ExtClient {

	private Client client;
}
