package com.antheminc.oss.nimbus.core.entity;

import org.springframework.data.annotation.Id;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity to generate and track global unique id for MongoDB
 * TODO - currently using long for the id type, need to be BigInteger or similar
 * 
 * @author AC67870
 *
 */
@Domain(value="sequence")
@Repo(Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true)
public class DBSequence {

	@Id
	private String id;

	private long seq;

}
