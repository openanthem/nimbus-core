package com.antheminc.oss.nimbus.core.domain.model.state;

import java.util.List;
import java.util.Map;

import com.antheminc.oss.nimbus.core.domain.definition.AssociatedEntity;
import com.antheminc.oss.nimbus.core.domain.definition.Converters.ParamConverter;
import com.antheminc.oss.nimbus.core.domain.definition.Execution.Config;
import com.antheminc.oss.nimbus.core.domain.definition.Model.Param.Values;
import com.antheminc.oss.nimbus.core.domain.model.config.AnnotationConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.EventHandlerConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamType;
import com.antheminc.oss.nimbus.core.domain.model.config.RulesConfig;

import lombok.Getter;
import lombok.Setter;
import test.com.antheminc.oss.nimbus.platform.utils.PathUtils;

/**
 * A mock ParamConfig implementation intended for testing purposes.
 * 
 * @author Tony Lopez (AF42192)
 *
 */
@Getter @Setter 
public class MockParamConfig implements ParamConfig<Object> {

	private List<AssociatedEntity> associatedEntities;
	private String beanName;
	private String code = "";
	private String configId = "";
	private List<ParamConverter> converters;
	private Desc desc;
	private EventHandlerConfig eventHandlerConfig;
	private List<Config> executionConfigs;
	private boolean leaf;
	private Map<String, ParamConfig<?>> paramConfigMap;
	private Class<Object> referredClass;
	private List<AnnotationConfig> rules;
	private RulesConfig rulesConfig;
	private ParamType type;
	private List<AnnotationConfig> uiNatures;
	private AnnotationConfig uiStyles;
	private List<AnnotationConfig> validations;
	private Values values;

	/*
	 * (non-Javadoc)
	 * @see com.antheminc.oss.nimbus.core.domain.model.config.EntityConfig#findParamByPath(java.lang.String)
	 */
	@Override
	public <K> ParamConfig<K> findParamByPath(String path) {
		return (ParamConfig<K>) this.paramConfigMap.get(path);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.antheminc.oss.nimbus.core.domain.model.config.EntityConfig#findParamByPath(java.lang.String[])
	 */
	@Override
	public <K> ParamConfig<K> findParamByPath(String[] pathArr) {
		return PathUtils.findFirstByPath(pathArr, k -> this.findParamByPath(k));
	}

	/*
	 * (non-Javadoc)
	 * @see com.antheminc.oss.nimbus.core.entity.Findable#isFound(java.lang.Object)
	 */
	@Override
	public boolean isFound(String by) {
		return this.paramConfigMap.containsValue(by);
	}

	/**
	 * Puts a <tt>ParamConfig</tt> into an internal map by <tt>path</tt> as key.
	 * 
	 * @param paramConfig the <tt>ParamConfig</tt> to store
	 * @param path the key to store <tt>paramConfig</tt> under
	 */
	public <K> void putParamConfig(ParamConfig<K> paramConfig, String path) {
		this.paramConfigMap.put(path, paramConfig);
	}

}
