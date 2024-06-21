/**
 *  Copyright 2016-2019 the original author or authors.
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

import java.util.List;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.defn.extension.NotExists;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.ValidationError;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Tony Lopez
 *
 */
@EnableLoggingInterceptor
@Getter(AccessLevel.PROTECTED)
public class NotExistsStateEventHandler extends AbstractConditionalStateEventHandler<NotExists> {

	private final CommandExecutorGateway commandGateway;
	
	public NotExistsStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		this.commandGateway = beanResolver.get(CommandExecutorGateway.class);
	}

	@Override
	protected void handleInternal(Param<?> onChangeParam, NotExists configuredAnnotation) {
		MultiOutput response = getCommandGateway().execute(CommandBuilder.withUri(configuredAnnotation.url()).getCommand(), null);
		List<?> searchResults = (List<?>) response.getSingleResult();
		if (!searchResults.isEmpty()) {
			ValidationError violation = new ValidationError.Param();
			onChangeParam.getValidationResult().addValidationError(violation);
		}
	}
}
