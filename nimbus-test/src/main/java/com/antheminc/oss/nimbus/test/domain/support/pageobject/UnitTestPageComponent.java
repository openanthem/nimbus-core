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
package com.antheminc.oss.nimbus.test.domain.support.pageobject;

import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

import lombok.Getter;
import lombok.Setter;

/**
 * /**
 * <p>
 * A testing support facade implementation that provides support for subclasses
 * to interact with the Framework by abstracting many of the common framework
 * invocations into common utility methods.
 * 
 * <p>
 * Subclassing {@code UnitTestPageComponent} will allow for the simplification
 * of test cases by abstracting the the underlying framework integrations into
 * the subclass, so that the unit test class is able to focus on its primary
 * concern: testing. These subclasses can achieve this by containing customized
 * methods for interacting with specific framework components, so that the unit
 * test case must only interact the {@code UnitTestPageComponent} subclass,
 * instead of interacting with the framework directly.
 * 
 * <p>
 * Typically, it is the aim of {@code UnitTestPageComponent} to be associated
 * with domain entity fields that are contained within the class definition for
 * another field decorated with &#64;{@code Page}. In other words, subclasses
 * are associated with child "components" of &#64;{@code Page} components. These
 * fields will typically be decorated with an annotation from
 * {@link com.antheminc.oss.nimbus.domain.defn.ViewConfig}. Being associated
 * with a field in this way means that the path arguments provided in this
 * instance's constructor is equivalent to the field name identified by
 * {@code relativePath}.
 * 
 * @author Tony Lopez
 * @since 1.1
 *
 * @param <P>
 *            The parent &#64;{@code UnitTestPage} of this modal.
 */
@Getter
@Setter
public abstract class UnitTestPageComponent<P extends UnitTestPage> extends UnitTestPage {

	public static final String EVENT_NOTIFY_ENDPOINT = "/event/notify";

	private final P parent;
	protected final String relativePath;

	/**
	 * <p>
	 * Base constructor for this class, intended to be used by all other
	 * constructors to initialize the required data in order to interact with a
	 * page from the framework perspective.
	 * 
	 * @param parent
	 *            the parent {@code UnitTestPage} owning the component
	 *            associated with this instance.
	 * @param relativePath
	 *            the path to the component associated with this instance,
	 *            relative to the core or view root domain. (e.g. the relative
	 *            path is represented by {@code "a/b/c/..."} in:
	 *            {@code /clientId/clientApp/p/domain:refId/pageId/[a/b/c/...]})
	 */
	public UnitTestPageComponent(P parent, String relativePath) {
		super(parent.beanResolver, parent.getClientId(), parent.getClientApp(), parent.getCoreDomainAlias(),
				parent.getViewDomainAlias(), parent.getPageId(), parent.getRefId());
		this.parent = parent;
		this.relativePath = relativePath;
	}

	/**
	 * <p>
	 * Builds and executes a framework request using {@link Action._get}, to the
	 * component param represented by this instance. Afterwards, the method
	 * attempts to retrieve and return the first param from the framework
	 * response (via calling {@link MultiOutput#getSingleResult()}).
	 * 
	 * @return the page param represented by this instance
	 */
	@Override
	public <T> Param<T> getParam() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(this.getViewRootDomainURI())
				.addRefId(this.getParent().getRefId()).addNested(this.relativePath).addAction(Action._get).getMock();

		return this.extractResponse(request, null);
	}

	/**
	 * <p>
	 * Invokes framework HTTP post request to {@code /event/notify} with
	 * {@code event} data.
	 * 
	 * @param event
	 *            the event data to send
	 * @return the response as received from the framework
	 * @see ModelEvent
	 */
	protected Object doEventNotify(ModelEvent<String> event) {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(this.getBaseURI())
				.addNested(EVENT_NOTIFY_ENDPOINT).getMock();

		return this.controller.handleEventNotify(request, event);
	}

	/**
	 * <p>
	 * Convenience method that builds and executes a framework request to the
	 * param identified by {@code path}, relative to the parent page param
	 * represented by this instance. The method will next attempt to return the
	 * first param retrieved from the response, via calling
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
	@Override
	protected <T> T doRequestAndGetLeafState(String path) {
		return super.doRequestAndGetLeafState(this.relativePath + path);
	}
}
