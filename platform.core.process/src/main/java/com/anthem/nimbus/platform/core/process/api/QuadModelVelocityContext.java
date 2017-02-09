package com.anthem.nimbus.platform.core.process.api;

import org.apache.velocity.context.Context;

import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.Model;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;

import lombok.RequiredArgsConstructor;

/**
 * @author Rakesh Patel
 *
 */
@RequiredArgsConstructor
public class QuadModelVelocityContext implements Context {

    final QuadModel<?,?> quadModel;

    @Override
    public Object put(String s, Object o) {
        return null;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Object get(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/").append(s.replaceAll("-","/"));
        Model viewSAC = quadModel.getView();
        return viewSAC.findParamByPath(stringBuilder.toString()).getState();
    }

    @Override
    public boolean containsKey(Object o) {
        return false;
    }

    @Override
    public Object[] getKeys() {
       throw new UnsupportedOperationException("getKeys Not supported by QuadModelVelocityContext");
    }

    @Override
    public Object remove(Object o) {
        return null;
    }
}
