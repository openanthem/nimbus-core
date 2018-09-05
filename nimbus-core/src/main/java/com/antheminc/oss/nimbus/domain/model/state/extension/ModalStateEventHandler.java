/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.model.state.extension;

import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Modal;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

/**
 * <p>Default StateEventHandler for <tt>ViewConfig.Modal</tt> that sets default
 * contextual values for enabled and visible to the value in the provided
 * <tt>&#64;ParamContext</tt> field <tt>context</tt>.</p>
 * 
 * @author Tony Lopez
 * @see com.antheminc.oss.nimbus.domain.defn.ViewConfig.Modal
 */
@EnableLoggingInterceptor
public class ModalStateEventHandler implements OnStateLoadHandler<Modal> {

	@Override
	public void onStateLoad(Modal configuredAnnotation, Param<?> param) {
		param.setVisible(configuredAnnotation.context().visible());
		param.setEnabled(configuredAnnotation.context().enabled());
	}
}