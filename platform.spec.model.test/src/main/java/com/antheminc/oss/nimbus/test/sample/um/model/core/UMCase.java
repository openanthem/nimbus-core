package com.antheminc.oss.nimbus.test.sample.um.model.core;

import java.util.List;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Cache;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity;
import com.antheminc.oss.nimbus.core.entity.DateRange;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="core_umcase", includeListeners={ListenerType.persistence})
@Model(value="core_umcase", excludeListeners={ListenerType.websocket})
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@ToString
public class UMCase extends AbstractEntity.IdString {

	private static final long serialVersionUID = 1L;
    
	@Getter @Setter private String requestType;

	@Getter @Setter private String caseType;
	
	@Getter @Setter private DateRange serviceDate;
	
	@Getter @Setter private Patient patient;
	
	

	@Getter @Setter private String[] types;
	
	@Getter @Setter private ServiceLine oneServiceLine;
	
	@Getter @Setter private ServiceLine oneServiceLineConverted;
	
	@Getter @Setter private List<ServiceLine> serviceLines;
	
	@Getter @Setter private List<ServiceLine> serviceLinesConverted;
}
