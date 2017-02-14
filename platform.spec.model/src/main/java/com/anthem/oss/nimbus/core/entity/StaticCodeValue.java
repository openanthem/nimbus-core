/**
 * 
 */
package com.anthem.oss.nimbus.core.entity;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
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
@Repo(Repo.Database.rep_mongodb)
@Execution.Input.Default @Execution.Output.Default @Execution.Output(Action._delete)
@Getter @Setter @RequiredArgsConstructor
public class StaticCodeValue {

	private final String paramCode;

	private final List<ParamValue> paramValues;
}
