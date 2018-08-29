/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.LabelState;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

/**
 * @author AC63348
 *
 */
public interface LabelConfigsOnLoadHandler extends OnStateLoadHandler<Label> {
	
	List<LabelState> buildLabelConfigs(Label label, Param<?> srcParam, Param<?> targetParam);

}
