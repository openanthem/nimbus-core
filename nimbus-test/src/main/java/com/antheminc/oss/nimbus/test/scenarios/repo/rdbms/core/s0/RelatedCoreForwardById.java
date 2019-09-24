/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.s0;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.RefId;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Soham.Chakravarti
 *
 */
@Model
@Entity
@Table(name="RELATED_CORE_FORWARD_BY_ID")

@Data @EqualsAndHashCode(exclude="id")
public class RelatedCoreForwardById implements RelatedCore {

	@Id @RefId @GeneratedValue
	@Column(name="ID", nullable=false, unique=true, updatable=false)
	private Long id;
	
//	@Column(name="CORE_ID", nullable=false, updatable=false)
//	private Long coreId;
	
	@Column(name="REL_ATTR1")
	private String relatedAttr1;
}
