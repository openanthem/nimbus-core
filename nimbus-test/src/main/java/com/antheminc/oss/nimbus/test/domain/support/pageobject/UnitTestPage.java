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
package com.antheminc.oss.nimbus.test.domain.support.pageobject;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.client.MockRestServiceServer;

import com.antheminc.oss.nimbus.channel.web.WebActionController;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ParamUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * A testing support facade implementation that provides support for subclasses
 * to interact with the Framework by abstracting many of the common framework
 * invocations into common utility methods.
 * 
 * <p>
 * Subclassing {@code UnitTestPage} will allow for the simplification of test
 * cases by abstracting the the underlying framework integrations into the
 * subclass, so that the unit test class is able to focus on its primary
 * concern: testing. These subclasses can achieve this by containing customized
 * methods for interacting with specific framework components, so that the unit
 * test case must only interact the {@code UnitTestPage} subclass, instead of
 * interacting with the framework directly.
 * 
 * <p>
 * Typically, it is the aim of {@code UnitTestPage} to be associated with domain
 * entity fields that are decorated with &#64;{@code Page}. Being associated
 * with a field in this way means that the path arguments provided in this
 * instance's constructor is equivalent to the field name decorated with
 * &#64;{@code Page}.
 * 
 * @author Tony Lopez
 * @since 1.1
 */
@Getter
@Setter
public abstract class UnitTestPage {

	public static final JustLogit logger = new JustLogit();

	private final String clientApp;
	private final String clientId;
	private final String coreDomainAlias;
	private final String pageId;
	private final Long refId;
	private final String viewDomainAlias;

	protected final BeanResolverStrategy beanResolver;
	protected final WebActionController controller;

	protected MockRestServiceServer mockServer;

	protected final ObjectMapper objectMapper;

	/**
	 * <p>
	 * Base constructor for this class, intended to be used by all other
	 * constructors to initialize the required data in order to interact with a
	 * page from the framework perspective.
	 * 
	 * @param beanResolver
	 *            to be provided from the Spring context
	 * @param clientId
	 *            the path argument clientId in
	 *            {@code /clientId/clientApp/p/domain:refId/pageId/...}
	 * @param clientApp
	 *            the path argument clientApp in
	 *            {@code /clientId/clientApp/p/domain:refId/pageId/...}
	 * @param coreDomainAlias
	 *            the path argument domain in
	 *            {@code /clientId/clientApp/p/domain:refId/pageId/...}
	 * @param viewDomainAlias
	 *            the path argument domain in
	 *            {@code /clientId/clientApp/p/domain:refId/pageId/...}
	 * @param pageId
	 *            the path argument pageId in
	 *            {@code /clientId/clientApp/p/domain:refId/pageId/...}
	 * @param refId
	 *            the path argument refId in
	 *            {@code /clientId/clientApp/p/domain:refId/pageId/...}
	 */
	public UnitTestPage(BeanResolverStrategy beanResolver, String clientId, String clientApp, String coreDomainAlias,
			String viewDomainAlias, String pageId, Long refId) {
		this.beanResolver = beanResolver;
		this.clientId = clientId;
		this.clientApp = clientApp;
		this.viewDomainAlias = viewDomainAlias;
		this.coreDomainAlias = coreDomainAlias;
		this.pageId = pageId;
		this.refId = refId;

		this.controller = this.beanResolver.find(WebActionController.class);
		this.objectMapper = this.beanResolver.find(ObjectMapper.class);
	}

	/**
	 * <p>
	 * Assembles the base URI where the param represented by this instance is
	 * located. The base URI includes all parts of the framework URI up to the
	 * domain separator of this param ({@code /p}).
	 * <p>
	 * The form of the string returned is {@code clientId/clientApp/p/}, where
	 * {@code clientId} and {@code clientApp} are provided in the constructor
	 * for this object.
	 * 
	 * @return the base URI string for this param
	 */
	public String getBaseURI() {
		return new StringBuilder().append(this.clientId).append(Constants.SEPARATOR_URI.code).append(this.clientApp)
				.append(Constants.SEPARATOR_URI.code).append(Constants.MARKER_URI_PLATFORM.code).toString();
	}

