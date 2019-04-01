package com.antheminc.oss.nimbus.domain.model.config.extension;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ToolTip;
import com.antheminc.oss.nimbus.domain.model.config.AnnotationConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

public class ToolTipStateEventHandler extends AbstractConfigEventHandler implements OnStateLoadHandler<ToolTip>
{

	@Override
	public void onStateLoad(ToolTip configuredAnnotation, Param<?> param) {
		
		List<AnnotationConfig> annotationList = param.getConfig().getUiNatures().stream().filter(uiNature -> uiNature.getAnnotation().equals(configuredAnnotation)).collect(Collectors.toList());
		
		if(annotationList != null && annotationList.size() != 1){
			throw new InvalidConfigException("Expected to find only one config annotation for tooltip");
		}
		
		annotationList.get(0).getAttributes().put("value", resolvePath(StringUtils.trimToNull(configuredAnnotation.value()), param));
	}

}