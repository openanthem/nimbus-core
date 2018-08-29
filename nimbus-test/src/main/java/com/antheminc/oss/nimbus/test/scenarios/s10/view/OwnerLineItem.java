package com.antheminc.oss.nimbus.test.scenarios.s10.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.test.scenarios.s10.core.s10_core;


import lombok.Getter;
import lombok.Setter;


@MapsTo.Type(s10_core.class)
@Getter @Setter
public class OwnerLineItem {

  @Path(linked = false)
  private List<OwnerLineItem> owners;
}
