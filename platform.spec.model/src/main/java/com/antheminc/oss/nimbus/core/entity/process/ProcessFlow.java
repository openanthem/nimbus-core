/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.process;

import java.io.Serializable;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value = "processFlow")
@Repo(value = Database.rep_mongodb)
@Getter @Setter
public class ProcessFlow extends AbstractEntity.IdString implements Serializable{
	private static final long serialVersionUID = 1L;
	private String processExecutionId;
}
