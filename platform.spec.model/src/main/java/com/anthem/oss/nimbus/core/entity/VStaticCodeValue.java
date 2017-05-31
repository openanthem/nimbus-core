/**
 * 
 */
package com.anthem.oss.nimbus.core.entity;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain("vstaticCodeValue")
@Repo
@Getter @Setter @RequiredArgsConstructor
public class VStaticCodeValue extends IdString {

	private static final long serialVersionUID = 1L;

	private final String paramCode;

}
