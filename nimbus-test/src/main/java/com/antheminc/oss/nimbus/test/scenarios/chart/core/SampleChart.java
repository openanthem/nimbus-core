/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.chart.core;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="samplechart", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
@Getter @Setter @RequiredArgsConstructor
public class SampleChart extends IdString{

	private static final long serialVersionUID = 1L;

	private final Action action;
	
	private final String domainRoot;
}
