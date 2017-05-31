/**
 * 
 */
package com.anthem.oss.nimbus.core.entity;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain("staticCodeValue")
@Repo
@Getter @Setter @RequiredArgsConstructor
public class StaticCodeValue extends IdString {

	private static final long serialVersionUID = 1L;

	private final String paramCode;

	private final List<ParamValue> paramValues;
}
