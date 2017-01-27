package com.anthem.nimbus.platform.core.process.api;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.exec.AbstractProcessTaskExecutor;
import com.anthem.nimbus.platform.core.process.api.exec.DefaultActionExecutorSearch;
import com.anthem.nimbus.platform.spec.contract.process.HierarchyMatchProcessTaskExecutor;
import com.anthem.nimbus.platform.spec.contract.process.ProcessExecutorEvents;
import com.anthem.nimbus.platform.spec.model.AbstractModel;
import com.anthem.nimbus.platform.spec.model.command.CommandElement.Type;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamValue;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;
import com.anthem.nimbus.platform.utils.reference.data.StaticCodeValue;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Component
@ConfigurationProperties(prefix="static.codevalue")
@RefreshScope
public class ParamCodeValueProvider extends AbstractProcessTaskExecutor implements HierarchyMatchProcessTaskExecutor {
	
	private static final String DEFAULT_KEY_ATTRIBUTE = "id";
	private static final String KEY_VALUE_SEPERATOR = "&";
	
	@Autowired
	DefaultActionExecutorSearch searchExecutor;
	
	@Getter @Setter
	private Map<String,List<ParamValue>> values;


	@Override
	public String getUri() {
		return "*/*/*/p/*/*/_search/_execute";
	}
	
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		// Have to override this method in the default executors to avoid the recursive call of the same method indefinitely from AbstractProcessTaskExecutor
	}

	
	/**
	 * Search will be in the order:
	 *	1. static code values (in below order):
	 *		1.1 config server, if not found
	 *		1.2 DB
	 *	2. Model as code values (in below order) 
 	 *		2.1 config server, if not found
	 *		2.2 DB 
	 *
	 */
	@Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		R codeValues = null;
		if(StringUtils.equalsIgnoreCase(cmdMsg.getCommand().getElement(Type.DomainAlias).get().getAlias(),"staticCodeValue")) {
			codeValues = getStaticCodeValue(cmdMsg);
		}
		else{
			codeValues = getModelAsCodeValue(cmdMsg);
		}
		
		return codeValues;
	}
	
	
	@SuppressWarnings("unchecked")
	private <R> R getStaticCodeValue(CommandMessage cmdMsg) {	
		// 1.1 config server lookup
		if(MapUtils.isNotEmpty(values) && CollectionUtils.isNotEmpty(values.get(cmdMsg.getRawPayload()))){
			return (R)values.get(cmdMsg.getRawPayload());
		}
		
		// 1.2 DB lookup
		cmdMsg.setRawPayload("{\"paramCode\":\""+cmdMsg.getRawPayload()+"\"}");
		List<StaticCodeValue> modelList = searchExecutor.doExecute(cmdMsg);
		if(CollectionUtils.isEmpty(modelList))
			return null;
		
		if(CollectionUtils.size(modelList) > 1)
			throw new IllegalStateException("StaticCodeValue look up for a command message"+cmdMsg+" returned more than one records for paramCode");
		
		return (R)modelList.get(0).getParamValues();
	}
	
	@SuppressWarnings("unchecked")
	private <R> R getModelAsCodeValue(CommandMessage cmdMsg) {
		
		// 2.1 config server lookup
		String domainAlias = cmdMsg.getCommand().getRootDomainAlias();
		if(MapUtils.isNotEmpty(values) && CollectionUtils.isNotEmpty(values.get(domainAlias))) {
			return (R)values.get(domainAlias);
		}
		
		// 2.2 DB lookup
		String[] keyValuePayload = cmdMsg.getRawPayload().split(KEY_VALUE_SEPERATOR);
		cmdMsg.setRawPayload(null); // Clearing rawpayload as the payload for this command message is specific for key/value lookup and not supported by search executor
		List<AbstractModel<?>> modelList = searchExecutor.doExecute(cmdMsg);
		
		if(CollectionUtils.isNotEmpty(modelList)) {
			String nestedDomainAlias = StringUtils.stripStart(cmdMsg.getCommand().getAbsoluteDomainAlias(), "/");
			
			String[] nestedDomainModels = nestedDomainAlias.split("/");
			Object currentObject = modelList;
			PropertyDescriptor currentPd = null;
			if(nestedDomainModels.length > 1) {
				for(int i=1; i<nestedDomainModels.length; i++) {
					try {
						if(i == 1) {
							currentPd = BeanUtils.getPropertyDescriptor(modelList.get(0).getClass(), nestedDomainModels[i]);
							currentObject = currentPd.getReadMethod().invoke(modelList.get(0));
						}
						else{
							currentPd = BeanUtils.getPropertyDescriptor(currentObject.getClass(), nestedDomainModels[i]);
							currentObject = currentPd.getReadMethod().invoke(currentObject);
						}
					}
					catch(Exception ex) {
						throw new PlatformRuntimeException("Failed to execute read on property: "+currentPd, ex);
					}
				}
			}
			
			Collection<AbstractModel<?>> coll = null;
			if(currentObject instanceof Collection<?>) {
				coll = (Collection<AbstractModel<?>>) currentObject;
			}
			else{
				coll = new ArrayList<>();
				coll.add((AbstractModel<?>)currentObject);
			}
			if (CollectionUtils.isEmpty(coll)) {
				List<ParamValue> paramValues = new ArrayList<>();
				return (R)paramValues;
			}
			Class<? extends AbstractModel> codeValueClass = coll.iterator().next().getClass();
			
			
			PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(codeValueClass, DEFAULT_KEY_ATTRIBUTE);
			PropertyDescriptor pd1 = BeanUtils.getPropertyDescriptor(codeValueClass, keyValuePayload.length > 0?keyValuePayload[0]:DEFAULT_KEY_ATTRIBUTE);
			PropertyDescriptor pd2 = BeanUtils.getPropertyDescriptor(codeValueClass, keyValuePayload.length > 1?keyValuePayload[1]:keyValuePayload[0]);
			
			try {
				List<ParamValue> paramValues = new ArrayList<>();
				for(Object model: coll) {
					paramValues.add(new ParamValue(pd.getReadMethod().invoke(model), (String)pd1.getReadMethod().invoke(model), (String)pd2.getReadMethod().invoke(model)));
				}
				return (R)paramValues;
			}
			catch(Exception ex) {
				throw new PlatformRuntimeException("Failed to execute read on property: "+pd, ex);
			}
		}
		return null;
	}

}
