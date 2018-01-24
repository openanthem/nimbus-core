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
package com.anthem.oss.nimbus.core.web.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import feign.Headers;

/**
 * The Interface PlatformRestInterface.
 * @author Rohit Bajaj
 * 
 * Extend this interface to generate service specific feign rest client
 *
 * @param <T> the generic type
 */
@Headers("Accept: application/json")
public interface PlatformRestInterface <T> {
	
	/** The Constant URI_PATTERN_P. */
	public static final String URI_PATTERN_P = "/platform/**/p";
	
	/** The Constant URI_PATTERN_P_OPEN. */
	public static final String URI_PATTERN_P_OPEN = URI_PATTERN_P + "/**";

	/**
	 * Handle get.
	 *
	 * @param req the req
	 * @param a the a
	 * @return the t
	 */
	@RequestMapping(value=URI_PATTERN_P_OPEN, produces="application/json", method=RequestMethod.GET)
	public T handleGet(HttpServletRequest req, @RequestParam(required=false) String a) ;
	
	/**
	 * Handle delete.
	 *
	 * @param req the req
	 * @param v the v
	 * @return the t
	 */
	@RequestMapping(value=URI_PATTERN_P_OPEN, produces="application/json", method=RequestMethod.DELETE)
	public T handleDelete(HttpServletRequest req, @RequestParam String v) ;
	
	/**
	 * Handle post.
	 *
	 * @param req the req
	 * @param json the json
	 * @return the t
	 */
	@Headers("Content-Type: application/json")
	@RequestMapping(value=URI_PATTERN_P_OPEN, produces="application/json", method=RequestMethod.POST)
	public T handlePost(HttpServletRequest req, @RequestBody String json) ;
	
	/**
	 * Handle put.
	 *
	 * @param req the req
	 * @param v the v
	 * @param json the json
	 * @return the t
	 */
	@Headers("Content-Type: application/json")
	@RequestMapping(value=URI_PATTERN_P_OPEN, produces="application/json", method=RequestMethod.PUT)
	public T handlePut(HttpServletRequest req, @RequestParam String v, @RequestBody String json) ;
	
	/**
	 * Handle patch.
	 *
	 * @param req the req
	 * @param v the v
	 * @param json the json
	 * @return the t
	 */
	@RequestMapping(value=URI_PATTERN_P_OPEN, produces="application/json", method=RequestMethod.PATCH)
	public T handlePatch(HttpServletRequest req, @RequestParam String v, @RequestBody String json) ;

}
