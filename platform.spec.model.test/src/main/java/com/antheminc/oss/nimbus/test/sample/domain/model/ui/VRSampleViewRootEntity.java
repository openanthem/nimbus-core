/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.domain.model.ui;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.MapsTo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Page;
import com.antheminc.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="sample_view", includeListeners={ListenerType.websocket})
@MapsTo.Type(SampleCoreEntity.class)
@Repo(Database.rep_none)
@Getter @Setter
public class VRSampleViewRootEntity {

	@Page(route="sample_view_colors")
	private VPSampleViewPageGreen page_green;

	@Page(route="sample_view_colors")
	private VPSampleViewPageBlue page_blue;

	@Page(route="sample_view_colors")
	private VPSampleViewPageRed page_red;

}
