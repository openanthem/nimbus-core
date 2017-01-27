/**
 * 
 */
package com.anthem.nimbus.platform.utils.reference.data;

import java.util.List;

import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo.Option;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@CoreDomain("staticCodeValue")
@Repo(Option.rep_mongodb)
@Execution.Input.Default @Execution.Output.Default @Execution.Output(Action._delete)
@Getter @Setter @RequiredArgsConstructor
public class StaticCodeValue {

	private final String paramCode;
	private final List<ParamValue> paramValues;
	
}
