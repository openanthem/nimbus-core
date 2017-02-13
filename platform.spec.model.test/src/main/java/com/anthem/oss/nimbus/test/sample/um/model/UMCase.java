package com.anthem.oss.nimbus.test.sample.um.model;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.Domain;
import com.anthem.oss.nimbus.core.domain.Execution;
import com.anthem.oss.nimbus.core.domain.Model;
import com.anthem.oss.nimbus.core.domain.Repo;
import com.anthem.oss.nimbus.core.domain.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;
import com.anthem.oss.nimbus.core.entity.DateRange;

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
public class UMCase extends AbstractEntity.IdString {

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
