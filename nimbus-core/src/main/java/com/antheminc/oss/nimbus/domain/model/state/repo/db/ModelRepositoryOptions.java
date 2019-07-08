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
package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;

/**
 * <p>A simple encapsulation for common options across {@link ModelRepository}
 * implementations.
 * 
 * @author Tony Lopez
 *
 */
public interface ModelRepositoryOptions {

	/**
	 * <p> A strategy definition used to determine the read/write control in
	 * relation to tenancy.
	 * 
	 * @author Tony Lopez
	 *
	 */
	public enum TenancyStrategy {

		/**
		 * <p> Default strategy that implies no tenancy strategy should be used.
		 */
		NONE,

		/**
		 * <p> Record level based strategy that mandates tenant information is
		 * stored and read as part of the domain entity.
		 */
		RECORD;
	}

	/**
	 * <p> Get the strategy that should be used to determine how data should be
	 * handled in regards to tenancy.
	 * @return the {@link TenancyStrategy} object
	 */
	TenancyStrategy getTenancyStrategy();
}
