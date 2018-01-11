/**
 * 
 */
package com.antheminc.nimbus.platform.core.bpm;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.MapsTo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Cache;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.ViewRoot;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value = "ctvsubscriberviewmodel", includeListeners={ListenerType.websocket}, lifecycle="ctvsubscriberviewmodel")
@MapsTo.Type(CTVSubscriberTestModel.class)
@Repo(value=Database.rep_none, cache=Cache.rep_device)
@ViewRoot(layout = "caseoverviewlayout")
@Getter @Setter
public class VRCTVSubscriberTestModel {
	private String viewParameter;
	private String viewResultParameter;
}
