package com.antheminc.oss.nimbus.entity.ui;

import com.antheminc.oss.nimbus.domain.defn.Model;

import lombok.Getter;
import lombok.Setter;

@Model @Getter @Setter
public class GridState {
	private String[] filterState;
	private int firstRecordNum;
	private String sortColumn;
}
