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
package com.antheminc.oss.nimbus.domain.model.config.builder.internal;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Execution.Let;
import com.antheminc.oss.nimbus.domain.defn.builder.internal.ExecutionConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.config.builder.ExecutionConfigProvider;

/**
 * @author Tony Lopez
 *
 */
public class ConfigVariableExecutionConfigProvider implements ExecutionConfigProvider<Let> {

	@Override
	public Config getMain(Let configAnnotation, ExecutionContext eCtx) {
		final CommandMessage cmdMsg = eCtx.getCommandMessage();
		// TODO review and clean up.
		final String valueString = "value=" + configAnnotation.value();
		String uri = "/p" + cmdMsg.getCommand().getAbsoluteDomainUri() + "/" + Action._process + "?fn=_setConfigVariable&name=" + configAnnotation.name() + "&" + valueString;
		return ExecutionConfigBuilder.buildExecConfig(uri);
	}

	@Override
	public Config getException(Let configAnnotation, ExecutionContext eCtx) {
		return null;
	}

}
