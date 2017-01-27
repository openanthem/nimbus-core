/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.repository;

import java.beans.PropertyDescriptor;
import java.util.Collection;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.contract.repository.ParamStateRepository;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default.param.state.rep_local")
public class DefaultParamStateRepositoryLocal implements ParamStateRepository {

	@SuppressWarnings("unchecked")
	@Override
	public <P> P _get(StateAndConfig.Param<P> param) {
		PropertyDescriptor pd = constructPropertyDescriptor(param);
		Object target = null;
		try {
			target = param.getParent().getState();
			if(target instanceof Collection<?>) { //TODO this is to fix the desrializing issue for collections e.g. object is not an instance of declaring class, classCastExecption etc...)
				return null;
			}
			return (target == null) ? null : (P)pd.getReadMethod().invoke(target);
		}
		catch (Exception ex) {
			throw new PlatformRuntimeException("Failed to execute write on property: "+pd +"and target: "+target, ex);
		}
	}
	
	@Override
	public <P> void _set(StateAndConfig.Param<P> param, P newState) {
		PropertyDescriptor pd = constructPropertyDescriptor(param);
		
		//ensure that the target parent model is instantiated
		Object target = param.getParent().getCreator().get();
		if(target==null) throw new PlatformRuntimeException("Target must not be null for setting in property: "+pd+" with value: "+ newState);
		
		try {
			pd.getWriteMethod().invoke(target, newState);
			
		} catch (Exception ex) {
			throw new PlatformRuntimeException("Failed to execute write on property: "+pd+" with value: "+newState, ex);
		}
	}
	
	public <P> PropertyDescriptor constructPropertyDescriptor(StateAndConfig.Param<P> param) {
		ModelConfig<?> mConfig = param.getParent().getConfig();
		ParamConfig<P> mpConfig= param.getConfig();
		
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(mConfig.getReferredClass(), mpConfig.getCode());
		return pd;
	}
}
