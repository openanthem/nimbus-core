/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Cache;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain("vstaticCodeValue")
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@Getter @Setter @RequiredArgsConstructor
public class VStaticCodeValue extends IdString {

	private static final long serialVersionUID = 1L;

	private final String paramCode;

}
