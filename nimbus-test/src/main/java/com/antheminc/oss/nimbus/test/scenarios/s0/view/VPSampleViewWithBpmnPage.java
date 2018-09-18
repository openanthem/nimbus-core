package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;

import lombok.Getter;
import lombok.Setter;


/**
 * 
 * 
 ** @author Sandeep Mantha
 */
@Getter @Setter @Model
public class VPSampleViewWithBpmnPage {
	
	@Tile(size = Tile.Size.Large)
	private VTSampleTile vtSampleTile;
	
	@Getter @Setter @Model
	public static class VTSampleTile {

	}

}