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
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.test.domain.support.pageobject.common.CloseableComponent;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * <p>
 * A testing support facade implementation that provides support for subclasses
 * to interact with the Framework by abstracting many of the common framework
 * invocations into common utility methods.
 * 
 * <p>
 * Subclassing {@code UnitTestModal} will allow for the simplification of test
 * cases by abstracting the the underlying framework integrations into the
 * subclass, so that the unit test class is able to focus on its primary
 * concern: testing. These subclasses can achieve this by containing customized
 * methods for interacting with specific framework components, so that the unit
 * test case must only interact the {@code UnitTestPage} subclass, instead of
 * interacting with the framework directly.
 * 
 * <p>
 * Typically, it is the aim of {@code UnitTestModal} to be associated with
 * domain entity fields that are decorated with &#64;{@code Modal}. Being
 * associated with a field in this way means that the path arguments provided in
 * this instance's constructor is equivalent to the field name decorated with
 * &#64;{@code Modal}. If a Form is needed (for user input), consider using
 * {@link UnitTestFormModal}.
 * 
 * @author Tony Lopez
 * @since 1.1
 * 
 * @param <P>
 *            The parent &#64;{@code UnitTestPage} of this modal.
 */
public abstract class UnitTestModal<P extends UnitTestPage> extends UnitTestPageComponent<P>
		implements CloseableComponent<P> {

	public static final String DEFAULT_CLOSE_MODAL_ALIAS = "closeModal";

	private final String closeModalAlias;

	/**
	 * <p>
	 * Base constructor for this class, intended to be used by all other
	 * constructors to initialize the required data in order to interact with
	 * this modal from the framework perspective.
	 * 
	 * @param parent
	 *            the parent {@code UnitTestPage} owning the component
	 *            associated with this instance.
	 * @param relativePath
	 *            the path to the component associated with this instance,
	 *            relative to the core or view root domain. (e.g. the relative
	 *            path is represented by {@code "a/b/c/..."} in:
	 *            {@code /clientId/clientApp/p/domain:refId/pageId/[a/b/c/...]})
	 * @param closeModalAlias
	 *            the alias of the close modal field that would be used to
	 *            invoke closing this modal
	 */
	public UnitTestModal(P parent, String relativePath, String closeModalAlias) {
		super(parent, relativePath);
		this.closeModalAlias = closeModalAlias;
	}

	/**
	 * <p>
	 * Secondary constructor for this class
	 * 
	 * <p>
	 * Calls the base constructor and sets a default alias for the "close modal"
	 * operation with {@link #DEFAULT_CLOSE_MODAL_ALIAS}.
	 * 
	 * @param parent
	 *            the parent {@code UnitTestPage} owning the component
	 *            associated with this instance.
	 * @param relativePath
	 *            the path to the component associated with this instance,
	 *            relative to the core or view root domain. (e.g. the relative
	 *            path is represented by {@code "a/b/c/..."} in:
	 *            {@code /clientId/clientApp/p/domain:refId/pageId/[a/b/c/...]})
	 * @see #UnitTestModal(UnitTestPage, String, String)
	 */
	public UnitTestModal(P parent, String relativePath) {
		this(parent, relativePath, DEFAULT_CLOSE_MODAL_ALIAS);
	}

	/**
	 * {@inheritDoc}
	 */
	public P click_close() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(this.getParent().getViewRootDomainURI())
				.addRefId(this.getParent().getRefId()).addNested(this.getRelativePath())
				.addNested(Constants.SEPARATOR_URI.code).addNested(this.closeModalAlias).addAction(Action._get)
				.getMock();

		this.controller.handlePost(request, null);
		return this.getParent();
	}
}
