/**
 * 
 */
package com.anthem.nimbus.platform.core.bpm;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Cache;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value="bpmtestmodel", includeListeners={ListenerType.persistence, ListenerType.update}) 
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@Getter @Setter @ToString(callSuper=true)
public class BPMTestModel extends IdString {
	private static final long serialVersionUID = 1L;
	private String sourceParameter;
	private String targetParameter;

}
