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
package com.antheminc.oss.nimbus.test.domain.model.state.extension;

import java.util.HashSet;
import java.util.Set;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message.Context;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message.Type;
import com.antheminc.oss.nimbus.domain.model.state.extension.EvalExprWithCrudActions;
import com.antheminc.oss.nimbus.test.domain.defn.extension.TestEventType.OnStateChangeNoOverride;

/**
 * @author Rakesh Patel
 *
 */
public class TestOnStateChangeNoOverrideHandler extends EvalExprWithCrudActions<OnStateChangeNoOverride> {

	public TestOnStateChangeNoOverrideHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override	
	protected void handleInternal(Param<?> onChangeParam, OnStateChangeNoOverride configuredAnnotation) {
		Set<Message> messages = new HashSet<>();
		Message message = new Message(configuredAnnotation.toString(),configuredAnnotation.value(), Type.INFO, Context.INLINE, "");
		messages.add(message);
		onChangeParam.setMessages(messages);
	}
}
