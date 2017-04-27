package com.anthem.oss.nimbus.core.config;

import org.springframework.data.mongodb.core.mapping.Document;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.ConfigNature.Ignore;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep Mantha
 *
 */
@Getter @Setter
@Domain("testdsl")
@Repo(Database.rep_mongodb)
@Execution.Input.Default
@Execution.Output.Default
@Document
@ToString
public class TestDSL extends IdString{
	@Ignore
	private static final long serialVersionUID = 1L;
	
    private String name;
    private Integer age;
}
