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
package com.antheminc.oss.nimbus.domain.model.state;

import java.io.Serializable;
import java.util.List;

import com.antheminc.oss.nimbus.domain.model.config.ParamConfigType;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListModel;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor @ToString(of="config")
public class StateType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore private final ParamConfigType config;
	
	@JsonIgnore
	public String getName() {
		return config.getName();
	}
	
	@JsonIgnore
	public boolean isTransient() {
		return false;
	}
	
	@JsonIgnore
	public boolean isCollection() {
		return false;
	}
	
	@JsonIgnore
	public boolean isNested() {
		return config.isNested();
	}
	
	@JsonIgnore
	public boolean isArray() {
		return config.isArray();
	}
	
	public <P> Nested<P> findIfNested() {
		return null;
	}
	
	public <P> NestedCollection<P> findIfCollection() {
		return null;
	}
	
	public <P> MappedTransient<P> findIfTransient() {
		return null;
	}
	
	
	@Getter @ToString(callSuper=true, of="model")
	public static class Nested<P> extends StateType implements Serializable {

		private static final long serialVersionUID = 1L;

		private EntityState.Model<P> model;
		
		public Nested(ParamConfigType.Nested<P> config, Model<P> model) {
			super(config);
			this.model = model;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Nested<P> findIfNested() {
			return this;
		}
	}
	
	public static class NestedCollection<P> extends Nested<List<P>> {
		private static final long serialVersionUID = 1L;

		public NestedCollection(ParamConfigType.NestedCollection<P> config, ListModel<P> model) {
			super(config, model);
		}
		
		@JsonIgnore
		@Override
		public boolean isCollection() {
			return true;
		}
		
		@JsonIgnore
		public boolean isLeafElements() {
			return getConfig().findIfCollection().isLeafElements();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public NestedCollection<P> findIfCollection() {
			return this;
		}
		
		@Override
		public ListModel<P> getModel() {
			return (EntityState.ListModel<P>)super.getModel();
		}
	}
	
	public static class MappedTransient<P> extends Nested<P> implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public MappedTransient(ParamConfigType.Nested<P> config) {
			super(config, null);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public MappedTransient<P> findIfTransient() {
			return this;
		}
		
		private void setModel(EntityState.Model<P> model) {
			super.model = model;
		}
		
		public void assign(EntityState.Model<P> mappedModel) {
			setModel(mappedModel);
		}
		
		public void unassign() {
			setModel(null);
		}
	}
	
}
