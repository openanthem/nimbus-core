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

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * @author Tony Lopez
 *
 */
@Getter
public class MongoDBModelRepositoryOptions {

	public static class Builder {

		private final List<MongoDBSearchOperation> searchOperations = new ArrayList<>();

		public Builder() {

		}

		public Builder(MongoDBModelRepositoryOptions options) {
			this.searchOperations.addAll(options.searchOperations);
		}

		public Builder addSearchOperation(MongoDBSearchOperation searchOperation) {
			this.searchOperations.add(searchOperation);
			return this;
		}

		public MongoDBModelRepositoryOptions build() {
			return new MongoDBModelRepositoryOptions(this);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(MongoDBModelRepositoryOptions options) {
		return new Builder(options);
	}

	private final List<MongoDBSearchOperation> searchOperations;

	public MongoDBModelRepositoryOptions(Builder builder) {
		this.searchOperations = builder.searchOperations;
	}
}