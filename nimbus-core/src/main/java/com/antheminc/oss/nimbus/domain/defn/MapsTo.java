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
package com.antheminc.oss.nimbus.domain.defn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;

/**
 * @author Soham Chakravarti
 *
 */
public class MapsTo {

	public enum Mode {
		UnMapped,
		MappedAttached,
		MappedDetached;
	}
	
	public enum Nature {
		Default,
		TransientColElem,
		TransientModel;
		
		public boolean isTransient() {
			return !Default.name().equals(name());
		}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@Model
	@Inherited
	public @interface Mapped {
		
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@Mapped
	public @interface Type {
		
		Class<?> value();
	}
	
	public enum LoadState {
		PROVIDED, // Manual loaded such as UI onload from grid or BPM or exec.config
		AUTO;	  // Framework loaded: from within mapsTo entity or by making cmdGateway call	
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.ANNOTATION_TYPE})
	public @interface DetachedState {
		// load state provided (via Grid call, BPM or exec-config) OR via F/w making cmdGateway call 
		LoadState loadState() default LoadState.PROVIDED;
		
		// once state is loaded, should it be cached or fetched each time
		Cache cacheState() default Cache.rep_none;
		
		// once state is loaded, should it be managed (edits reflected) by this Quad or treated as ReadOnly
		boolean manageState() default false;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface Path {
		
		String value() default "";
		
		boolean linked() default true;
		
		String colElemPath() default DEFAULT_COL_ELEM_PATH;
		
		Nature nature() default Nature.Default;
		
		DetachedState detachedState() default @DetachedState;
	}
	
	public static Mode getMode(Path path) {
		if(path==null) return Mode.UnMapped;
		
		return path.linked() ? Mode.MappedAttached : Mode.MappedDetached;
	}
	
	public static boolean hasCollectionPath(Path path) {
		return StringUtils.trimToNull(path.colElemPath())!=null;
	}
	
	public static final String DETACHED_SIMULATED_FIELD_NAME = "detachedParam";
	public static final String DEFAULT_COL_ELEM_PATH = "";
}
