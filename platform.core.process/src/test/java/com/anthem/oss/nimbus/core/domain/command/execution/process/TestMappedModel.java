/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution.process;

import com.antheminc.oss.nimbus.core.domain.definition.MapsTo;

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