	/**
	 * <p>
	 * Assembles the URI where the core param represented by this instance is
	 * located. The core root domain URI includes all parts of the framework URI
	 * up to the core domain alias of the associated param.
	 * <p>
	 * The form of the string returned is
	 * {@code clientId/clientApp/p/coreDomainAlias}, where
	 * {@code coreDomainAlias} is provided in the constructor for this object.
	 * 
	 * @return the base URI string for this param
	 * @see #getBaseURI()
	 */
	public String getCoreRootDomainURI() {
		return new StringBuilder().append(this.getBaseURI()).append(Constants.SEPARATOR_URI.code)
				.append(this.coreDomainAlias).toString();
	}

	/**
	 * <p>
	 * Builds and executes a framework request using {@link Action._get}, to the
	 * page param represented by this instance. Afterwards, the method attempts
	 * to retrieve and return the first param from the framework response (via
	 * calling {@link MultiOutput#getSingleResult()}).
	 * 
	 * @return the page param represented by this instance
	 */
	@SuppressWarnings("unchecked")
	public <T> Param<T> getParam() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(this.getViewRootDomainURI())
				.addRefId(this.refId).addAction(Action._get).getMock();

		return (Param<T>) this.extractResponse(request, null);
	}

	/**
	 * <p>
	 * Builds and executes a framework request using {@link Action._get}, to the
	 * page param represented by this instance. Afterwards, the method attempts
	 * to retrieve and return the first param from the framework response (via
	 * calling {@link MultiOutput#getSingleResult()}).
	 * <p>
	 * The method then invokes {@link Param#findParamByPath(String)} using
	 * {@code pathFromViewRoot} to identify the desired param and returns it.
	 * <p>
	 * If there is a need to invoke an {@link Action._get} on a particular
	 * param, rather than on the page param (e.g. to execute
	 * {@link com.antheminc.oss.nimbus.domain.defn.Execution.Config} decorating
	 * that param), consider creating a method in a subclass of this class that
	 * manually invokes that framework call.
	 * 
	 * @return the param identified from the context of this page param and
	 *         {@code pathFromViewRoot}
	 * @see #getParam()
	 */
	public <T> Param<T> getParam(String pathFromViewRoot) {
		return this.getParam().findParamByPath(pathFromViewRoot);
	}

	/**
	 * <p>
	 * Assembles the URI where the view param represented by this instance is
	 * located. The view root domain URI includes all parts of the framework URI
	 * up to the view domain alias of the associated param.
	 * <p>
	 * The form of the string returned is
	 * {@code clientId/clientApp/p/viewDomainAlias}, where
	 * {@code viewDomainAlias} is provided in the constructor for this object.
	 * 
	 * @return the base URI string for this param
	 * @see #getBaseURI()
	 */
	public String getViewRootDomainURI() {
		return new StringBuilder().append(this.getBaseURI()).append(Constants.SEPARATOR_URI.code)
				.append(this.viewDomainAlias).toString();
	}

	/**
	 * <p>
	 * Assembles a portion of the URI, including the view param and refId of the
	 * param represented by this instance.
	 * <p>
	 * The form of the string returned is {@code /viewDomainAlias:refId}, where
	 * {@code refId} is provided in the constructor for this object.
	 * 
	 * @return the view root domain alias with string for this param
	 */
	public String getViewRootWithRefId() {
		return new StringBuilder().append(Constants.SEPARATOR_URI.code).append(this.viewDomainAlias)
				.append(Constants.SEPARATOR_URI_VALUE.code).append(this.refId).toString();
	}

	/**
	 * <p>
	 * Invokes an {@link Action._get} framework request to the page param
	 * represented by this instance and returns the visible property of that
	 * param.
	 * 
	 * @return whether or not this page param is visible.
	 */
	public boolean isVisible() {
		return this.getParam().isVisible();
	}

	/**
	 * <p>
	 * Converts {@code payload} to its JSON string equivalent using this
	 * instance's {@code objectMapper}.
	 * 
	 * @throws RuntimeException
	 *             if unable to convert {@code payload} to JSON
	 * @param payload
	 *            the object to convert
	 * @return the JSON string equivalent of {@code payload}
	 */
	protected String convertToStringPayload(Object payload) {
		if (null == payload) {
			return null;
		}

		final String sPayload;
		if (payload.getClass().equals(String.class)) {
			sPayload = (String) payload;
		} else {
			try {
				sPayload = this.objectMapper.writeValueAsString(payload);
			} catch (JsonProcessingException e) {
				throw new RuntimeException("Failed to convert payload to string.", e);
			}
		}

		return sPayload;
	}

	/**
	 * <p>
	 * Convenience method with null-checking that may be used to invoke a
	 * framework HTTP post request by providing the required parameters. A
	 * {@code null} payload is sent with the request.
	 * <p>
	 * The following fields are allowed to be {@code null}:
	 * <ul>
	 * <li>{@code refId}</li>
	 * <li>{@code nestedPath}</li>
	 * <li>{@code action}</li>
	 * </ul>
	 * 
	 * @param uri
	 *            the base URI of the request
	 * @param refId
	 *            the unique framework id of the domain entity in the request
	 * @param nestedPath
	 *            the nested path to append to the request after {@code uri} and
	 *            {@code refId} are added
	 * @param action
	 *            the {@link Action} to invoke
	 * @return the response object as received from the framework
	 * @see #doRequest(String, Long, String, Action, Object)
	 * @see #convertToStringPayload(Object)
	 */
	protected Object doRequest(String uri, Long refId, String nestedPath, Action action) {
		return this.doRequest(uri, refId, nestedPath, action, null);
	}

	/**
	 * <p>
	 * Convenience method with null-checking that may be used to invoke a
	 * framework HTTP post request by providing the required parameters.
	 * <p>
	 * If provided, {@code payload} will be converted to it's JSON string
	 * equivalent using this instance's {@code objectMapper} instance.
	 * <p>
	 * The following fields are allowed to be {@code null}:
	 * <ul>
	 * <li>{@code refId}</li>
	 * <li>{@code nestedPath}</li>
	 * <li>{@code action}</li>
	 * <li>{@code payload}</li>
	 * </ul>
	 * 
	 * @param uri
	 *            the base URI of the request
	 * @param refId
	 *            the unique framework id of the domain entity in the request
	 * @param nestedPath
	 *            the nested path to append to the request after {@code uri} and
	 *            {@code refId} are added
	 * @param action
	 *            the {@link Action} to invoke
	 * @param payload
	 *            the object to send as payload in the HTTP post request
	 * @return the response object as received from the framework
	 * @see #convertToStringPayload(Object)
	 */
	protected Object doRequest(String uri, Long refId, String nestedPath, Action action, Object payload) {
		MockHttpRequestBuilder request = MockHttpRequestBuilder.withUri(uri);

		if (null != refId) {
			request.addRefId(refId);
		}

		if (null != nestedPath) {
			request.addNested(nestedPath);
		}

		if (null != action) {
			request.addAction(action);
		}

		String sPayload = convertToStringPayload(payload);

		return this.controller.handlePost(request.getMock(), sPayload);
	}

	/**
	 * <p>
	 * Convenience method that builds and executes a framework HTTP post request
	 * to the param identified by the combination of {@code uri}, {@code refId},
	 * and {@code nestedPath}. Once executed, this method will attempt to locate
	 * a param from each of the response's {@link MultiOutput}'s
	 * {@link Output#getValue()} values that has a URI path ending with
	 * {@code paramPathEndsWith}.
	 * <p>
	 * If {@code paramPathEndsWith} is {@code null}, this method will return the
	 * result of {@link MultiOutput#getSingleResult()}. If multiple params are
	 * found in this manner, only the first will be returned. If
	 * {@code paramPathEndsWith} is provided, but no params are identified, this
	 * method will throw a {@link RuntimeException}.
	 * <p>
	 * When provided, {@code payload} will be converted to it's JSON string
	 * equivalent (using this instance's {@code objectMapper} instance) and sent
	 * alongside the framework HTTP post request.
	 * <p>
	 * The following fields are allowed to be {@code null}:
	 * <ul>
	 * <li>{@code refId}</li>
	 * <li>{@code nestedPath}</li>
	 * <li>{@code action}</li>
	 * <li>{@code payload}</li>
	 * <li>{@code paramPathEndsWith}</li>
	 * </ul>
	 * <p>
	 * If more control is needed, consider building the framework request object
	 * manually and invoking {@link #extractResponse(MockHttpServletRequest)} or
	 * {@link #extractResponseByParamPath(Object, String)}.
	 * 
	 * @param uri
	 *            the base URI of the request
	 * @param refId
	 *            the unique framework id of the domain entity in the request
	 * @param nestedPath
	 *            the nested path to append to the request after {@code uri} and
	 *            {@code refId} are added
	 * @param action
	 *            the {@link Action} to invoke
	 * @param payload
	 *            the object to send as payload in the HTTP post request
	 * @param paramPathEndsWith
	 *            the ending path of the param to identify, from the set of
	 *            params received in the {@code response} request on
	 * @return the param identified by {@code paramPathEndsWith}
	 * @see #extractResponseByParamPath(Object, String)
	 */
	protected <T> Param<T> doRequestAndExtractParam(String uri, Long refId, String nestedPath, Action action,
			Object payload, String paramPathEndsWith) {
		Object response = this.doRequest(uri, refId, nestedPath, action, payload);
		return this.extractResponseByParamPath(response, paramPathEndsWith);
	}

	/**
	 * <p>
	 * Convenience method that builds and executes a framework request to the
	 * param identified by {@code path}, relative to the page param represented
	 * by this instance. The method will next attempt to return the first param
	 * retrieved from the response, via calling
	 * {@link MultiOutput#getSingleResult()}, and will return the leaf state of
	 * that param.
	 * <p>
	 * If more control is needed, consider building the framework request object
	 * manually and invoking {@link #extractResponse(MockHttpServletRequest)}.
	 * 
	 * @param path
	 *            the path, relative to the page param represented by this
	 *            instance, to the param to execute a request on
	 * @return the param identified by {@code path}
	 * @see #extractResponseByParamPath(Object, String)
	 */
	protected <T> T doRequestAndGetLeafState(String path) {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(this.getViewRootDomainURI())
				.addRefId(this.getRefId()).addNested(path).addAction(Action._get).getMock();

		Param<T> response = this.extractResponse(request);
		return response.getLeafState();
	}

	/**
	 * <p>
	 * Given a framework request data, {@code request}, this method executes
	 * that {@code request} and deciphers the response. The method will next
	 * attempt to return the first param retrieved from the response, via
	 * calling {@link MultiOutput#getSingleResult()}.
	 * 
	 * @param request
	 *            the framework request object to execute
	 * @return the param identified within the response ending with
	 *         {@code endsWith}
	 * @see #extractResponseByParamPath(Object, String)
	 */
	protected <T> Param<T> extractResponse(MockHttpServletRequest request) {
		return this.extractResponse(request, null, null);
	}

	/**
	 * <p>
	 * Given a framework request data, {@code request} and payload
	 * {@code json}(optional), this method executes that {@code request} and
	 * deciphers the response. The method will next attempt to return the first
	 * param retrieved from the response, via calling
	 * {@link MultiOutput#getSingleResult()}.
	 * 
	 * @param request
	 *            the framework request object to execute
	 * @param json
	 *            a payload to send alongside the framework request (if
	 *            applicable)
	 * @return the param identified within the response ending with
	 *         {@code endsWith}
	 * @see #extractResponseByParamPath(Object, String)
	 */
	protected <T> Param<T> extractResponse(MockHttpServletRequest request, String json) {
		return this.extractResponse(request, json, null);
	}

	/**
	 * <p>
	 * Given a framework request data, {@code request} and payload
	 * {@code json}(optional), this method executes that {@code request} and
	 * deciphers the response. The method will next attempt to locate a param
	 * from each of the returned {@link MultiOutput}'s values that end with
	 * {@code endsWith} in the param path (identified by
	 * {@link Param#getPath()}).
	 * <p>
	 * If multiple params are found, only the first will be returned.
	 *
	 * @throws RuntimeException
	 *             if {@code endsWith} is provided and not contained by any of
	 *             the outputs deciphered in the response
	 * @param request
	 *            the framework request object to execute
	 * @param json
	 *            a payload to send alongside the framework request (if
	 *            applicable)
	 * @param endsWith
	 *            the ending path of the param to identify, from the set of
	 *            params received in the response
	 * @return the param identified within the response ending with
	 *         {@code endsWith}
	 * @see #extractResponseByParamPath(Object, String)
	 */
	protected <T> Param<T> extractResponse(MockHttpServletRequest request, String json, String endsWith) {
		return this.extractResponseByParamPath(this.controller.handlePost(request, json), endsWith);
	}

	/**
	 * @see ParamUtils#extractResponseByParamPath(Object, String)
	 */
	protected <T> Param<T> extractResponseByParamPath(Object response, String paramPathEndsWith) {
		return ParamUtils.extractResponseByParamPath(response, paramPathEndsWith);
	}
}
