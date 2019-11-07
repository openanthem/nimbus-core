/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.view;

import static com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.JPAConstants.SEQ_GEN_NAME;
import static com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.JPAConstants.SEQ_GEN_PARAM_K_NM;
import static com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.JPAConstants.SEQ_GEN_STRATEGY;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.extension.AuditView;
import com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.SampleJPARootCoreEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value = "auditview_a1", includeListeners = { ListenerType.none })
@MapsTo.Type(SampleJPARootCoreEntity.class)
@AuditView
@Repo(value=Repo.Database.rep_rdbms, cache=Cache.rep_none)
@Entity
@Table(name="AUDITVIEW_A1")
@Getter @Setter @ToString
public class VRAudit_A1 {

	@Id
	@GeneratedValue(generator=SEQ_GEN_NAME)
	@GenericGenerator(name=SEQ_GEN_NAME, strategy=SEQ_GEN_STRATEGY, parameters=@Parameter(name=SEQ_GEN_PARAM_K_NM, value="SEQ_AUDITV_A1"))
	private Long id;
	
	@Column
	@Path("/id")
	private Long entityId;
	
	
	@Column
	@Path
	private String a1;
	
	@Column
	@Path
	private String a2;
	
    @Column
	@Path
	private String lastModifiedBy;
	
    @Column
	@Path
	private ZonedDateTime lastModifiedDate;
}
