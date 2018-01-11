package com.antheminc.oss.nimbus.test.sample.domain.model.ui;

import java.util.List;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Execution.Config;
import com.antheminc.oss.nimbus.core.domain.definition.Executions.Configs;
import com.antheminc.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Cache;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
//AC12974@bitbucket.anthem.com/scm/nim/anthm-internal-oss-backend.git
import com.antheminc.oss.nimbus.test.sample.domain.model.core.SampleCoreAssociatedEntity;

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