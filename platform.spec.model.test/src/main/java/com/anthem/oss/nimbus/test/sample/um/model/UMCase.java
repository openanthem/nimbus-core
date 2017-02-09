package com.anthem.oss.nimbus.test.sample.um.model;

import java.util.List;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
import com.anthem.nimbus.platform.spec.model.DateRange;
import com.anthem.nimbus.platform.spec.model.dsl.Domain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.anthem.nimbus.platform.spec.model.dsl.Model;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo.Database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="core_umcase") 
@Repo(Database.rep_mongodb)
@Execution.Input.Default_Exclude_Search
@Execution.Output.Default_Exclude_Search
@ToString
public class UMCase extends AbstractModel.IdString {

	private static final long serialVersionUID = 1L;
    
	@Model.Param.Values(Values.RequestType.class)
	@Getter @Setter private String requestType;

	@Model.Param.Values(Values.CaseType.class)
	@Getter @Setter private String caseType;
	
	@Getter @Setter private DateRange serviceDate;
	
	@Getter @Setter private Patient patient;
	
	

	@Getter @Setter private List<String> types;
	
	@Getter @Setter private ServiceLine oneServiceLine;
	
	@Getter @Setter private ServiceLine oneServiceLineConverted;
	
	@Getter @Setter private List<ServiceLine> serviceLines;
	
	@Getter @Setter private List<ServiceLine> serviceLinesConverted;
}
