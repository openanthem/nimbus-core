/**
 * 
 */
package com.antheminc.oss.nimbus.test.entity.sample.s1.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="s1v_main")
@Getter @Setter
@SuppressWarnings("serial")
public class S1V_VRMain extends IdString {

	@Path(linked=false)
	private List<S1V_LineItem> detachedItems;
}
