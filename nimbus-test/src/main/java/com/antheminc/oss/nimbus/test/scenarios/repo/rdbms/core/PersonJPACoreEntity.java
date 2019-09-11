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
package com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core;

import static com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.JPAConstants.SEQ_GEN_NAME;
import static com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.JPAConstants.SEQ_GEN_PARAM_K_NM;
import static com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.JPAConstants.SEQ_GEN_STRATEGY;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Tony Lopez
 *
 */
@Domain(value = "person", includeListeners = { ListenerType.persistence })
@Repo(Database.rep_rdbms)
@Entity
@Table(name = "PERSON")
@Data
@EqualsAndHashCode(callSuper=false)
public class PersonJPACoreEntity extends IdLong {

	private static final long serialVersionUID = 1L;

	public PersonJPACoreEntity() {
		
	}
	
	public PersonJPACoreEntity(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	@Id
	@GeneratedValue(generator = SEQ_GEN_NAME)
	@GenericGenerator(name = SEQ_GEN_NAME, strategy = SEQ_GEN_STRATEGY, parameters = @Parameter(name = SEQ_GEN_PARAM_K_NM, value = "SEQ_SAMPLE"))
	@Override
	public Long getId() {
		return super.getId();
	}

	@Column
	private String firstName;

	@Column
	private String lastName;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private AddressJPACoreEntity address;
}
