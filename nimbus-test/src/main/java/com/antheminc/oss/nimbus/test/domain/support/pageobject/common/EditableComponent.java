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
 * This interface is used to describe a component who is able to to be edited
 * and should return a parent component after interacting with it.
 * 
 * @author Tony Lopez
 * @since 1.1
 *
 * @param <P>
 *            The parent &#64;{@code UnitTestPage} of this component.
 */
public interface EditableComponent<P> extends CloseableComponent<P> {

	/**
	 * <p>
	 * Simulates the user clicking the edit button on the component.
	 * 
	 * @return the parent {@code UnitTestPage} object that would be displayed
	 *         after interacting with the component
	 */
	P click_edit();
}
