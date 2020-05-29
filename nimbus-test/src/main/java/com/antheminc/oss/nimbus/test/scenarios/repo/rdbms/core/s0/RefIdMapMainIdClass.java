/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.s0;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.RefId;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Soham.Chakravarti
 *
 */
@Domain(value="refidmap_main_idclass_core", includeListeners=ListenerType.persistence)
@Repo(Database.rep_rdbms)

@Entity
@Table(name="REFID_MAIN_IDCLASS_CORE")
@IdClass(RefIdMapMainIdClassPK.class)

@Data @EqualsAndHashCode(exclude="id")
public class RefIdMapMainIdClass {

	@RefId
	private Long id;
	
	@Id
	private String key1;
	
	@Id
	private int key2;
	
	private String attr1;
}
