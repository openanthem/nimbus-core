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

import com.antheminc.oss.nimbus.domain.defn.extension.ParamContext;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

/**
 * Default StateEventHandler for fields decorated with <tt>ParamContext</tt> that sets
 * param context values as defined within the <tt>ParamContext</tt> annotation attributes.
 *  
 * @author Tony Lopez
 * @see com.antheminc.oss.nimbus.domain.defn.extension.ParamContext
 */
@EnableLoggingInterceptor
public class ParamContextStateEventHandler implements OnStateLoadHandler<ParamContext> {

	@Override
	public void onStateLoad(ParamContext configuredAnnotation, Param<?> param) {
		param.setVisible(configuredAnnotation.visible());
		param.setEnabled(configuredAnnotation.enabled());
	}
}
