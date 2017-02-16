/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.process;

import java.io.Serializable;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain("processFlow")
@Repo(Database.rep_mongodb)
@Getter @Setter
public class ProcessFlow extends AbstractEntity.IdString implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//TODO should this be the "id" or separate attribute
	private String processExecutionId;
	
	private PageNavigation pageNavigation;
}
