/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.s0;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.Repo;

import lombok.Data;

/**
 * @author Soham.Chakravarti
 *
 */
@Domain(value="rootcore", includeListeners=ListenerType.persistence)
@Repo(Database.rep_rdbms)

@Entity
@Table(name="ROOT_CORE")

@Data
public class RootCore {

	@Id
	@Column(name="ID", nullable=false, unique=true)
	private Long id;
	
	@Column(name="ATTR1")
	private String attr1;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="CORE_ID", nullable=false, updatable=false)
	private List<RelatedCoreForwardById> relatedObjectsForwardOnly = new ArrayList<>();
	
//	@OneToMany(cascade=CascadeType.ALL, mappedBy="coreId")
//	private List<RelatedCoreBidirectionalById> relatedObjectsBidirectionalById = new ArrayList<>();
//	
//	@OneToMany(cascade=CascadeType.ALL, mappedBy="core")
//	private List<RelatedCoreBidirectionalByObject> relatedObjectsBidirectionalByObj = new ArrayList<>();

}
