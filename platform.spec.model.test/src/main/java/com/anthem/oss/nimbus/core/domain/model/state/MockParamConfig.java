package com.anthem.oss.nimbus.core.domain.model.state;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.AssociatedEntity;
import com.anthem.oss.nimbus.core.domain.definition.Converters.ParamConverter;
import com.anthem.oss.nimbus.core.domain.definition.Execution.Config;
import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values;
import com.anthem.oss.nimbus.core.domain.model.config.AnnotationConfig;
import com.anthem.oss.nimbus.core.domain.model.config.EventHandlerConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.anthem.oss.nimbus.core.domain.model.config.RulesConfig;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
public class MockParamConfig implements ParamConfig<Object> {

	private Values values;
	private String code = "";
	
	@Override
	public String getConfigId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<Object> getReferredClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventHandlerConfig getEventHandlerConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <K> ParamConfig<K> findParamByPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <K> ParamConfig<K> findParamByPath(String[] pathArr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RulesConfig getRulesConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFound(String by) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getBeanName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParamType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public com.anthem.oss.nimbus.core.domain.model.config.ParamConfig.Desc getDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Config> getExecutionConfigs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AnnotationConfig> getValidations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AnnotationConfig> getUiNatures() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnnotationConfig getUiStyles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AnnotationConfig> getRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ParamConverter> getConverters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AssociatedEntity> getAssociatedEntities() {
		// TODO Auto-generated method stub
		return null;
	}

}
