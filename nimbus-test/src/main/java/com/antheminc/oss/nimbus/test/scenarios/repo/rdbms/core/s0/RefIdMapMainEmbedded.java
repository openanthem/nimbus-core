/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.s0;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Domain(value="refidmap_main_embedded_core", includeListeners=ListenerType.persistence)
@Repo(Database.rep_rdbms)

@Entity
@Table(name="REFID_MAIN_EMBEDDED_CORE")

@Data @EqualsAndHashCode(exclude="id")
public class RefIdMapMainEmbedded {

	@RefId
	private Long id;
	
	@EmbeddedId
	private RefIdMapMainEmbeddedPK pk;
	
	private String attr1;
}
