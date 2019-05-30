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
package com.antheminc.oss.nimbus.test.domain.support.pageobject.common;

/**
 * <p>
 * A testing support facade implementation that provides support for subclasses
 * to interact with the Framework by abstracting many of the common framework
 * invocations into common utility methods.
 * 
 * <p>
 * This interface is used to describe a component who is able to receive user
 * input in the form of an &#64;{@code Form} component.
 * 
 * @author Tony Lopez
 * @since 1.1
 *
 * @param <P>
 *            The parent &#64;{@code UnitTestPage} of this component.
 * @param <F>
 *            The object represented by the form within this component.
 */
public interface FormFillableComponent<P, F> {

	/**
	 * <p>
	 * Simulates the user filling in the data provided as {@code form} in the
	 * component.
	 * 
	 * @param form
	 *            the data to input into the component.
	 * @return the parent {@code UnitTestPage} object that would be displayed
	 *         after interacting with the component
	 */
	P fillForm(F form);
}
