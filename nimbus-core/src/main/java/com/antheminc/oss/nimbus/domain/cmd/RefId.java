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
package com.antheminc.oss.nimbus.domain.cmd;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Setter
public abstract class RefId<T> {
	
	public interface SyntheticKeyValueId {
		RefId<Long> getSynthetic();
		
		RefId<Map<String, String>> getKeyValue();
		void setKeyValue(RefId<Map<String, String>> kv);
	}
	
	public abstract Long getId();
	
	public abstract T getValue();
	
	public boolean isKeyValue() {
		return false;
	}
	
	public RefId<Map<String, String>> findIfKeyValue() {
		return null;
	}
	
	public boolean isComposite() {
		return false;
	}
	
	public RefId<SyntheticKeyValueId> findIfComposite() {
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj)
			return true;
		
		if(obj==null || !RefId.class.isInstance(obj))
			return false;
		
		RefId<?> other = RefId.class.cast(obj);
		if(this.getId().equals(other.getId()))
			return true;
		
		if(this.isComposite() && equals(other, this.findIfComposite())) {
			return true;
		}
		
		if(other.isComposite() && equals(this, other.findIfComposite())) {
			return true;
		}
		
		if(this.isComposite() && other.isComposite()) {
			Long thisCompId = Optional.ofNullable(this.findIfComposite().getValue()).map(SyntheticKeyValueId::getKeyValue).map(RefId::getId).orElse(null);
			Long otherCompId = Optional.ofNullable(other.findIfComposite().getValue()).map(SyntheticKeyValueId::getKeyValue).map(RefId::getId).orElse(null);
			
			if(thisCompId!=null && otherCompId!=null && thisCompId.equals(otherCompId))
				return true;
		}
		
		return false;
	}
	
	private static boolean equals(RefId<?> a, RefId<SyntheticKeyValueId> b) {
		return Optional.ofNullable(b.getValue())
				.map(SyntheticKeyValueId::getKeyValue)
				.map(RefId::getId)
				.filter(kvId->kvId.equals(a.getId()))
				.map(id->true)
				.orElse(false);
	}
	
	@Override
	public String toString() {
		return String.valueOf(getId());
	}
	

	public static Long nullSafeGetId(RefId<?> refId) {
		return Optional.ofNullable(refId).map(RefId::getId).orElse(null);
	}
	
	public static <X extends RuntimeException> Long nullSafeGetId(RefId<?> refId, Supplier<X> exceptionSupplier) {
		return Optional.ofNullable(refId).map(RefId::getId).orElseThrow(exceptionSupplier);
	}
	
	public static RefId<Long> with(Long id) {
		return id==null ? null : new RefIdLong(id);
	} 
	
	public static RefId<Map<String, String>> with(Map<String, String> map) {
		return map==null || map.isEmpty() ? null : new RefIdKeyValue(map);
	}
	
	public static RefId<String> with(String id) {
		if(StringUtils.trimToNull(id)==null || StringUtils.equalsIgnoreCase("null", id))
			return null;
		
		try {
			Long longId = Long.valueOf(id);
			return new RefIdStringIsLong(id, longId);
			
		} catch (Exception ex) {
			return new RefIdString(id);
		}
	}
	
	public static RefId<SyntheticKeyValueId> with() {
		RefId<Long> refIdSynthetic = with(Math.round(Math.random()));
		return with(refIdSynthetic);
	}
	
	public static RefId<SyntheticKeyValueId> with(RefId<Long> refIdSynthetic) {
		return refIdSynthetic==null ? null : new RefIdComposite(refIdSynthetic);
	}
	
	
	@Getter @RequiredArgsConstructor
	private static class RefIdLong extends RefId<Long> {
		private final Long value;
		
		@Override
		public Long getId() {
			return value;
		}
	}

	@Getter @RequiredArgsConstructor
	private static class RefIdString extends RefId<String> {
		private final String value;
		
		@Override
		public Long getId() {
			return (long)value.hashCode();
		}
	}
	
	private static class RefIdStringIsLong extends RefIdString {
		private final Long id;
		
		public RefIdStringIsLong(String value, Long id) {
			super(value);
			this.id = id;
		}
		
		@Override
		public Long getId() {
			return id;
		}
	}
	
	@Getter 
	private static class RefIdKeyValue extends RefId<Map<String, String>> {
		private final Map<String, String> value;
		private final Long id;
		
		public RefIdKeyValue(Map<String, String> value) {
			this.value = value;
			
			/* Eager generation of Id so that Id value doesn't change with future changes to key value, making the Id tied to the instance.
			 * Ideally, an entity with multiple PK (identity fields) should not undergo change once persisted. 
			 */
			this.id = generateId();
		}
		
		public Long generateId() {
			TreeMap<String, String> sortedMap = new TreeMap<>(value);
			
			StringBuilder sb = new StringBuilder();
			sortedMap.values().stream()
				.forEach(sb::append);
			
			return (long)sb.toString().hashCode();
		}

		@Override
		public Map<String, String> getValue() {
			return Collections.unmodifiableMap(value);
		}

		@Override
		public boolean isKeyValue() {
			return true;
		}
		
		@Override
		public RefId<Map<String, String>> findIfKeyValue() {
			return this;
		}
		
		@Override
		public String toString() {
			StringBuilder uri = new StringBuilder();
			
			value.entrySet().stream()
				.forEach(e->{
					String k = e.getKey();
					String v = e.getValue();
					uri.append(k).append("=").append(v).append(";");
				});
			
			String finalUri = uri.toString();
			finalUri = finalUri.substring(0, finalUri.length()-1);
			return finalUri;
		}
	}

	@RequiredArgsConstructor
	private static class RefIdComposite extends RefId<SyntheticKeyValueId> {
		private final RefId<Long> refIdSynthetic;
		private RefId<Map<String, String>> refIdKV;
		
		@Override
		public boolean isComposite() {
			return true;
		}
		
		@Override
		public RefId<SyntheticKeyValueId> findIfComposite() {
			return this;
		}
		
		@Override
		public Long getId() {
			return refIdSynthetic.getId();
		}
	
		@Override
		public SyntheticKeyValueId getValue() {
			return new SyntheticKeyValueId() {
				@Override
				public RefId<Long> getSynthetic() {
					return refIdSynthetic;
				}
				@Override
				public RefId<Map<String, String>> getKeyValue() {
					return refIdKV;
				}
				@Override
				public void setKeyValue(RefId<Map<String, String>> kv) {
					refIdKV = kv;
				}
			};
		}
	}

}
