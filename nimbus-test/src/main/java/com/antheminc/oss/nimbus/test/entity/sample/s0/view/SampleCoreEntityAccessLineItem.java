/**
 * 
 */
package com.antheminc.oss.nimbus.test.entity.sample.s0.view;

import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Link;
import com.antheminc.oss.nimbus.domain.defn.extension.AccessConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.AccessConditional.Permission;
import com.antheminc.oss.nimbus.test.entity.sample.s0.core.SampleCoreEntityAccess;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@MapsTo.Type(SampleCoreEntityAccess.class)
@Getter @Setter
public class SampleCoreEntityAccessLineItem {

	@Path @GridColumn
	@AccessConditional(whenAuthorities="?[#this == 'case_management'].empty", p=Permission.READ)
	private String attr_String;
	
	@Link(imgSrc = "edit.png")
	@AccessConditional(whenAuthorities="?[#this == 'case_management'].empty", p=Permission.HIDDEN)
	private String viewLink;
}
