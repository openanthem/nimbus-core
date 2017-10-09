package com.anthem.oss.nimbus.test.sample.domain.model.ui;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Execution.Config;
import com.anthem.oss.nimbus.core.domain.definition.Executions.Configs;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Cache;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.task.AssignmentTask;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreAssociatedEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 * Flow: Process Core Entity Update (attr_String)
 * Acronym: CEU
 *
 */
@Domain(value="ceu_sampleassociatedentity")
@Repo(value=Database.rep_none, cache=Cache.rep_device)
@Getter @Setter
public class CEU_SampleAssociatedEntity {

	private String entityId;
    
	@Path(linked=false)
	private List<SampleCoreAssociatedEntity> allAssociatedEntities;

	@Configs({
		@Config(url="/entityId/_update"),
		@Config(url="/allAssociatedEntities/_process?fn=_set&url=/p/sample_coreassociatedentity/_search?fn=query&where=sample_coreassociatedentity.entityId.eq('<!/entityId!>')"),
		@Config(url="/p/sample_coreassociatedentity:<!col/id!>/status/_update?rawPayload=\"Cancelled\"", col="<!/allAssociatedEntities!>")
	})
    public String action_updateStatus;
   
}