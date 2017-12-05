/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.domain.model.core;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
import com.antheminc.oss.nimbus.core.entity.audit.AuditEntry;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="sample_core_audit_history", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
@Getter @Setter
public class SampleCoreAuditEntry extends AuditEntry {

	private static final long serialVersionUID = 1L;

}
