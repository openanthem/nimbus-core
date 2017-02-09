/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.um.model;

import java.time.LocalDate;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.ConfigNature.Ignore;
import com.anthem.nimbus.platform.spec.model.dsl.Domain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo.Database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
@Domain("patient") 
@Repo(Database.rep_mongodb)
@Execution.Input.Default @Execution.Output.Default  @Execution.Output(Action._delete)
@ToString
public class Patient extends AbstractModel.IdString {
    
	@Ignore
	private static final long serialVersionUID = 1L;

	private String subscriberId;

	private String firstName;

	private String lastName;

	private LocalDate dob;
	
}
