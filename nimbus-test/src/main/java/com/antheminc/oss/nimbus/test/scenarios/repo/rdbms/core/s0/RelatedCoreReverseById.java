/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.s0;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author Soham.Chakravarti
 *
 */
@Entity
@Table(name="RELATED_CORE_REVERSE_BY_ID")

@Data
public class RelatedCoreReverseById implements RelatedCore {

	@Id @Column(name="ID", nullable=false, unique=true, updatable=false)
	private Long id;
	
	@ManyToOne(targetEntity=RootCore.class)
	@JoinColumn(name="CORE_ID", nullable=false, updatable=false)
	private Long coreId;
	
	@Column(name="REL_ATTR1")
	private String relatedAttr1;
	
}
