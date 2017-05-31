/**
 * 
 */
package com.anthem.oss.nimbus.core.entity;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;

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
public class StaticCodeValue {

	@Id
	private final String paramCode;

	private final List<ParamValue> paramValues;
}
