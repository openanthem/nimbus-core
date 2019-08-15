/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core;

import static com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.JPAConstants.SEQ_GEN_NAME;
import static com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.JPAConstants.SEQ_GEN_PARAM_K_NM;
import static com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.JPAConstants.SEQ_GEN_STRATEGY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="sample_jpa_core", includeListeners={ListenerType.persistence})
@Repo(Database.rep_rdbms)
@Entity
@Table(name="SAMPLE_JPA_CORE")
@Getter @Setter @ToString
public class SampleJPARootCoreEntity  extends IdLong {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator=SEQ_GEN_NAME)
	@GenericGenerator(name=SEQ_GEN_NAME, strategy=SEQ_GEN_STRATEGY, parameters=@Parameter(name=SEQ_GEN_PARAM_K_NM, value="SEQ_SAMPLE"))
	@Override
	public Long getId() {
		return super.getId();
	}
	
	@Column
	private String a1;
}
