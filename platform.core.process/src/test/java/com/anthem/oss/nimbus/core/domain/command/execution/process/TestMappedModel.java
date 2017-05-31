/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.process;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
//@Domain(value="testmappedmodel") 
@MapsTo.Type(TestModel.class) 
@Getter @Setter
public class TestMappedModel {
	
	private String parameter1;
	private String parameter2;
	
	@MapsTo.Path(linked=false)
	private String parameter3;
}
