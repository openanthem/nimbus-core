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

import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

import lombok.Getter;

/**
 * <p>
 * A testing support facade implementation that provides support for subclasses
 * to interact with the Framework by abstracting many of the common framework
 * invocations into common utility methods.
 * 
 * <p>
 * Subclassing {@code UnitTestFormModal} will allow for the simplification of
 * test cases by abstracting the the underlying framework integrations into the
 * subclass, so that the unit test class is able to focus on its primary
 * concern: testing. These subclasses can achieve this by containing customized
 * methods for interacting with specific framework components, so that the unit
 * test case must only interact the {@code UnitTestPage} subclass, instead of
 * interacting with the framework directly.
 * 
 * <p>
 * Typically, it is the aim of {@code UnitTestFormModal} to be associated with
 * domain entity fields that are decorated with &#64;{@code Modal} which expects
 * user input via form entry. Being associated with a field in this way means
 * that the path arguments provided in this instance's constructor is equivalent
 * to the field name decorated with &#64;{@code Modal}. If a Form is not needed,
 * consider using {@link UnitTestModal}.
 * 
 * @author Tony Lopez
 * @since 1.1
 * 
 * @param <P>
 *            The parent &#64;{@code UnitTestPage} of this modal.
 * @param <F>
 *            The object represented by the form within this modal.
 */
@Getter
public abstract class UnitTestFormModal<P extends UnitTestPage, F> extends UnitTestModal<P> {

	protected final String formParamPath;
	protected final String formParamPathFromModal;

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
	 * @param formParamPathFromModal
	 *            the path to the form param represented in this modal, relative
	 *            to the modal's path. (e.g. the path {@code "/a/b/c/form"}
	 *            would be the {@code formParamPathFromModal} in:
	 *            {@code /clientId/clientApp/p/domain:refId/pageId/tile/modal/a/b/c/form/...})
	 */
	public UnitTestFormModal(P parent, String relativePath, String formParamPathFromModal) {
		super(parent, relativePath);
		this.formParamPathFromModal = formParamPathFromModal;
		this.formParamPath = this.relativePath + formParamPathFromModal;
	}

	/**
	 * <p>
	 * Returns the param of the form owned by the modal represented by this
	 * instance.
	 * 
	 * @return the {@link Param} of the form object
	 */
	public Param<F> getFormParam() {
		return this.getParam().findParamByPath(this.formParamPathFromModal);
	}
}
